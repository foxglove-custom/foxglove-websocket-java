package com.jiaruiblog.foxglove.entity;

import lombok.Data;

import java.util.List;

/**
 * @ClassName ServerInfo
 * @Description TODO
 * @Author luojiarui
 * @Date 2023/8/27 17:43
 * @Version 1.0
 **/
@Data
public class ServerInfo {

    private String op;

    private String name;

    private List<String> capabilities;

    private List<String> supportedEncodings;

    private String metadata;

    private Integer sessionId;
}
