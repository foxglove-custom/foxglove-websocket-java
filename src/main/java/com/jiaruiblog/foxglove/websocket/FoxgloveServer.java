package com.jiaruiblog.foxglove.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.entity.Advertise;
import com.jiaruiblog.foxglove.entity.ChannelInfo;
import com.jiaruiblog.foxglove.entity.ServerInfo;
import com.jiaruiblog.foxglove.schema.SceneEntity;
import com.jiaruiblog.foxglove.schema.SceneUpdate;
import com.jiaruiblog.foxglove.util.DataUtil;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import static com.jiaruiblog.foxglove.util.DataUtil.*;

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

    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam String req,
                          @RequestParam MultiValueMap reqMap, @PathVariable String uid, @PathVariable Map pathMap) {

//        String submitedToken = headers.get("sec-websocket-protocol");
//        System.out.println("token信息" + submitedToken);


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
        serverInfo.setName("example server");
        serverInfo.setCapabilities(Arrays.asList("clientPublish", "services"));
        serverInfo.setSupportedEncodings(Arrays.asList("json"));

        String severInfoString = JSON.toJSONString(serverInfo);

        this.session.sendText(severInfoString);

        Advertise advertise = new Advertise();
        advertise.setOp("advertise");
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setId(1);
        channelInfo.setTopic("示例消息");
        channelInfo.setEncoding("json");
        channelInfo.setSchemaName("示例消息(只展示字符串)");
        channelInfo.setSchema("{\"type\": \"object\", \"properties\": {\"msg\": {\"type\": \"string\"}, \"count\": {\"type\": \"number\"}}}");
        channelInfo.setSchemaEncoding("jsonschema");

        ChannelInfo channelInfo1 = new ChannelInfo();
        channelInfo1.setId(2);
        channelInfo1.setTopic("sceneEntity");
        channelInfo1.setEncoding("json");
        channelInfo1.setSchemaName("SceneEntity");
        String schema = DataUtil.loadSchemaJson("SceneUpdate.json");
        channelInfo1.setSchema(schema);
        channelInfo1.setSchemaEncoding("jsonschema");

        ChannelInfo channelInfo2 = new ChannelInfo();
        channelInfo2.setId(3);
        channelInfo2.setTopic("FrameTransforms");
        channelInfo2.setEncoding("json");
        channelInfo2.setSchemaName("FrameTransforms");
        schema = DataUtil.loadSchemaJson("FrameTransforms.json");
        channelInfo2.setSchema(schema);
        channelInfo2.setSchemaEncoding("jsonschema");

        advertise.setChannels(Arrays.asList(channelInfo, channelInfo1, channelInfo2));

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
    public void onMessage(Session session, String message) throws Exception {
        System.out.println("接受到消息" + message);

        JSONObject msg = JSON.parseObject(message);
        Object op = msg.get("op");
        System.out.println("-------------op:\t" + op);
        if ("advertise".equals(op)) {
            JSONArray subscriptions = msg.getJSONArray("subscriptions");
            System.out.println("subscriptions: " + subscriptions);

            SendCountThread sendCountThread = new SendCountThread(session);
            sendCountThread.start();

            SendSceneThread sendSceneThread = new SendSceneThread(session);
            sendSceneThread.start();
        }
    }


    class SendCountThread extends Thread {

        private Session session;

        SendCountThread(Session session) {
            this.session = session;
        }

        @Override
        public void run() {

            while (true) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "hello at " + LocalTime.now());

                jsonObject.put("count", new Random().nextInt(1000));
                jsonObject.put("number", new Random().nextInt(1000));

                byte[] bytes2 = jsonObject.toJSONString().getBytes();
                byte[] pack1 = new byte[5];
                for (int j = 0; j < pack1.length; j++) {
                    if (j == 0) {
                        pack1[j] = Byte.parseByte("1");
                    } else {
                        pack1[j] = Byte.parseByte("0");
                    }
                }

                Long ns = 1692891094326598000L;
                byte constantInfo = 1;
                byte[] constantInfoByte = new byte[]{constantInfo};
                byte[] dataType = getIntBytes(0);
                byte[] nsTime = getLongBytes(ns);
                byte[] packz1 = byteConcat(constantInfoByte, dataType, nsTime);
                byte[] pack2 = byteConcat(packz1, bytes2);


                this.session.sendBinary(pack2);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    class SendSceneThread extends Thread {

        private Session session;

        SendSceneThread(Session session) {
            this.session = session;
        }

        @Override
        public void run() {
            int i = 0;
            while (true) {

                SceneEntity sceneEntity = new SceneEntity();
                SceneEntity.SceneTimestamp sceneTimestamp = sceneEntity.new SceneTimestamp();
                sceneTimestamp.setSec(i * i * i % 20);
                sceneTimestamp.setNsec(i++ % 20);
                sceneEntity.setTimestamp(sceneTimestamp);

                SceneUpdate sceneUpdate = new SceneUpdate();
                sceneUpdate.setEntities(Arrays.asList(sceneEntity));
                JSONObject jsonObject = (JSONObject) JSON.toJSON(sceneUpdate);

                byte[] bytes2 = jsonObject.toJSONString().getBytes();
                Long ns = 1692891094326598000L;
                byte constantInfo = 1;
                byte[] constantInfoByte = new byte[]{constantInfo};

                byte[] dataType = getIntBytes(1);

                byte[] nsTime = getLongBytes(ns);

                byte[] packz1 = byteConcat(constantInfoByte, dataType, nsTime);

                byte[] pack2 = byteConcat(packz1, bytes2);
                this.session.sendBinary(pack2);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        // 这里接收到用户指令
        for (byte b : bytes) {
            System.out.println("----byte:\t" + b);
        }
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
