package com.view.foxglove.schema;

import com.view.foxglove.entity.VehicleInfo;
import lombok.Data;

import java.util.List;

/**
 * @ClassName SceneUpdate
 * @Description TODO
 * @Author luojiarui
 * @Date 2023/9/28 17:18
 * @Version 1.0
 **/

@Data
public class SceneUpdate extends VehicleInfo {

    private List<SceneEntityDeletion> deletions;

    private List<SceneEntity> entities;

}
