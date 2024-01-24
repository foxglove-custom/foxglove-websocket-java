package com.visualization.foxglove.thread;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yeauty.pojo.Session;

import java.util.concurrent.TimeUnit;

import static com.visualization.foxglove.websocket.FoxgloveServer.EMPTY_CHASSIS_CODE;

@Data
@Slf4j
public class SendDataThread extends Thread {

    protected int frequency;
    protected int index;
    protected Session session;
    protected volatile String chassisCode;
    protected volatile boolean running = true;

    protected int NO_DATA_SLEEP = 10;

    protected int loopCount = 0;

    protected int chassisNoDataCount = 0;
    protected int CHASSIS_NO_DATA_PRINT_COUNT = 10;

    private static final int PRINT_LOG_THRESHOLD = 200;

    protected boolean chassisHasData;

    public SendDataThread(int index, Session session) {
        this.index = index;
        this.session = session;
    }


    /**
     * 在达到阈值时打印消息
     */
    protected void printLog(int threshold) {
        if (loopCount == threshold) {
            log.info("------------" + Thread.currentThread().getName() + " sent data---------");
            loopCount = 0;
        }
        loopCount++;
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

    protected void printKafkaNoDataMessage() throws InterruptedException {
        log.info("===================" + Thread.currentThread().getName() + "没有获取到数据=========================");
        TimeUnit.SECONDS.sleep(NO_DATA_SLEEP);
    }

    protected void printChassisNoDataMessage() throws InterruptedException {
        if (StringUtils.equals(chassisCode, EMPTY_CHASSIS_CODE)) {
            return;
        }
        chassisNoDataCount++;
        if (chassisNoDataCount == 1) {
            log.info("===================" + Thread.currentThread().getName() + "在" + chassisCode + "下没有获取到数据=========================");
        }
        if (chassisNoDataCount == CHASSIS_NO_DATA_PRINT_COUNT) {
            chassisNoDataCount = 0;
            TimeUnit.SECONDS.sleep(NO_DATA_SLEEP);
        }
    }
}
