package com.jiaruiblog.foxglove.schema;

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

    private Long sec;

    private Long nsec;

}
