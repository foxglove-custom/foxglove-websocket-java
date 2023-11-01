package com.jiaruiblog.foxglove.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jiaruiblog.foxglove.entity.VehicleInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationFix extends VehicleInfo {

    @JsonProperty("frame_id")
    private String frameId;

    private float latitude;
    private float longitude;
    private float altitude;
}
