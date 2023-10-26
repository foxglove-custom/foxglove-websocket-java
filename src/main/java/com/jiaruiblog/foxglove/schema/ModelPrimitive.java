package com.jiaruiblog.foxglove.schema;

import lombok.Data;

@Data
public class ModelPrimitive {

    private Pose pose;

    private Vector3 scale;

    private Color color;

    private boolean override_color;

    private String url;

    private String media_type;

    private byte[] data;
}
