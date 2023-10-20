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

//        ChannelInfo channelImage = new ChannelInfo();
//        channelImage.setId(2);
//        channelImage.setTopic("rawImage");
//        channelImage.setEncoding("json");
//        channelImage.setSchemaName("foxglove.RawImage");
//        String schema = DataUtil.loadJsonSchema("RawImage.json");
//        channelImage.setSchema(schema);
//        channelImage.setSchemaEncoding("jsonschema");

        ChannelInfo channelScene = new ChannelInfo();
        channelScene.setId(3);
        channelScene.setTopic("sceneEntity");
        channelScene.setEncoding("json");
        channelScene.setSchemaName("SceneEntity");
        schema = DataUtil.loadJsonSchema("SceneUpdate.json");
        channelScene.setSchema(schema);
        channelScene.setSchemaEncoding("jsonschema");

        ChannelInfo channelFrame = new ChannelInfo();
        channelFrame.setId(4);
        channelFrame.setTopic("FrameTransforms");
        channelFrame.setEncoding("json");
        channelFrame.setSchemaName("FrameTransforms");
        schema = DataUtil.loadJsonSchema("FrameTransforms.json");
        channelFrame.setSchema(schema);
        channelFrame.setSchemaEncoding("jsonschema");

        ChannelInfo channelCompressImage = new ChannelInfo();
        channelCompressImage.setId(5);
        channelCompressImage.setTopic("CompressImage");
        channelCompressImage.setEncoding("json");
        channelCompressImage.setSchemaName("foxglove.CompressedImage");
        schema = DataUtil.loadJsonSchema("CompressedImage.json");
        channelCompressImage.setSchema(schema);
        channelCompressImage.setSchemaEncoding("jsonschema");

        ChannelInfo channelPointCloud = new ChannelInfo();
        channelPointCloud.setId(6);
        channelPointCloud.setTopic("PointCloud");
        channelPointCloud.setEncoding("json");
        channelPointCloud.setSchemaName("foxglove.PointCloud");
        schema = DataUtil.loadJsonSchema("PointCloud.json");
        channelPointCloud.setSchema(schema);
        channelPointCloud.setSchemaEncoding("jsonschema");


        List<ChannelInfo> channelList = Arrays.asList(channelPointCloud);
        //List<ChannelInfo> channelList = Arrays.asList(channelDemo, channelScene, channelFrame, channelCompressImage);
        return channelList;
    }
}
