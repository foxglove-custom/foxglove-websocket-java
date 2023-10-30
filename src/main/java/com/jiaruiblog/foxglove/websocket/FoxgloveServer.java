package com.jiaruiblog.foxglove.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.entity.Advertise;
import com.jiaruiblog.foxglove.entity.ServerInfo;
import com.jiaruiblog.foxglove.message.test.GPSGenerator;
import com.jiaruiblog.foxglove.message.test.RawMessageGenerator;
import com.jiaruiblog.foxglove.message.test.Scene3DGenerator;
import com.jiaruiblog.foxglove.thread.SendDataThread;
import com.jiaruiblog.foxglove.thread.bak.Send3DStreamThread;
import com.jiaruiblog.foxglove.thread.bak.SendGPSThread;
import com.jiaruiblog.foxglove.thread.bak.SendMessageThread;
import com.jiaruiblog.foxglove.util.ChannelUtil;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FoxgloveServer
 * @Description TODO
 * @Author luojiarui
 * @Date 2023/7/31 21:55
 * @Version 1.0
 **/
@ServerEndpoint(port = "8767")
public class FoxgloveServer {

    /**
     * 与某个客户端的连接会话， 需要通过它来给客户端发送数据
     *
     * @author 罗佳瑞
     * @date 2022/04/22
     */
    private Session session;

    /**
     * 标示连接用户的唯一信息
     *
     * @author 罗佳瑞
     * @date 2022/04/22
     */
    private String uid;

    private boolean connected = false;

    private List<Thread> threadList = new ArrayList<>();

    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam String req,
                          @RequestParam MultiValueMap reqMap, @PathVariable String uid, @PathVariable Map pathMap) {
        System.out.println("session信息" + session.toString());
        session.setSubprotocols("foxglove.websocket.v1");

        // TODO 未实现避免同一个客户端连接用户的方法，前端避免重复连接
    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String req,
                       @RequestParam MultiValueMap reqMap, @PathVariable String uid, @PathVariable Map pathMap) {
        System.out.println("这是第一次新的链接 ---》 new connection");
        System.out.println("返回的信息 " + req);

        this.session = session;
        // 指定的用户信息赋给uid信息
        this.uid = uid;

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
        System.out.println("one connection closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("接受到消息" + message);

        JSONObject msg = JSON.parseObject(message);
        Object op = msg.get("op");
        System.out.println("-------------msg:\t" + message);
        boolean opCheck = "subscribe".equals(op);
        if (opCheck && !connected) {
            connected = true;
            System.out.println("----------开启多线程，循环发送-------------");
            JSONArray subscriptions = msg.getJSONArray("subscriptions");
            System.out.println("subscriptions: " + subscriptions);

            System.out.println("=================previous thread size:\t" + threadList.size());
            threadList.forEach(t -> t.interrupt());

            Thread sendMessageThread = new Thread(new SendDataThread(0, 100, session, new RawMessageGenerator()));
            sendMessageThread.start();

            Thread send3DThread = new Thread(new SendDataThread(1, 100, session, new Scene3DGenerator()));
            send3DThread.start();

            Thread sendGPSThread = new Thread(new SendDataThread(2, 100, session, new GPSGenerator()));
            sendGPSThread.start();

            threadList.clear();
            threadList.add(sendMessageThread);
            threadList.add(sendGPSThread);
            threadList.add(send3DThread);
        }
    }


    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        // 这里接收到用户指令
        String message = new String(Arrays.copyOfRange(bytes, 5, bytes.length));
        System.out.println(message);
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.sendText(message);
    }

}
