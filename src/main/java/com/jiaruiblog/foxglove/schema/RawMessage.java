package com.jiaruiblog.foxglove.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawMessage {

    private int count;

    private int number;

    private String msg;
}
