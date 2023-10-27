package com.jiaruiblog.foxglove.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationFix {

    private Timestamp timestamp;

    private String frame_id;

    private float latitude;
    private float longitude;
    private float altitude;

}
