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
public class CompressedImage {

    private Timestamp timestamp;

    private String frameId;

    private String format;

    private String data;

    @Data
    public class Timestamp {
        Integer sec;

        Integer nsec;
    }

}
