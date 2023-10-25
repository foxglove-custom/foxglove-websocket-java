package com.jiaruiblog.foxglove.util;

import com.jiaruiblog.foxglove.entity.ChannelInfo;

import java.util.ArrayList;
import java.util.List;

public class ChannelUtil {

    public static List<ChannelInfo> createChannels() {
        String schema = null;

//        ChannelInfo channelDemo = new ChannelInfo();
//        channelDemo.setId(1);
//        channelDemo.setTopic("示例消息");
//        channelDemo.setEncoding("json");
//        channelDemo.setSchemaName("示例消息(只展示字符串)");
//        channelDemo.setSchema("{\"type\": \"object\", \"properties\": {\"msg\": {\"type\": \"string\"}, \"count\": {\"type\": \"number\"}}}");
//        channelDemo.setSchemaEncoding("jsonschema");

        ChannelInfo channelScene = new ChannelInfo();
        channelScene.setId(1);
        channelScene.setTopic("SceneUpdate");
        channelScene.setEncoding("json");
        channelScene.setSchemaName("foxglove.SceneUpdate");
        schema = DataUtil.loadJsonSchema("SceneUpdate.json");
        channelScene.setSchema(schema);
        channelScene.setSchemaEncoding("jsonschema");

//        ChannelInfo channelFrame = new ChannelInfo();
//        channelFrame.setId(3);
//        channelFrame.setTopic("FrameTransforms");
//        channelFrame.setEncoding("json");
//        channelFrame.setSchemaName("FrameTransforms");
//        schema = DataUtil.loadJsonSchema("FrameTransforms.json");
//        channelFrame.setSchema(schema);
//        channelFrame.setSchemaEncoding("jsonschema");
//
//        ChannelInfo channelCompressImage = new ChannelInfo();
//        channelCompressImage.setId(2);
//        channelCompressImage.setTopic("CompressImage");
//        channelCompressImage.setEncoding("json");
//        channelCompressImage.setSchemaName("foxglove.CompressedImage");
//        schema = DataUtil.loadJsonSchema("CompressedImage.json");
//        channelCompressImage.setSchema(schema);
//        channelCompressImage.setSchemaEncoding("jsonschema");

//        ChannelInfo channelPointCloud = new ChannelInfo();
//        channelPointCloud.setId(2);
//        channelPointCloud.setTopic("PointCloud");
//        channelPointCloud.setEncoding("json");
//        channelPointCloud.setSchemaName("foxglove.PointCloud");
//        schema = DataUtil.loadJsonSchema("PointCloud.json");
//        channelPointCloud.setSchema(schema);
//        channelPointCloud.setSchemaEncoding("jsonschema");

//        ChannelInfo channelTransform = new ChannelInfo();
//        channelTransform.setId(3);
//        channelTransform.setTopic("frameTransform");
//        channelTransform.setEncoding("json");
//        channelTransform.setSchemaName("foxglove.FrameTransform");
//        schema = DataUtil.loadJsonSchema("FrameTransform.json");
//        channelTransform.setSchema(schema);
//        channelTransform.setSchemaEncoding("jsonschema");

//        ChannelInfo channelImage = new ChannelInfo();
//        channelImage.setId(1);
//        channelImage.setTopic("rawImage");
//        channelImage.setEncoding("json");
//        channelImage.setSchemaName("foxglove.RawImage");
//        schema = DataUtil.loadJsonSchema("RawImage.json");
//        channelImage.setSchema(schema);
//        channelImage.setSchemaEncoding("jsonschema");


        List<ChannelInfo> channelList = new ArrayList<>();
        channelList.add(channelScene);
        return channelList;
    }
}
