package com.jiaruiblog.foxglove.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.schema.CompressedImage;
import com.jiaruiblog.foxglove.schema.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.yeauty.pojo.Session;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.Instant;
import java.util.Base64;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

@Slf4j
public class SendImageThread extends SendDataThread {

    private int MAX_COUNT = 1445;

    private String rtsp = "rtsp://127.0.0.1:8554/demo";

    public SendImageThread(int index, int frequency, Session session) {
        super(index, frequency, session);
    }

    @Override
    public void run() {
        sendImageByRTSP();
    }

    private void sendImageByRTSP() {
        FFmpegFrameGrabber grabber = null;
        try {
            grabber = FFmpegFrameGrabber.createDefault(rtsp);
            grabber.setOption("rtsp_transport", "tcp"); // 使用tcp的方式，不然会丢包很严重
            grabber.setOption("stimeout", "500000");
            //设置缓存大小，提高画质、减少卡顿花屏
            grabber.setOption("buffer_size", "1024000");
            grabber.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();
            Frame frame;
            long startTime = System.currentTimeMillis();
            long frameCount = 0;
            while ((frame = grabber.grabImage()) != null) {
                // 按照指定频率处理帧
                if ((System.currentTimeMillis() - startTime) < (frameCount * 1000 / frequency)) {
                    continue;
                }

                CompressedImage compressedImage = new CompressedImage();
                Timestamp timestamp = new Timestamp();
                int nano = Instant.now().getNano();
                long second = Instant.now().getEpochSecond();
                timestamp.setSec((int) second);
                timestamp.setNsec(nano);

                BufferedImage image = converter.getBufferedImage(frame);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);

                byte[] encode = Base64.getEncoder().encode(baos.toByteArray());
                String data = new String(encode);
                compressedImage.setData(data);
                compressedImage.setFormat("png");
                compressedImage.setFrame_id("main");
                compressedImage.setTimestamp(timestamp);

                JSONObject jsonObject = (JSONObject) JSON.toJSON(compressedImage);
                byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), compressedImage.getTimestamp().getNsec(), index);
                this.session.sendBinary(bytes);

                // long类型不用担心溢出
                frameCount++;

                printLog(100);
            }

        } catch (FFmpegFrameGrabber.Exception e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                grabber.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendImageByLocal() {
        try {
            int count = 0;
            while (true) {
                count++;
                if (count > MAX_COUNT) {
                    count = count % MAX_COUNT;
                }
                CompressedImage rawImage = readImageLocal(count);
                JSONObject jsonObject = (JSONObject) JSON.toJSON(rawImage);
                byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), rawImage.getTimestamp().getNsec(), index);
                this.session.sendBinary(bytes);
                Thread.sleep(frequency);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private CompressedImage readImageLocal(int index) {
        CompressedImage image = new CompressedImage();
        String filePath = "E:\\foxglove\\image_rtsp\\" + index + ".png";
        try (InputStream is = new FileInputStream(filePath)) {
            Timestamp timestamp = new Timestamp();
            int nano = Instant.now().getNano();
            long second = Instant.now().getEpochSecond();
            timestamp.setSec((int) second);
            timestamp.setNsec(nano);
            image.setTimestamp(timestamp);

            byte[] bytes = IOUtils.toByteArray(is);
            image.setFormat("jpeg");
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