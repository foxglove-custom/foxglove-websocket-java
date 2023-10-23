package com.jiaruiblog.foxglove.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.schema.FrameTransform;
import com.jiaruiblog.foxglove.schema.Quaternion;
import com.jiaruiblog.foxglove.schema.Timestamp;
import com.jiaruiblog.foxglove.schema.Vector3;
import org.yeauty.pojo.Session;

import java.time.Instant;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

public class SendTransformThread implements Runnable {

    private int frequency;
    private int index;
    private Session session;

    public SendTransformThread(int index, int frequency, Session session) {
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
            Timestamp timestamp = new Timestamp();
            int nano = Instant.now().getNano();
            long second = Instant.now().getEpochSecond();
            timestamp.setSec((int) second);
            timestamp.setNsec(nano);

            FrameTransform transform = new FrameTransform();
            transform.setTimestamp(timestamp);
            transform.setChild_frame_id("obstacle");
            transform.setParent_frame_id("root");

            Quaternion rotation = new Quaternion(100f + i, 200f + i, 100f + i, 100f + i / 3);
            transform.setRotation(rotation);

            Vector3 translation = new Vector3(100f, 200f + i, 100f + i);
            transform.setTranslation(translation);

            JSONObject jsonObject = (JSONObject) JSON.toJSON(transform);

            byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), nano, index);
            this.session.sendBinary(bytes);
            try {
                Thread.sleep(frequency);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
