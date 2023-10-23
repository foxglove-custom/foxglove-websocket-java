package com.jiaruiblog.foxglove.schema;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SceneUpdate
 * @Description TODO
 * @Author luojiarui
 * @Date 2023/9/28 17:18
 * @Version 1.0
 **/

@Data
public class SceneUpdate {

    private List<Object> deletions = new ArrayList<>();

    private List<SceneEntity> entities = new ArrayList<>();

}
