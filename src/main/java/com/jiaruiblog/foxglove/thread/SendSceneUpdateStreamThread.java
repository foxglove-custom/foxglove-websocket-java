package com.jiaruiblog.foxglove.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.schema.*;
import org.yeauty.pojo.Session;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

public class SendSceneUpdateStreamThread implements Runnable {

    private int frequency;
    private int index;
    private Session session;
    private List<SceneUpdate> updateList;

    public SendSceneUpdateStreamThread(int index, int frequency, Session session) {
        this.index = index;
        this.session = session;
        this.frequency = frequency;
        updateList = readEntityData();
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

                Timestamp timestamp = new Timestamp();
                int nano = Instant.now().getNano();
                long second = Instant.now().getEpochSecond();
                timestamp.setSec((int) second);
                timestamp.setNsec(nano);

                SceneEntity entity = new SceneEntity();
                entity.setTimestamp(timestamp);
                entity.setFrame_id("obstacle");
                entity.setFrame_locked(true);
                entity.setId(data[1]);

                List<KeyValuePair> metadata = Arrays.asList(new KeyValuePair("category", "vehicle.truck"));
                entity.setMetadata(metadata);
                entity.setCubes(this.addCubes(data));

                entityList.add(entity);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("==============解析完毕，共有" + updateList.size() + "条记录======================");
        return updateList;
    }

    @Override
    public void run() {
        int i = 0, size = updateList.size();
        while (true) {
            if (i >= size) {
                i = 0;
                System.out.println("==============播放完毕，新的轮回======================");
            }
            SceneUpdate sceneUpdate = updateList.get(i);
            i++;
            System.out.println("-----------读取第" + i + "个元素------------");
            JSONObject jsonObject = (JSONObject) JSON.toJSON(sceneUpdate);

            Integer ns = sceneUpdate.getEntities().get(0).getTimestamp().getNsec();
            byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), ns, index);
            this.session.sendBinary(bytes);
            try {
                Thread.sleep(frequency);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<CubePrimitive> addCubes(String[] data) {
        CubePrimitive cube = new CubePrimitive();
        Color color = new Color(1f, 0.38823529411764707f, 0.2784313725490196f, 0.5f);

        float length = Float.parseFloat(data[4]);
        float width = Float.parseFloat(data[5]);
        float height = length < width ? length : width;
        Vector3 size = new Vector3(length, width, height);

        Pose pose = new Pose();
        float x = Float.parseFloat(data[2]);
        float y = Float.parseFloat(data[3]);
        Vector3 position = new Vector3(x, y, 10f);
        float w = Float.parseFloat(data[6]);
        Quaternion orientation = new Quaternion(0f, 0f, 0.812556848660791f, w);
        pose.setPosition(position);
        pose.setOrientation(orientation);

        cube.setColor(color);
        cube.setSize(size);
        cube.setPose(pose);
        List<CubePrimitive> cubeList = Arrays.asList(cube);
        return cubeList;
    }
}
