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

    private Integer id;

    private String topic;

    private String encoding;

    private String schemaName;

    private String schema;

    private String schemaEncoding;

}
