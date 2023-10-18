package com.jiaruiblog.foxglove.util;

import com.jiaruiblog.foxglove.entity.ChannelInfo;

import java.util.Arrays;
import java.util.List;

public class ChannelUtil {

    public static List<ChannelInfo> createChannels() {
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
        List<ChannelInfo> channelList = Arrays.asList(channelDemo, channelImage, channelScene, channelFrame);
        return channelList;
    }
}
