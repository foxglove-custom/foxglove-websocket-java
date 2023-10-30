package com.jiaruiblog.foxglove.util;

import com.jiaruiblog.foxglove.entity.ChannelInfo;
import com.jiaruiblog.foxglove.message.MessageGenerator;
import com.jiaruiblog.foxglove.message.test.GPSGenerator;
import com.jiaruiblog.foxglove.message.test.RawMessageGenerator;
import com.jiaruiblog.foxglove.message.test.Scene3DGenerator;

import java.util.ArrayList;
import java.util.List;

public class ChannelUtil {

    public static List<ChannelInfo> createChannels() {
        String schema = null;

        ChannelInfo channelMessage = new ChannelInfo();
        channelMessage.setId(0);
        channelMessage.setTopic("/drive/raw_msg");
        channelMessage.setEncoding("json");
        channelMessage.setSchemaName("示例消息(只展示字符串)");
        channelMessage.setSchema("{\"type\": \"object\", \"properties\": {\"msg\": {\"type\": \"string\"}, \"count\": {\"type\": \"number\"}}}");
        channelMessage.setSchemaEncoding("jsonschema");

        ChannelInfo channel3D = new ChannelInfo();
        channel3D.setId(1);
        channel3D.setTopic("/drive/3D");
        channel3D.setEncoding("json");
        channel3D.setSchemaName("foxglove.SceneUpdate");
        schema = DataUtil.loadJsonSchema("SceneUpdate.json");
        channel3D.setSchema(schema);
        channel3D.setSchemaEncoding("jsonschema");


//        ChannelInfo channelImage = new ChannelInfo();
//        channelImage.setId(2);
//        channelImage.setTopic("/drive/image");
//        channelImage.setEncoding("json");
//        channelImage.setSchemaName("foxglove.CompressedImage");
//        schema = DataUtil.loadJsonSchema("CompressedImage.json");
//        channelImage.setSchema(schema);
//        channelImage.setSchemaEncoding("jsonschema");

        ChannelInfo channelGPS = new ChannelInfo();
        channelGPS.setId(2);
        channelGPS.setTopic("/drive/gps");
        channelGPS.setEncoding("json");
        channelGPS.setSchemaName("foxglove.LocationFix");
        schema = DataUtil.loadJsonSchema("LocationFix.json");
        channelGPS.setSchema(schema);
        channelGPS.setSchemaEncoding("jsonschema");


        List<ChannelInfo> channelList = new ArrayList<>();
        channelList.add(channelMessage);
//        channelList.add(channelImage);
        channelList.add(channel3D);
        channelList.add(channelGPS);
        return channelList;
    }

    public static MessageGenerator getGenerator(int channelId) {
        switch (channelId) {
            case 0:
                return new RawMessageGenerator();
            case 1:
                return new Scene3DGenerator();
            case 2:
                return new GPSGenerator();
            default:
                return null;
        }
    }
}
