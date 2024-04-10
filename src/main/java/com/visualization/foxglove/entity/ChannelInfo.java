package com.visualization.foxglove.entity;

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

    /**
     * topic id，在注册时指定其值
     */
    private Integer id;

    /**
     * topic名称
     */
    private String topic;

    /**
     * topic编码，如json,protobuf等
     */
    private String encoding;

    /**
     * topic描述
     */
    private String schemaName;

    /**
     * topic对应的数据结构，可以是自定义的，也可以是foxglove官方定义好的
     */
    private String schema;

    /**
     * 对用结构的表示形式，如json时为jsonschema
     */
    private String schemaEncoding;
}
