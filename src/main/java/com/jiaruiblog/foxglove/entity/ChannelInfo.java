package com.jiaruiblog.foxglove.entity;

import lombok.Data;

/**
 * @ClassName ChannelInfo
 * @Description TODO
 * @Author luojiarui
 * @Date 2023/8/27 17:45
 * @Version 1.0
 **/
@Data
public class ChannelInfo {

    private Integer id;

    private String topic;

    private String encoding;

    private String schemaName;

    private String schema;

    private String schemaEncoding;

//
//    String str = "{\n" +
//            "  \"op\":\"advertise\",\n" +
//            "  \"channels\":[\n" +
//            "    {\n" +
//            "    \"id\":0,\n" +
//            "      \"topic\":\"example_msg\",\n" +
//            "      \"encoding\":\"json\",\n" +
//            "      \"schemaName\":\"ExampleMsg\",\n" +
//            "      \"schema\":\n" +
//            "      \"{\\\"type\\\": \\\"object\\\", \\\"properties\\\": {\\\"msg\\\": {\\\"type\\\": \\\"string\\\"}, \\\"count\\\": {\\\"type\\\": \\\"number\\\"}}}\",\n" +
//            "      \"schemaEncoding\":\"jsonschema\"\n" +
//            "    }]\n" +
//            "}";
}
