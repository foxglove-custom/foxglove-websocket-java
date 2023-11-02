package com.jiaruiblog.foxglove.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jiaruiblog.foxglove.schema.Timestamp;
import lombok.Data;

@Data
public class VehicleInfo {

    @JsonProperty("chassis_code")
    private String chassisCode;

    private Timestamp timestamp;
}
