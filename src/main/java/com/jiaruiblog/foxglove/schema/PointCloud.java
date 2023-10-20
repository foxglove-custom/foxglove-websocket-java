package com.jiaruiblog.foxglove.schema;

import lombok.Data;

@Data
public class PointCloud {

    public Timestamp timestamp;

    private String frame_id;

    private Integer point_stride;

    private String data;

    private String pose;

    private PackedElement[] fields;
}
