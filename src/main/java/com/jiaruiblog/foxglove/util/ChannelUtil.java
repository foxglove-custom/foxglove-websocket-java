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
        String schema = null;

        ChannelInfo channelScene = new ChannelInfo();
        channelScene.setId(2);
        channelScene.setTopic("SceneUpdate");
        channelScene.setEncoding("json");
        channelScene.setSchemaName("foxglove.SceneUpdate");
        schema = DataUtil.loadJsonSchema("SceneUpdate.json");
        channelScene.setSchema(schema);
        channelScene.setSchemaEncoding("jsonschema");

        ChannelInfo channelFrame = new ChannelInfo();
        channelFrame.setId(3);
        channelFrame.setTopic("FrameTransforms");
        channelFrame.setEncoding("json");
        channelFrame.setSchemaName("FrameTransforms");
        schema = DataUtil.loadJsonSchema("FrameTransforms.json");
        channelFrame.setSchema(schema);
        channelFrame.setSchemaEncoding("jsonschema");

        ChannelInfo channelCompressImage = new ChannelInfo();
        channelCompressImage.setId(4);
        channelCompressImage.setTopic("CompressImage");
        channelCompressImage.setEncoding("json");
        channelCompressImage.setSchemaName("foxglove.CompressedImage");
        schema = DataUtil.loadJsonSchema("CompressedImage.json");
        channelCompressImage.setSchema(schema);
        channelCompressImage.setSchemaEncoding("jsonschema");

        ChannelInfo channelPointCloud = new ChannelInfo();
        channelPointCloud.setId(5);
        channelPointCloud.setTopic("PointCloud");
        channelPointCloud.setEncoding("json");
        channelPointCloud.setSchemaName("foxglove.PointCloud");
        schema = DataUtil.loadJsonSchema("PointCloud.json");
        channelPointCloud.setSchema(schema);
        channelPointCloud.setSchemaEncoding("jsonschema");


        List<ChannelInfo> channelList = Arrays.asList(channelScene);
        //List<ChannelInfo> channelList = Arrays.asList(channelDemo, channelScene, channelFrame, channelCompressImage);
        return channelList;
    }
}
