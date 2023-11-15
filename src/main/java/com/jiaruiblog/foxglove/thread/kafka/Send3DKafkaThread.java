package com.jiaruiblog.foxglove.thread.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.schema.*;
import com.jiaruiblog.foxglove.thread.SendDataThread;
import com.jiaruiblog.foxglove.util.DFSceneUtil;
import com.jiaruiblog.foxglove.util.DateUtil;
import com.jiaruiblog.foxglove.util.KafkaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.yeauty.pojo.Session;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormattedBytes;

@Slf4j
public class Send3DKafkaThread extends SendDataThread {

    private String topic;
    private String group;

    public Send3DKafkaThread(int index, int frequency, Session session, String topic, String group) {
        super(index, frequency, session);
        this.topic = topic;
        this.group = group;
    }

    @Override
    public void run() {
        Properties props = KafkaUtil.getConsumerProperties(group, StringDeserializer.class.getName());
        List<ModelPrimitive> models = DFSceneUtil.addModels();

        long gpsTime = 0L;
        List<SceneEntity> entityList = new ArrayList<>();
        List<String> obsCurIdList = new ArrayList<>();
        List<String> obsOldList = new ArrayList<>();
        Timestamp timestamp = null;

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
                        if (entityList.size() > 0) {
                            obsOldList.removeAll(obsCurIdList);
                            List<SceneEntityDeletion> deletionList = this.createDeleteEntityList(timestamp, obsOldList);

                            SceneUpdate sceneUpdate = new SceneUpdate();
                            sceneUpdate.setEntities(entityList);
                            sceneUpdate.setDeletions(deletionList);

                            printLog();
                            JSONObject jsonObject = (JSONObject) JSON.toJSON(sceneUpdate);
                            byte[] bytes = getFormattedBytes(jsonObject.toJSONString().getBytes(), index);
                            this.session.sendBinary(bytes);
                            log.info("------------------------" + ts + "发送数据");

                            entityList.clear();
                            obsOldList.clear();
                            obsOldList.addAll(obsCurIdList);
                            obsCurIdList.clear();

                            Thread.sleep(100);
                        }
                    } else {
                        String obstacleId = "obs_" + data[7];
                        obsCurIdList.add(obstacleId);
                        SceneEntity entity = DFSceneUtil.addSceneEntity("obstacle", "vehicle.truck", data, timestamp);
                        entityList.add(entity);
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("--------------------Kafka线程已经停止: " + Thread.currentThread().getName());
    }

    private List<SceneEntityDeletion> createDeleteEntityList(Timestamp timestamp, List<String> oldIdList) {
        List<SceneEntityDeletion> deleteList = oldIdList.stream().map(s -> {
            SceneEntityDeletion del = new SceneEntityDeletion();
            del.setId(s);
            del.setType(0);
            del.setTimestamp(timestamp);
            return del;
        }).collect(Collectors.toList());
        return deleteList;
    }
}
