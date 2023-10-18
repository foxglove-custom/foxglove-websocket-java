package com.jiaruiblog.foxglove.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.schema.SceneEntity;
import com.jiaruiblog.foxglove.schema.SceneUpdate;
import org.yeauty.pojo.Session;

import java.util.Arrays;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

public class SendSceneThread implements Runnable {

    private int index;
    private Session session;

    public SendSceneThread(int index, Session session) {
        this.index = index;
        this.session = session;
    }

    @Override
    public void run() {
        int i = 0;
        while (true) {

            SceneEntity sceneEntity = new SceneEntity();
            SceneEntity.SceneTimestamp sceneTimestamp = sceneEntity.new SceneTimestamp();
            sceneTimestamp.setSec(i * i * i % 20);
            sceneTimestamp.setNsec(i++ % 20);
            sceneEntity.setTimestamp(sceneTimestamp);

            SceneUpdate sceneUpdate = new SceneUpdate();
            sceneUpdate.setEntities(Arrays.asList(sceneEntity));
            JSONObject jsonObject = (JSONObject) JSON.toJSON(sceneUpdate);

            Long ns = 1692891094326598000L;
            byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), ns, index);
            this.session.sendBinary(bytes);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
