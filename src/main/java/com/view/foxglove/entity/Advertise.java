package com.view.foxglove.entity;

import lombok.Data;

import java.util.List;

/**
 * @ClassName Advertise
 * @Description TODO
 * @Author luojiarui
 * @Date 2023/8/27 17:45
 * @Version 1.0
 **/
@Data
public class Advertise {

    private String op;

    private List<ChannelInfo> channels;
}
