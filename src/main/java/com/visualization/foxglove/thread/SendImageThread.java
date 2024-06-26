package com.visualization.foxglove.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.visualization.foxglove.config.AppCtxUtil;
import com.visualization.foxglove.config.DataConfig;
import com.visualization.foxglove.schema.CompressedImage;
import com.visualization.foxglove.schema.Timestamp;
import com.visualization.foxglove.util.DataUtil;
import com.visualization.foxglove.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.yeauty.pojo.Session;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class SendImageThread extends SendDataThread {

    private DataConfig dataConfig;

    public SendImageThread(int index, Session session) {
        super(index, session);
        this.dataConfig = AppCtxUtil.getBean(DataConfig.class);
        this.frequency = dataConfig.getRtsp().getFrequency();
    }

    @Override
    public void run() {
        sendImageByRTSP();
    }

    private void sendImageByRTSP() {
        FFmpegFrameGrabber grabber = null;
        try {
            DataConfig.RTSP rtsp = dataConfig.getRtsp();
            grabber = FFmpegFrameGrabber.createDefault(rtsp.getUrl());
            grabber.setOption("rtsp_transport", "tcp"); // 使用tcp的方式，不然会丢包很严重
            grabber.setOption("stimeout", "50000000");
            //设置缓存大小，提高画质、减少卡顿花屏
            grabber.setOption("buffer_size", "1024000");
            grabber.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();
            Frame frame;
            long startTime = System.currentTimeMillis();
            long frameCount = 0;
            String imageFormat = "jpg";
            while ((frame = grabber.grabImage()) != null && running) {
                // 按照指定频率处理帧
                if ((System.currentTimeMillis() - startTime) < (frameCount * 1000 / frequency)) {
                    continue;
                }

                CompressedImage compressedImage = new CompressedImage();
                Timestamp timestamp = DateUtil.createTimestamp();

                BufferedImage image = converter.getBufferedImage(frame);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, imageFormat, baos);

                byte[] encode = Base64.getEncoder().encode(baos.toByteArray());
                String data = new String(encode);
                compressedImage.setData(data);
                compressedImage.setFormat(imageFormat);
                compressedImage.setFrameId("main_image");
                compressedImage.setTimestamp(timestamp);

                JSONObject jsonObject = (JSONObject) JSON.toJSON(compressedImage);
                byte[] bytes = DataUtil.getFormattedBytes(jsonObject.toJSONString().getBytes(), index);
                this.session.sendBinary(bytes);

                // long类型不用担心溢出
                frameCount++;

                printLog(100);
            }
            log.info("--------------------图片发送线程停止运行: " + Thread.currentThread().getName());
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

}
