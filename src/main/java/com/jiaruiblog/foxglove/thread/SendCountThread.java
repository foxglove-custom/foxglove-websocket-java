package com.jiaruiblog.foxglove.thread;

import com.alibaba.fastjson.JSONObject;
import org.yeauty.pojo.Session;

import java.time.LocalTime;
import java.util.Random;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

public class SendCountThread implements Runnable {
    private Session session;

    public SendCountThread(Session session) {
        this.session = session;
    }

    @Override
    public void run() {

        while (true) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "hello at " + LocalTime.now());

            jsonObject.put("count", new Random().nextInt(1000));
            jsonObject.put("number", new Random().nextInt(1000));

            Long ns = 1692891094326598000L;
            byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), ns, 0);
            this.session.sendBinary(bytes);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
