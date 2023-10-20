package com.jiaruiblog.foxglove.schema;

import lombok.Data;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SceneEntity
 * @Description TODO
 * @Author luojiarui
 * @Date 2023/9/27 20:30
 * @Version 1.0
 **/
@Data
public class SceneEntity {

    private Timestamp timestamp;

    private Timestamp lifetime;

    private String frame_id;

    private String id;

    private boolean frame_locked;

    private List<KeyValuePair> metadata = new ArrayList<>();

    private List<CubePrimitive> cubes = new ArrayList<>();

    private List<Object> arrows = new ArrayList<>();

    private List<Object> spheres = new ArrayList<>();

    private List<Object> cylinders = new ArrayList<>();

    private List<Line> lines = new ArrayList<>();

    private List<Object> triangles = new ArrayList<>();

    private List<Object> texts = new ArrayList<>();

    private List<Object> models = new ArrayList<>();

}
