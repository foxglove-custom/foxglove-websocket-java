package com.jiaruiblog.foxglove.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.schema.RawImage;
import org.apache.commons.io.IOUtils;
import org.yeauty.pojo.Session;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

public class SendRawImageThread implements Runnable {

    private int index;
    private Session session;
    private int count = 0;
    private int MAX_COUNT = 8;

    public SendRawImageThread(int index, Session session) {
        this.index = index;
        this.session = session;
    }

    @Override
    public void run() {
        try {
            while (true) {
                count++;
                if (count > MAX_COUNT) {
                    count = count % MAX_COUNT;
                }
                RawImage rawImage = readImage(count);
                JSONObject jsonObject = (JSONObject) JSON.toJSON(rawImage);
                Integer ns = rawImage.getTimestamp().getNsec();
                byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), ns, index);
                this.session.sendBinary(bytes);
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private RawImage readImage(int index) {
        RawImage image = new RawImage();
        String filePath = "E:\\foxglove\\image\\" + index + ".jpg";
        try (InputStream is = new FileInputStream(filePath)) {
            byte[] bytes = IOUtils.toByteArray(is);
            int size = bytes.length;
            byte[] newBytes = Arrays.copyOfRange(bytes, 0, size);
            BufferedImage buf = ImageIO.read(new ByteArrayInputStream(bytes));
            int height = buf.getHeight();
            int width = buf.getWidth();
            image.setStep(size);
            System.out.println(buf.getType() + "-----size:\t" + size);
            image.setWidth(width);
            image.setHeight(height);
            image.setEncoding("rgb8");
            image.setFrame_id("main");
            RawImage.Timestamp timestamp = image.new Timestamp();
            int nano = Instant.now().getNano();
            long second = Instant.now().getEpochSecond();
            timestamp.setSec((int) second);
            timestamp.setNsec(nano);
            image.setTimestamp(timestamp);
            byte[] encode = Base64.getEncoder().encode(newBytes);
            String data = new String(encode);
            image.setData(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }

}
