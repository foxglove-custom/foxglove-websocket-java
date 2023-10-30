package com.jiaruiblog.foxglove.thread.bak;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.schema.*;
import com.jiaruiblog.foxglove.util.DataUtil;
import org.yeauty.pojo.Session;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

public class Send3DSimulateThread implements Runnable {

    private int frequency;
    private int index;
    private Session session;

    public Send3DSimulateThread(int index, int frequency, Session session) {
        this.index = index;
        this.session = session;
        this.frequency = frequency;
    }

    @Override
    public void run() {
        int i = 0;
        while (true) {
            i = i > 10 ? 0 : i;
            i++;
            SceneEntity entity = new SceneEntity();
            Timestamp timestamp = new Timestamp();
            int nano = Instant.now().getNano();
            long second = Instant.now().getEpochSecond();
            timestamp.setSec((int) second);
            timestamp.setNsec(nano);
            entity.setTimestamp(timestamp);
            entity.setFrame_id("obstacle");
            entity.setId("6dd2"); // id号必须保持相同，才能替换旧的
            entity.setFrame_locked(true);

            List<KeyValuePair> metadata = Arrays.asList(new KeyValuePair("category", "vehicle.truck"));
            entity.setMetadata(metadata);

            // 添加立方体
            entity.setCubes(this.addCubes(i));
            entity.setModels(this.addModels(i));

            SceneUpdate sceneUpdate = new SceneUpdate();
            sceneUpdate.setEntities(Arrays.asList(entity));
            JSONObject jsonObject = (JSONObject) JSON.toJSON(sceneUpdate);

            byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), nano, index);
            this.session.sendBinary(bytes);
            try {
                Thread.sleep(frequency);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<ModelPrimitive> addModels(int i) {

        ModelPrimitive model = new ModelPrimitive();
        Pose pose = new Pose();
        Vector3 position = new Vector3(0f, 0f, -340.623f);
        Quaternion orientation = new Quaternion(0.0f, 0.0f, 1f, 0.7f + i / 100);
        pose.setPosition(position);
        pose.setOrientation(orientation);

        Vector3 scale = new Vector3(10f, 10f, 10f);
        Color color = new Color(0.6f, 0.2f, 1f, 0.8f);
        byte[] bytes = DataUtil.loadGlbData("car.glb");

        model.setColor(color);
        model.setPose(pose);
        model.setData(bytes);
        model.setScale(scale);
        model.setMedia_type("model/gltf-binary");

        return Arrays.asList(model);
    }

    public List<CubePrimitive> addCubes(int i) {
        CubePrimitive cube = new CubePrimitive();
        Color color = new Color(1f, 0.38823529411764707f, 0.2784313725490196f, 0.5f);
        Vector3 size = new Vector3(10.201f, 20.877f, 10.55f);

        Pose pose = new Pose();
        Vector3 position = new Vector3(0f + i * 10, 0f + i * 20, -300.623f);
        Quaternion orientation = new Quaternion(0f, 0f, 0.812556848660791f, -0.5828819500503033f + i / 200);
        pose.setPosition(position);
        pose.setOrientation(orientation);

        cube.setColor(color);
        cube.setSize(size);
        cube.setPose(pose);
        List<CubePrimitive> cubeList = Arrays.asList(cube);
        return cubeList;
    }
}
