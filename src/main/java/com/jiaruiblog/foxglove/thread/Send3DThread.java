package com.jiaruiblog.foxglove.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.schema.*;
import com.jiaruiblog.foxglove.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.yeauty.pojo.Session;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

@Slf4j
public class Send3DThread extends SendDataThread {

    private List<SceneUpdate> updateList;
    private List<ModelPrimitive> models;
    private List<String> oldIdList = new ArrayList<>();

    public Send3DThread(int index, int frequency, Session session) {
        super(index, frequency, session);
        updateList = this.readEntityData();
        models = this.addModels();
    }

    private List<SceneUpdate> readEntityData() {
        List<SceneUpdate> updateList = new ArrayList<>();
        String file = "E:\\foxglove\\obstacle_data.data";
        String str;
        String oldTs = null;
        List<SceneEntity> entityList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((str = br.readLine()) != null) {
                String[] data = str.split(",");

                String currentTs = data[0];
                if (!currentTs.equals(oldTs)) {
                    if (entityList.size() > 0) {
                        SceneUpdate sceneUpdate = new SceneUpdate();
                        sceneUpdate.setEntities(entityList);
                        updateList.add(sceneUpdate);
                    }
                    entityList = new ArrayList<>();
                    oldTs = currentTs;
                }

                SceneEntity entity = this.createEntity(data[1], "obstacle", "vehicle.truck", null);
                entity.setCubes(this.addCubes(data));

                entityList.add(entity);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("==============解析完毕，共有" + updateList.size() + "条点云记录======================");
        return updateList;
    }

    private SceneEntity createEntity(String id, String frameId, String metaDataValue, Timestamp ts) {
        if (ts == null) {
            Timestamp timestamp = new Timestamp();
            int nano = Instant.now().getNano();
            long second = Instant.now().getEpochSecond();
            timestamp.setSec((int) second);
            timestamp.setNsec(nano);
            ts = timestamp;
        }

        List<KeyValuePair> metadata = Arrays.asList(new KeyValuePair("category", metaDataValue));

        SceneEntity entity = new SceneEntity();
        entity.setTimestamp(ts);
        entity.setFrame_id(frameId);
        entity.setFrame_locked(true);
        entity.setId(id);
        entity.setMetadata(metadata);
        return entity;
    }

    @Override
    public void run() {
        int i = 0, size = updateList.size();
        while (running) {
            if (i >= size) {
                i = 0;
                log.info("==============播放完毕，新的轮回======================");
            }
            SceneUpdate sceneUpdate = updateList.get(i);
            List<SceneEntity> entities = sceneUpdate.getEntities();
            Timestamp timestamp = entities.get(0).getTimestamp();
            List<String> newIdList = entities.stream().map(SceneEntity::getId).collect(Collectors.toList());
            oldIdList.removeAll(newIdList);
            if (oldIdList.size() > 0) {
                List<SceneEntityDeletion> deleteList = oldIdList.stream().map(s -> {
                    SceneEntityDeletion del = new SceneEntityDeletion();
                    del.setId(s);
                    del.setType(0);
                    del.setTimestamp(timestamp);
                    return del;
                }).collect(Collectors.toList());
                sceneUpdate.setDeletions(deleteList);
            }

            // 减少车辆模型的显示频率
            if (i % 5 == 0) {
                SceneEntity carEntity = this.createEntity("drive_car", "obstacle", "vehicle.car", timestamp);
                carEntity.setModels(models);
                entities.add(carEntity);
            }

            oldIdList = newIdList;
            i++;
            printLog(100);
            JSONObject jsonObject = (JSONObject) JSON.toJSON(sceneUpdate);
            byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), index);
            this.session.sendBinary(bytes);
            try {
                Thread.sleep(frequency);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<ModelPrimitive> addModels() {
        ModelPrimitive model = new ModelPrimitive();
        Pose pose = new Pose();
        Vector3 position = new Vector3(-11.200000f, -8.499990f, 1.623f);
        Quaternion orientation = new Quaternion(0.0f, 0.0f, 0f, 0.1f);
        pose.setPosition(position);
        pose.setOrientation(orientation);

        Vector3 scale = new Vector3(1f, 1f, 1f);
        //Color color = new Color(0.6f, 0.2f, 1f, 0.8f);
        Color color = new Color(1f, 0.38823529411764707f, 0.2784313725490196f, 0.5f);
        byte[] bytes = DataUtil.loadGlbData("car.glb");

        model.setColor(color);
        model.setPose(pose);
        model.setData(bytes);
        model.setScale(scale);
        model.setMedia_type("model/gltf-binary");

        return Arrays.asList(model);
    }

    private List<CubePrimitive> addCubes(String[] data) {
        CubePrimitive cube = new CubePrimitive();
        Color color = this.setCubeColor(Integer.parseInt(data[7]));

        float length = Float.parseFloat(data[4]);
        float width = Float.parseFloat(data[5]);
        float height = length < width ? length : width;
        Vector3 size = new Vector3(length, width, height);

        Pose pose = new Pose();
        float x = Float.parseFloat(data[2]);
        float y = Float.parseFloat(data[3]);
        Vector3 position = new Vector3(x, y, 10f);
        float w = Float.parseFloat(data[6]);
        Quaternion orientation = new Quaternion(0f, 0f, 1f, w);
        pose.setPosition(position);
        pose.setOrientation(orientation);

        cube.setColor(color);
        cube.setSize(size);
        cube.setPose(pose);
        List<CubePrimitive> cubeList = Arrays.asList(cube);
        return cubeList;
    }

    private Color setCubeColor(int type) {
        switch (type) {
            case 0:
                return new Color(0.1843137254901961f, 0.30980392156862746f, 0.30980392156862746f, 0.5f);
            case 1:
                return new Color(0.1137254901960784f, 0.0784313725490196f, 0.1f, 0.58f);
            case 2:
                return new Color(0f, 0f, 0.9019607843137255f, 0.5f);
            default:
                return new Color(1f, 0.38823529411764707f, 0.2784313725490196f, 0.5f);
        }
    }
}
