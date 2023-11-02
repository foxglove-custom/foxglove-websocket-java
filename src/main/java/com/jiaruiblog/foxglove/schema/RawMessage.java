package com.jiaruiblog.foxglove.schema;

import com.jiaruiblog.foxglove.entity.VehicleInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RawMessage extends VehicleInfo {

    private int count;

    private int number;

    private String msg;
}
