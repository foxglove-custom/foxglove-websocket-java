package com.jiaruiblog.foxglove.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.schema.CompressedImage;
import org.apache.commons.io.IOUtils;
import org.yeauty.pojo.Session;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

public class SendCompressedImageThread implements Runnable {

    private int index;
    private Session session;
    private int count = 0;
    private int MAX_COUNT = 8;

    public SendCompressedImageThread(int index, Session session) {
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
                CompressedImage rawImage = readImage(count);
                JSONObject jsonObject = (JSONObject) JSON.toJSON(rawImage);
                long ns = 1692891094326598000L;
                byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), ns, index);
                this.session.sendBinary(bytes);
//                System.out.println("----------send compressed image");
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private CompressedImage readImage(int index) {
        CompressedImage image = new CompressedImage();
        String filePath = "E:\\foxglove\\image\\" + index + ".jpg";
        try (InputStream is = new FileInputStream(filePath)) {
            byte[] bytes = IOUtils.toByteArray(is);
            image.setFormat("jpeg");
            image.setFrame_id(String.valueOf(index));
            CompressedImage.Timestamp timestamp = image.new Timestamp();
            timestamp.setSec(index * index * index % 20);
            timestamp.setNsec(index);
            image.setTimestamp(timestamp);
            byte[] encode = Base64.getEncoder().encode(bytes);
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
