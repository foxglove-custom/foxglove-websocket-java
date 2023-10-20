package com.jiaruiblog.foxglove.schema;

import lombok.Data;

/**
 * @ClassName FrameTransform
 * @Description TODO
 * @Author luojiarui
 * @Date 2023/9/28 18:11
 * @Version 1.0
 **/
@Data
public class FrameTransform {


    String timestamp;

    String parentFrameId;

    String childFrameId;

    Vector3 translation;


}
