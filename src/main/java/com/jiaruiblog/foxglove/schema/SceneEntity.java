package com.jiaruiblog.foxglove.schema;

import lombok.Data;

import javax.sound.sampled.Line;
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


    SceneTimestamp timestamp;

    String frameId;

    String id;

    SceneLifeTime lifeTime;

    Boolean frameLocked;

    List<Metadata> metadata;

    List<Arrow> arrows;

    List<Cube> cubes;

    List<Sphere> spheres;

    List<Cylinder> cylinders;

    List<Line> lines;

    List<Triangle> triangles;

    List<Text> texts;

    List<Model> models;

    @Data
    public class SceneTimestamp {
        Integer sec;

        Integer nsec;
    }

    @Data
    public class SceneLifeTime {
        Integer sec;

        Integer nsec;
    }

    @Data
    public class Metadata {
        String key;
        String value;
    }

    public class Arrow {

    }

    public class Cube {

    }

    class Sphere{

    }

    class Cylinder {

    }

    class Triangle {

    }

    class Text {

    }

    class Model {

    }

}
