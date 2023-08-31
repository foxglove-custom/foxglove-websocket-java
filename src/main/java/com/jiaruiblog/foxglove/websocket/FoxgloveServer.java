package com.jiaruiblog.foxglove.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.util.SystemUtil;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.tomcat.util.buf.HexUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName FoxgloveServer
 * @Description TODO
 * @Author luojiarui
 * @Date 2023/7/31 21:55
 * @Version 1.0
 **/
@ServerEndpoint(port = "8766")
public class FoxgloveServer {

    /**
     * 与某个客户端的连接会话， 需要通过它来给客户端发送数据
     * @author 罗佳瑞
     * @date 2022/04/22
     */
    private Session session;

    /**
     * 标示连接用户的唯一信息
     * @author 罗佳瑞
     * @date 2022/04/22
     */
    private String uid;

    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam String req,
                          @RequestParam MultiValueMap reqMap, @PathVariable String uid, @PathVariable Map pathMap){

//        String submitedToken = headers.get("sec-websocket-protocol");
//        System.out.println("token信息" + submitedToken);


        System.out.println("session信息" + session.toString());
        session.setSubprotocols("foxglove.websocket.v1");

        // TODO 未实现避免同一个客户端连接用户的方法，前端避免重复连接
    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String req,
                       @RequestParam MultiValueMap reqMap, @PathVariable String uid, @PathVariable Map pathMap){
        System.out.println("这是第一次新的链接 ---》 new connection");
        System.out.println("返回的信息 " + req);

        this.session = session;
        // 指定的用户信息赋给uid信息
        this.uid =  uid;

        this.session.sendText(serverInfo().toString());
        this.session.sendText(advertise().toString());
        this.session.sendText(advertiseServices().toString());
//        this.session.sendText(xxx().toString());

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
        System.out.println("接受到消息"+message);

        JSONObject msg = JSON.parseObject(message);
        if ("subscribe".equals(msg.get("op"))) {
            JSONArray subscriptions = msg.getJSONArray("subscriptions");
            System.out.println(subscriptions);

            String str = "AQAAAABoXkDl4Uh3F3sibXNnIjogIkhlbGxvISIsICJjb3VudCI6IDU0fQ==";

            String hexStr = "0100000000a071a1e52f4977177b226d7367223a202248656c6c6f21222c2022636f756e74223a20313731367d";

            String strUtf8 = "\u0001\u0000\u0000\u0000\u0000�q��/Iw\u0017{\"msg\": \"Hello!\", \"count\": 1716}";

            String getff = "AAAAAMCTE44BWn4XeyJtc2ciOiJoZWxsbyIsImNvdW50IjoiOTk5OTkifQ==";

            byte[] bytes = Base64.decodeBase64(str);

            byte[] bytes1 = HexUtils.fromHexString(hexStr);
            System.out.println(new String(bytes1));

            System.out.println(new String(bytes));

            int i = 1;
            while (true) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "hello");
                Random random = new Random();

                double systemCpuLoad = SystemUtil.getSystemCpuLoad();


                jsonObject.put("count", systemCpuLoad);

                // 通道id，小端
                byte[] channelId = getIntBytes(0);
                // 当前ns时间戳，小端
                byte[] timestamp = getLongBytes2(System.currentTimeMillis() * 1000 * 1000);

//                String aa = "{\"msg\": \"Hello!\", \"count\": 51}";

                byte[] bytes2 = jsonObject.toJSONString().getBytes();

//                bytes2 = aa.getBytes();

                byte[] bytes3 = byteConcat(channelId, timestamp, bytes2);

//                for (byte b : bytes3) {
//                    System.out.println(b);
//                }

                long[] vas = new long[]{1, 0};

                byte[] pack1 = new byte[5];
                for (int j = 0; j < pack1.length; j++) {
                    if (j == 0) {
                        pack1[j] = Byte.parseByte("1");
                    } else {
                        pack1[j] = Byte.parseByte("0");
                    }
                }

                Long ns = 1692891094326598000L;

                byte[] longBytes2 = getLongBytes2(ns);

                byte[] bytes4 = byteConcat(pack1, timestamp, bytes2);

                byte constantInfo = 1;
                byte[] constantInfoByte = new byte[]{constantInfo};

                byte[] dataType = getIntBytes(0);

                byte[] nsTime = getLongBytes(ns);

                byte[] packz1 = byteConcat(constantInfoByte, dataType, nsTime);

                byte[] pack2 = byteConcat(packz1, bytes2);


//            session.sendBinary(str.getBytes());
                session.sendBinary(pack2);
                Thread.sleep(1000);
                i ++;
            }



        }

    }

    static final long fx = 0xffL;

    /**
     * int 转 byte[]
     * 小端
     * @param data
     * @return
     */
    public static byte[] getIntBytes(int data) {
        int length = 4;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) ((data >> (i*8)) & fx);
        }
        return bytes;
    }

    /**
     * long 转 byte[]
     * 小端
     * @param data
     * @return
     */
    public static byte[] getLongBytes(long data) {
        int length = 8;
        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) ((data >> (i*8)) & fx);
        }
        return bytes;
    }

    /**
     * long 转 byte[]
     * 小端
     * @param data
     * @return
     */
    public static byte[] getLongBytes2(long data) {
        int length = 8;
        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) ((data >> (i*8)) & fx);
        }
        return bytes;
    }

    public static byte[] byteConcat(byte[] bt1, byte[] bt2){
        byte[] bt4 = new byte[bt1.length+bt2.length];
        int len = 0;
        System.arraycopy(bt1, 0, bt4, 0, bt1.length);
        len += bt1.length;
        System.arraycopy(bt2, 0, bt4, len, bt2.length);
        return bt4;
    }

    public static byte[] byteConcat(byte[] bt1, byte[] bt2, byte[] bt3){
        byte[] bt4 = new byte[bt1.length+bt2.length+bt3.length];
        int len = 0;
        System.arraycopy(bt1, 0, bt4, 0, bt1.length);
        len += bt1.length;
        System.arraycopy(bt2, 0, bt4, len, bt2.length);
        len += bt2.length;
        System.arraycopy(bt3, 0, bt4, len, bt3.length);
        return bt4;
    }


    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println("发的是非得失" + b);
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

    public JSONObject serverInfo() {

        String str = "{\n" +
                "  \"op\":\"serverInfo\",\n" +
                "  \"name\":\"example server\",\n" +
                "  \"capabilities\":[\"clientPublish\",\"services\"],\n" +
                "  \"supportedEncodings\":[\"json\"],\n" +
                "  \"metadata\":null,\n" +
                "  \"sessionId\":null\n" +
                "}";

        return JSON.parseObject(str);

    }

    public JSONObject advertise() {


        String str = "{\n" +
                "  \"op\":\"advertise\",\n" +
                "  \"channels\":[\n" +
                "    {\n" +
                "    \"id\":1,\n" +
                "      \"topic\":\"example_msg\",\n" +
                "      \"encoding\":\"json\",\n" +
                "      \"schemaName\":\"ExampleMsg\",\n" +
                "      \"schema\":\n" +
                "      \"{\\\"type\\\": \\\"object\\\", \\\"properties\\\": {\\\"msg\\\": {\\\"type\\\": \\\"string\\\"}, \\\"count\\\": {\\\"type\\\": \\\"number\\\"}}}\",\n" +
                "      \"schemaEncoding\":\"jsonschema\"\n" +
                "    }," +
                " {\n" +
                "       \"id\":0,\n" +
                "      \"topic\":\"raw_image\",\n" +
                "      \"encoding\":\"json\",\n" +
                "      \"schemaName\":\"RawImage\",\n" +
                "      \"schema\":\n" +
                "      \"{\\\"title\\\":\\\"foxglove.RawImage\\\",\\\"description\\\":\\\"A raw image\\\",\\\"$comment\\\":\\\"Generated by https://github.com/foxglove/schemas\\\",\\\"type\\\":\\\"object\\\",\\\"properties\\\":{\\\"timestamp\\\":{\\\"type\\\":\\\"object\\\",\\\"title\\\":\\\"time\\\",\\\"properties\\\":{\\\"sec\\\":{\\\"type\\\":\\\"integer\\\",\\\"minimum\\\":0},\\\"nsec\\\":{\\\"type\\\":\\\"integer\\\",\\\"minimum\\\":0,\\\"maximum\\\":999999999}},\\\"description\\\":\\\"Timestamp of image\\\"},\\\"frame_id\\\":{\\\"type\\\":\\\"string\\\",\\\"description\\\":\\\"Frame of reference for the image. The origin of the frame is the optical center of the camera. +x points to the right in the image, +y points down, and +z points into the plane of the image.\\\"},\\\"width\\\":{\\\"type\\\":\\\"integer\\\",\\\"minimum\\\":0,\\\"description\\\":\\\"Image width\\\"},\\\"height\\\":{\\\"type\\\":\\\"integer\\\",\\\"minimum\\\":0,\\\"description\\\":\\\"Image height\\\"},\\\"encoding\\\":{\\\"type\\\":\\\"string\\\",\\\"description\\\":\\\"Encoding of the raw image data\\\\n\\\\nSupported values: `8UC1`, `8UC3`, `16UC1`, `32FC1`, `bayer_bggr8`, `bayer_gbrg8`, `bayer_grbg8`, `bayer_rggb8`, `bgr8`, `bgra8`, `mono8`, `mono16`, `rgb8`, `rgba8`, `uyvy` or `yuv422`, `yuyv` or `yuv422_yuy2`\\\"},\\\"step\\\":{\\\"type\\\":\\\"integer\\\",\\\"minimum\\\":0,\\\"description\\\":\\\"Byte length of a single row\\\"},\\\"data\\\":{\\\"type\\\":\\\"string\\\",\\\"contentEncoding\\\":\\\"base64\\\",\\\"description\\\":\\\"Raw image data\\\"}}}\",\n" +
                "      \"schemaEncoding\":\"jsonschema\"\n" +
                "    }" +
                "]\n" +
                "}";




        return JSONObject.parseObject(str);
    }

    public JSONObject advertiseServices() {
        String str = "{\"op\":\"advertiseServices\",\n" +
                "  \"services\":[\n" +
                "    {\n" +
                "      \"id\":0,\n" +
                "      \"name\":\"example_set_bool\",\n" +
                "      \"requestSchema\":\n" +
                "      \"{\\\"type\\\": \\\"object\\\", \\\"properties\\\": {\\\"data\\\": {\\\"type\\\": \\\"boolean\\\"}}}\",\n" +
                "      \"responseSchema\":\n" +
                "      \"{\\\"type\\\": \\\"object\\\", \\\"properties\\\": {\\\"success\\\": {\\\"type\\\": \\\"boolean\\\"}, \\\"message\\\": {\\\"type\\\": \\\"string\\\"}}}\",\n" +
                "      \"type\":\"example_set_bool\"\n" +
                "    }]\n" +
                "}";
        return JSONObject.parseObject(str);
    }

    public JSONObject xxx() {
        String str = "{\"op\":\"advertise\",\"channels\":[{\"id\":1,\"topic\":\"/move_base_simple/goal\",\"encoding\":\"json\",\"schemaName\":\"geometry_msgs/PoseStamped\"}]}";

        return JSONObject.parseObject(str);
    }

    public static String byteToHex(byte b)
    {
        String hexString = Integer.toHexString(b & 0xFF);
        //由于十六进制是由0~9、A~F来表示1~16，所以如果Byte转换成Hex后如果是<16,就会是一个字符（比如A=10），通常是使用两个字符来表示16进制位的,
        //假如一个字符的话，遇到字符串11，这到底是1个字节，还是1和1两个字节，容易混淆，如果是补0，那么1和1补充后就是0101，11就表示纯粹的11
        if (hexString.length() < 2)
        {
            hexString = new StringBuilder(String.valueOf(0)).append(hexString).toString();
        }
        return hexString.toUpperCase();
    }

    /**
     * 字节数组转Hex
     * @param bytes 字节数组
     * @return Hex
     */
    public static String bytesToHex(byte[] bytes)
    {
        StringBuffer sb = new StringBuffer();
        if (bytes != null && bytes.length > 0)
        {
            for (int i = 0; i < bytes.length; i++) {
                String hex = byteToHex(bytes[i]);
                sb.append(hex);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        Long ns = 1692891094326598000L;

        byte[] longBytes2 = getLongBytes2(ns);


        for (byte b : longBytes2) {
            System.out.println(byteToHex(b));
        }

        System.out.println("*******");
        short shortData = 0;
        byte[] shortBytes = getShortBytes(shortData);

        for (byte shortByte : shortBytes) {
            System.out.println(byteToHex(shortByte));
        }

        System.out.println("&&&&");

        byte a = 1;

        System.out.println(byteToHex(a));

    }

//    static final long fx = 0xffl;
    /**
     * short 转 byte[]
     * 小端
     * @param data
     * @return
     */
    public static byte[] getShortBytes(short data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & fx);
        bytes[1] = (byte) ((data >> 8) & fx);
        return bytes;
    }


}
