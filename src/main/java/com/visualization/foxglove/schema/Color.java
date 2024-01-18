package com.visualization.foxglove.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Color {

    private Float r;
    private Float g;
    private Float b;
    private Float a;
}
