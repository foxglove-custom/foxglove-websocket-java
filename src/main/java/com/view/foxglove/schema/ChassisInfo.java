package com.view.foxglove.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.view.foxglove.entity.VehicleInfo;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class ChassisInfo extends VehicleInfo {

    @JsonProperty("rtsp_url")
    private String rtspUrl;
}
