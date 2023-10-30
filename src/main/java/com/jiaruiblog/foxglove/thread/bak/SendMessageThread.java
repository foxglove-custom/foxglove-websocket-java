package com.jiaruiblog.foxglove.thread.bak;

import com.alibaba.fastjson.JSONObject;
import org.yeauty.pojo.Session;

import java.time.LocalTime;
import java.util.Random;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

public class SendMessageThread implements Runnable {
    private int index;
    private Session session;
    private int frequency;

    public SendMessageThread(int index, int frequency, Session session) {
        this.index = index;
        this.session = session;
        this.frequency = frequency;
    }

    @Override
    public void run() {

        while (true) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "hello at " + LocalTime.now());

            jsonObject.put("count", new Random().nextInt(1000));
            jsonObject.put("number", new Random().nextInt(1000));

            byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), index);
            this.session.sendBinary(bytes);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
