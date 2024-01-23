package com.visualization.foxglove.thread.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.visualization.foxglove.config.AppCtxUtil;
import com.visualization.foxglove.config.KafkaConfig;
import com.visualization.foxglove.schema.CubePrimitive;
import com.visualization.foxglove.schema.SceneEntity;
import com.visualization.foxglove.schema.SceneUpdate;
import com.visualization.foxglove.schema.Timestamp;
import com.visualization.foxglove.thread.SendDataThread;
import com.visualization.foxglove.util.DFSceneUtil;
import com.visualization.foxglove.util.DateUtil;
import com.visualization.foxglove.util.KafkaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.yeauty.pojo.Session;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.visualization.foxglove.util.DataUtil.getFormattedBytes;

@Slf4j
public class Send3DKafkaThread extends SendDataThread {

    private KafkaConfig kafkaConfig;

    private String topic;

    public Send3DKafkaThread(int index, int frequency, Session session, String topic) {
        super(index, frequency, session);
        this.topic = topic;
        this.kafkaConfig = AppCtxUtil.getBean(KafkaConfig.class);
    }

    @Override
    public void run() {
        Properties props = KafkaUtil.getConsumerProperties(kafkaConfig);

        long gpsTime = 0L;
        Timestamp timestamp;

        List<CubePrimitive> cubeList = new ArrayList<>();

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(topic));
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    String[] data = record.value().split("\\001");
                    String chassisCode = data[0];
                    if (!this.chassisCode.equals(chassisCode)) {
                        continue;
                    }
                    long ts = Long.parseLong(data[2]);
                    if (ts != gpsTime) {
                        gpsTime = ts;
                        timestamp = DateUtil.createTimestamp(gpsTime);
                        if (gpsTime != 0) {
                            SceneEntity entity = DFSceneUtil.createEntity("entity", "obstacle", "vehicle.truck", timestamp);
                            entity.setCubes(cubeList);
                            SceneUpdate sceneUpdate = new SceneUpdate();
                            sceneUpdate.setEntities(Arrays.asList(entity));

                            printLog();
                            JSONObject jsonObject = (JSONObject) JSON.toJSON(sceneUpdate);
                            byte[] bytes = getFormattedBytes(jsonObject.toJSONString().getBytes(), index);
                            this.session.sendBinary(bytes);
                            cubeList.clear();

                            Thread.sleep(frequency);
                        }
                    } else {
                        CubePrimitive cube = DFSceneUtil.createCube(data);
                        cubeList.add(cube);
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("--------------------Kafka线程已经停止: " + Thread.currentThread().getName());
    }
}
