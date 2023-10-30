package com.jiaruiblog.foxglove.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.message.MessageGenerator;
import lombok.extern.slf4j.Slf4j;
import org.yeauty.pojo.Session;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

@Slf4j
public class SendDataThread implements Runnable {

    private int frequency;
    private int index;
    private Session session;
    private MessageGenerator<?> generator;

    public SendDataThread(int index, int frequency, Session session, MessageGenerator<?> generator) {
        this.index = index;
        this.session = session;
        this.frequency = frequency;
        this.generator = generator;
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Object message = generator.consume();
            JSONObject jsonObject = (JSONObject) JSON.toJSON(message);
            byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), index);
            this.session.sendBinary(bytes);
            log.info(Thread.currentThread().getName() + "\tsend data" + "\t" + Thread.currentThread().isInterrupted());
            try {
                Thread.sleep(frequency);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }
}
