package com.jiaruiblog.foxglove.schema;

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

    private String frameId;

    private Integer width;

    private Integer height;

    private String encoding;

    private Integer step;

    private String data;

    @Data
    public class Timestamp {
        Integer sec;

        Integer nsec;
    }

}
