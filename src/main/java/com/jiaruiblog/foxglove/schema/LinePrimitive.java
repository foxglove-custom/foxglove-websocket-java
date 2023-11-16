package com.jiaruiblog.foxglove.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinePrimitive {

    private Integer type;

    private Pose pose;

    private Float thickness;

    @JsonProperty("scale_invariant")
    private boolean scaleInvariant;

    private Color color;

    private List<Point3> points;
}
