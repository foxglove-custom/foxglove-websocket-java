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

    private Timestamp timestamp;

    private String parent_frame_id;

    private String child_frame_id;

    private Vector3 translation;

    private Quaternion rotation;
}
