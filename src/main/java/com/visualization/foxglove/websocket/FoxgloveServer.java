package com.visualization.foxglove.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.visualization.foxglove.entity.Advertise;
import com.visualization.foxglove.entity.ServerInfo;
import com.visualization.foxglove.entity.Subscription;
import com.visualization.foxglove.thread.SendDataThread;
import com.visualization.foxglove.util.ChannelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.visualization.foxglove.util.ChannelUtil.getChannelName;
import static com.visualization.foxglove.util.ChannelUtil.getKafkaSendThread;

/**
 * @ClassName FoxgloveServer
 * @Description TODO
 * @Author luojiarui
 * @Date 2023/7/31 21:55
 * @Version 1.0
 **/
@Slf4j
@ServerEndpoint(port = "8767")
public class FoxgloveServer {

    /**
     * 与某个客户端的连接会话， 需要通过它来给客户端发送数据
     *
     * @author 罗佳瑞
     * @date 2022/04/22
     */
    private Session session;

    private String chassisCode;

    private Map<Integer, SendDataThread> threadMap = new HashMap<>();

    public static final String EMPTY_CHASSIS_CODE = "NaN";

    @BeforeHandshake
    public void handshake(Session session) {
        log.info("----------session信息" + session.toString());
        session.setSubprotocols("foxglove.websocket.v1");

        this.closeAllThreads();
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("这是一次新的链接 ---》 new connection" + session.hashCode());

        this.session = session;
        // 指定的用户信息赋给uid信息

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setOp("serverInfo");
        serverInfo.setName("foxglove data render");
        serverInfo.setCapabilities(Arrays.asList("clientPublish", "services"));
        serverInfo.setSupportedEncodings(Arrays.asList("json"));

        String severInfoString = JSON.toJSONString(serverInfo);

        this.session.sendText(severInfoString);
        Advertise advertise = new Advertise();
        advertise.setOp("advertise");
        advertise.setChannels(ChannelUtil.createChannels());

        this.session.sendText(JSON.toJSONString(advertise));
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // 从对象集合中删除该连接对象
        log.info("-------one connection closed");
        session.close();
        this.closeAllThreads();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        JSONObject msg = JSON.parseObject(message);
        String op = msg.getString("op");
        log.info("-------------on open msg:\t" + message);
        switch (op) {
            case "subscribe":
                this.createThread(msg);
                break;
            case "unsubscribe":
                this.stopThread(msg);
                break;
        }
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        // 这里接收到用户指令
        String data = new String(Arrays.copyOfRange(bytes, 5, bytes.length));
        JSONObject message = JSON.parseObject(data);
        log.info("--------binary message:\t" + message);
        String newChassisCode = message.getString("chassis_code");
        if (!StringUtils.equals(chassisCode, newChassisCode)) {
            chassisCode = newChassisCode;
            threadMap.forEach((k, v) -> v.setChassisCode(chassisCode));
        }
    }

    private void createThread(JSONObject msg) {
        List<Subscription> subscribeList = msg.getObject("subscriptions", new TypeReference<List<Subscription>>() {
        });
        log.info("============开始创建基于channel的数据发送线程==============" + subscribeList);
        for (Subscription sub : subscribeList) {
            Integer channelId = sub.getChannelId();
            SendDataThread thread = getKafkaSendThread(sub.getId(), channelId, session);
            String threadName = "thread-" + getChannelName(channelId) + "-" + RandomStringUtils.randomAlphabetic(6).toLowerCase();
            thread.setName(threadName);
            thread.setChassisCode(chassisCode == null ? EMPTY_CHASSIS_CODE : chassisCode);
            thread.start();
            threadMap.put(channelId, thread);
        }
    }

    private void stopThread(JSONObject msg) {
        List<Integer> channelList = msg.getObject("subscriptionIds", new TypeReference<List<Integer>>() {
        });
        for (Integer channelId : channelList) {
            SendDataThread thread = threadMap.remove(channelId);
            if (thread != null) {
                thread.stopThread();
                log.info("----------------------通道" + channelId + "对应的线程已被停止");
            }
        }
    }

    private void closeAllThreads() {
        threadMap.forEach((k, v) -> v.stopThread());
        threadMap.clear();
    }

}
