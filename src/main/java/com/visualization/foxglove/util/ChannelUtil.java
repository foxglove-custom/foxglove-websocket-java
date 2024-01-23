package com.visualization.foxglove.util;

import com.visualization.foxglove.entity.ChannelInfo;
import com.visualization.foxglove.thread.SendChassisThread;
import com.visualization.foxglove.thread.SendDataThread;
import com.visualization.foxglove.thread.SendImageThread;
import com.visualization.foxglove.thread.kafka.Send3DKafkaThread;
import com.visualization.foxglove.thread.kafka.SendMapKafkaThread;
import com.visualization.foxglove.thread.kafka.SendTextKafkaThread;
import org.yeauty.pojo.Session;

import java.util.ArrayList;
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

    public static SendDataThread getKafkaSendThread(int id, int channelId, Session session) {
        switch (channelId) {
            case 0:
                return new SendChassisThread(id, session);
            case 1:
                return new SendTextKafkaThread(id, session);
            case 2:
                return new Send3DKafkaThread(id, session);
            case 3:
                return new SendMapKafkaThread(id, session);
            case 4:
                return new SendImageThread(id, session);
            default:
                return null;
        }
    }

    public static String getChannelName(int channelId) {
        switch (channelId) {
            case 0:
                return "chassis";
            case 1:
                return "text";
            case 2:
                return "3d";
            case 3:
                return "map";
            case 4:
                return "image";
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
