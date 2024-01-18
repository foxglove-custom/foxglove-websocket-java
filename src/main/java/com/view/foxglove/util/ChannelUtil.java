package com.view.foxglove.util;

import com.view.foxglove.entity.ChannelInfo;
import com.view.foxglove.thread.SendChassisThread;
import com.view.foxglove.thread.SendDataThread;
import com.view.foxglove.thread.SendImageThread;
import com.view.foxglove.thread.kafka.Send3DKafkaThread;
import com.view.foxglove.thread.kafka.SendGPSKafkaThread;
import com.view.foxglove.thread.kafka.SendMessageKafkaThread;
import com.view.foxglove.thread.test.Send3DThread;
import com.view.foxglove.thread.test.SendGPSThread;
import com.view.foxglove.thread.test.SendMessageThread;
import org.yeauty.pojo.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChannelUtil {

    public static List<ChannelInfo> createChannels() {
        String schema;

        ChannelInfo channelChassis = new ChannelInfo();
        channelChassis.setId(0);
        channelChassis.setTopic("/drive/chassis_code");
        channelChassis.setEncoding("json");
        channelChassis.setSchemaName("底盘编码(用于切换播放源)");
        channelChassis.setSchema("{\"type\": \"object\", \"properties\": {\"chassis_code\": {\"type\": \"string\"}, \"rtsp_url\": {\"type\": \"string\"}}}");
        channelChassis.setSchemaEncoding("jsonschema");

        ChannelInfo channelControl = createControlData(1);

        ChannelInfo channel3D = new ChannelInfo();
        channel3D.setId(2);
        channel3D.setTopic("/drive/3D");
        channel3D.setEncoding("json");
        channel3D.setSchemaName("foxglove.SceneUpdate");
        schema = DataUtil.loadJsonSchema("SceneUpdate.json");
        channel3D.setSchema(schema);
        channel3D.setSchemaEncoding("jsonschema");

        ChannelInfo channelGPS = new ChannelInfo();
        channelGPS.setId(3);
        channelGPS.setTopic("/drive/map");
        channelGPS.setEncoding("json");
        channelGPS.setSchemaName("foxglove.LocationFix");
        schema = DataUtil.loadJsonSchema("LocationFix.json");
        channelGPS.setSchema(schema);
        channelGPS.setSchemaEncoding("jsonschema");


        List<ChannelInfo> channelList = new ArrayList<>();
        channelList.add(channelChassis);
        channelList.add(channelControl);
        channelList.add(channel3D);
        channelList.add(channelGPS);
        return channelList;
    }

    public static SendDataThread getTestSendThread(int id, int channelId, int frequency, Session session) {
        switch (channelId) {
            case 0:
                return new SendChassisThread(id, frequency, session);
            case 1:
                return new SendMessageThread(id, frequency, session);
            case 2:
                return new Send3DThread(id, frequency, session);
            case 3:
                return new SendGPSThread(id, frequency, session);
            case 4:
                String rtsp = "rtsp://127.0.0.1:8554/demo1";
                return new SendImageThread(id, frequency, session, rtsp);
            default:
                return null;
        }
    }

    public static SendDataThread getKafkaSendThread(int id, int channelId, int frequency, Session session) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String group = "test-group-" + timestamp;
        switch (channelId) {
            case 0:
                return new SendChassisThread(id, frequency, session);
            case 1:
                return new SendMessageKafkaThread(id, frequency, session, "DFICP_0X8001106S_HIVE_TOPIC", group);
            case 2:
                return new Send3DKafkaThread(id, frequency, session, "DFICP_0X800110AS_HIVE_TOPIC", group);
            case 3:
                return new SendGPSKafkaThread(id, frequency, session, "DFICP_0X8001102S_HIVE_TOPIC", group);
            case 4:
                String rtsp = "rtsp://127.0.0.1:8554/demo1";
                return new SendImageThread(id, frequency, session, rtsp);
            default:
                return null;
        }
    }

    private static ChannelInfo createControlData(int index) {
        ChannelInfo channelControl = new ChannelInfo();
        channelControl.setId(index);
        channelControl.setTopic("/drive/control_data");
        channelControl.setEncoding("json");
        channelControl.setSchemaName("控制数据信号");
        StringBuffer sb = new StringBuffer();
        sb.append("{\"type\": \"object\", \"properties\":{");
        sb.append("\"底盘号\": {\"type\": \"string\"},");
        sb.append("\"终端id\": {\"type\": \"string\"},");
        sb.append("\"时间\": {\"type\": \"string\"},");
        sb.append("\"纬度\": {\"type\": \"string\"},");
        sb.append("\"经度\": {\"type\": \"string\"},");
        sb.append("\"海拔\": {\"type\": \"string\"},");
        sb.append("\"SysstADMdmp\": {\"type\": \"string\"},");
        sb.append("\"位置跟踪误差\": {\"type\": \"string\"},");
        sb.append("\"期望车速m/s\": {\"type\": \"string\"},");
        sb.append("\"期望加速度m/s2\": {\"type\": \"string\"},");
        sb.append("\"行为ID\": {\"type\": \"string\"},");
        sb.append("\"坡度%\": {\"type\": \"string\"},");
        sb.append("\"整车总质量kg\": {\"type\": \"string\"},");
        sb.append("\"本车车速(km/h)\": {\"type\": \"string\"},");
        sb.append("\"本车加速度m/s2\": {\"type\": \"string\"},");
        sb.append("\"变速箱档位\": {\"type\": \"string\"},");
        sb.append("\"变速箱传动比\": {\"type\": \"string\"},");
        sb.append("\"离合器开关\": {\"type\": \"string\"},");
        sb.append("\"发动机摩擦扭矩百分比\": {\"type\": \"string\"},");
        sb.append("\"制动设备源地址\": {\"type\": \"string\"},");
        sb.append("\"发动机实际扭矩百分比\": {\"type\": \"string\"},");
        sb.append("\"驾驶员需求扭矩百分比\": {\"type\": \"string\"},");
        sb.append("\"发动机控制设备源地址\": {\"type\": \"string\"},");
        sb.append("\"左前轮制动压力\": {\"type\": \"string\"},");
        sb.append("\"发动机转速\": {\"type\": \"string\"},");
        sb.append("\"挂车状态\": {\"type\": \"string\"},");
        sb.append("\"传动系统结合状态\": {\"type\": \"string\"},");
        sb.append("\"变速箱选择的档位\": {\"type\": \"string\"},");
        sb.append("\"变速箱换挡状态\": {\"type\": \"string\"},");
        sb.append("\"制动踏板开度(百分比)\": {\"type\": \"string\"},");
        sb.append("\"发动机预估损失扭矩百分比\": {\"type\": \"string\"},");
        sb.append("\"变速箱TSC1限扭状态\": {\"type\": \"string\"},");
        sb.append("\"真实油门开度百分比\": {\"type\": \"string\"},");
        sb.append("\"巡航设定车速\": {\"type\": \"string\"},");
        sb.append("\"变速箱输出轴转速\": {\"type\": \"string\"},");
        sb.append("\"制动系统准备可以释放\": {\"type\": \"string\"},");
        sb.append("\"角速度(rad/s)\": {\"type\": \"string\"},");
        sb.append("\"AX(m/s^2)\": {\"type\": \"string\"},");
        sb.append("\"AY(m/s^2)\": {\"type\": \"string\"},");
        sb.append("\"总驱动力\": {\"type\": \"string\"}");
        sb.append("}}");
        channelControl.setSchema(sb.toString());
        channelControl.setSchemaEncoding("jsonschema");
        return channelControl;
    }

}
