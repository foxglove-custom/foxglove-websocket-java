package com.visualization.foxglove.thread;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.yeauty.pojo.Session;

import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public class SendDataThread extends Thread {

    protected int frequency;
    protected int index;
    protected Session session;
    protected volatile String chassisCode;
    protected volatile boolean running = true;

    protected int CHASSIS_NO_DATA_SLEEP = 10;

    protected int count = 0;

    private static final int PRINT_LOG_THRESHOLD = 200;

    public SendDataThread(int index, Session session) {
        this.index = index;
        this.session = session;
    }


    /**
     * 在达到阈值时打印消息
     */
    protected void printLog(int threshold) {
        if (count == threshold) {
            log.info("------------" + Thread.currentThread().getName() + " sent data---------");
            count = 0;
        }
        count++;
    }

    /**
     * 在达到阈值时打印消息
     */
    protected void printLog() {
        printLog(PRINT_LOG_THRESHOLD);
    }

    public void stopThread() {
        this.running = false;
    }

    protected void printChassisNoDataMessage() throws InterruptedException {
        log.info("===================" + Thread.currentThread().getName() + "没有获取到数据=========================");
        TimeUnit.SECONDS.sleep(CHASSIS_NO_DATA_SLEEP);
    }
}
