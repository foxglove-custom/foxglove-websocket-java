package com.jiaruiblog.foxglove.thread;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.yeauty.pojo.Session;

@Data
@Slf4j
public class SendDataThread extends Thread {

    protected int frequency;
    protected int index;
    protected Session session;
    protected volatile String code;
    protected volatile boolean running = true;

    protected int count = 0;

    public SendDataThread(int index, int frequency, Session session) {
        this.index = index;
        this.session = session;
        this.frequency = frequency;
    }


    /**
     * 在达到阈值时打印消息
     */
    protected void printLog(int threshold) {
        if (count == threshold) {
            log.info("------------" + Thread.currentThread().getName() + " sent data");
            count = 0;
        }
        count++;
    }

    public void stopThread() {
        this.running = false;
    }
}
