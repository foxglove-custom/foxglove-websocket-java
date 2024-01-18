package com.visualization.foxglove.entity;

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

    /**
     * for displaying or debugging
     **/
    private String name;

    /**
     * arrays of strings, optional features
     **/
    private List<String> capabilities;

    /**
     * information client which encoding supported
     **/
    private List<String> supportedEncodings;

    /**
     * map of key-value pairs
     * TODO map
     **/
    private String metadata;

    /**
     * timestamp or UUID
     **/
    private Integer sessionId;
}
