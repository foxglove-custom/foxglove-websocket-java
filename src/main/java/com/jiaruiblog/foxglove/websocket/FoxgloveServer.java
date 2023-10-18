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

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

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
        ChannelInfo channelDemo = new ChannelInfo();
        channelDemo.setId(1);
        channelDemo.setTopic("示例消息");
        channelDemo.setEncoding("json");
        channelDemo.setSchemaName("示例消息(只展示字符串)");
        channelDemo.setSchema("{\"type\": \"object\", \"properties\": {\"msg\": {\"type\": \"string\"}, \"count\": {\"type\": \"number\"}}}");
        channelDemo.setSchemaEncoding("jsonschema");

        ChannelInfo channelImage = new ChannelInfo();
        channelImage.setId(2);
        channelImage.setTopic("原始图片");
        channelImage.setEncoding("json");
        channelImage.setSchemaName("原始图片");
        String schema = DataUtil.loadSchemaJson("RawImage.json");
        channelImage.setSchema(schema);
        channelImage.setSchemaEncoding("jsonschema");

        ChannelInfo channelScene = new ChannelInfo();
        channelScene.setId(3);
        channelScene.setTopic("sceneEntity");
        channelScene.setEncoding("json");
        channelScene.setSchemaName("SceneEntity");
        schema = DataUtil.loadSchemaJson("SceneUpdate.json");
        channelScene.setSchema(schema);
        channelScene.setSchemaEncoding("jsonschema");

        ChannelInfo channelFrame = new ChannelInfo();
        channelFrame.setId(4);
        channelFrame.setTopic("FrameTransforms");
        channelFrame.setEncoding("json");
        channelFrame.setSchemaName("FrameTransforms");
        schema = DataUtil.loadSchemaJson("FrameTransforms.json");
        channelFrame.setSchema(schema);
        channelFrame.setSchemaEncoding("jsonschema");

        advertise.setChannels(Arrays.asList(channelDemo, channelImage, channelScene, channelFrame));

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

                Long ns = 1692891094326598000L;
                byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), ns, 2);
                this.session.sendBinary(bytes);
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
