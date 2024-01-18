package com.visualization.foxglove.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackedElement {

    private String name;

    private int offset;

    private int type;
}
