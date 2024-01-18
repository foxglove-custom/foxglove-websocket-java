package com.view.foxglove.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.view.foxglove.schema.Timestamp;
import lombok.Data;

@Data
public class VehicleInfo {

    @JsonProperty("底盘号")
    private String chassisCode;

    @JsonProperty("时间戳")
    private Timestamp timestamp;
}