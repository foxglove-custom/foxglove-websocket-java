package com.jiaruiblog.foxglove.schema;

import lombok.Data;

import java.util.Base64;

/**
 * @ClassName RawImage
 * @Description TODO
 * @Author luojiarui
 * @Date 2023/8/31 22:56
 * @Version 1.0
 **/
@Data
public class RawImage {

    private String timestamp;

    private String frameId;

    private Integer width;

    private Integer height;

    private String encoding;

    private Integer step;

    private Base64 data;

}
