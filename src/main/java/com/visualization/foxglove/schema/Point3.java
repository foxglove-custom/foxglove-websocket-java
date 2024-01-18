package com.visualization.foxglove.schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Point3 {

    private Float x;
    private Float y;
    private Float z;
}
