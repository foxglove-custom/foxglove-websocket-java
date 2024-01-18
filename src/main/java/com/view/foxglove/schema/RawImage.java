package com.view.foxglove.schema;

import lombok.Data;

/**
 * @ClassName RawImage
 * @Description TODO
 * @Author luojiarui
 * @Date 2023/8/31 22:56
 * @Version 1.0
 **/
@Data
public class RawImage {

    private Timestamp timestamp;

    private String frame_id;

    private Integer width;

    private Integer height;

    private String encoding;

    private Integer step;

    private String data;

}
