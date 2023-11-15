package com.jiaruiblog.foxglove.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Timestamp
 * @Description TODO
 * @Author luojiarui
 * @Date 2023/8/31 23:01
 * @Version 1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Timestamp {

    @JsonProperty("秒")
    private Long sec;

    @JsonProperty("纳秒")
    private Long nsec;

}
