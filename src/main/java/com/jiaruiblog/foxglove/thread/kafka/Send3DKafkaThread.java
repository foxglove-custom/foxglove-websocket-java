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

import static com.jiaruiblog.foxglove.util.DataUtil.getFormattedBytes;

@Slf4j
public class Send3DKafkaThread extends SendDataThread {

    private List<LinePrimitive> lineList = new ArrayList<>();

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

        long gpsTime = 0L;
        Timestamp timestamp;

        List<CubePrimitive> cubeList = new ArrayList<>();

        new Thread(() -> {
            try {
                this.createLinePrimitive();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

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
                            SceneEntity entity = DFSceneUtil.addSceneEntity("entity", "obstacle", "vehicle.truck", data, timestamp);
                            entity.setCubes(cubeList);
                            entity.setLines(lineList);
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

    private void createLinePrimitive() throws InterruptedException {
        String topic = "DFICP_0X800110CS_HIVE_POINT_PLANNING_RESULT_TOPIC";
        List<Point3> pointList = new ArrayList<>();
        Properties props = KafkaUtil.getConsumerProperties(group + "_1", StringDeserializer.class.getName());
        //props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(topic));
            long gpsTime = 0;
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    String[] data = record.value().split("\\001");
                    String chassisCode = data[0];
                    if (!this.chassisCode.equals(chassisCode)) {
                        continue;
                    }
                    long ts = Long.parseLong(data[2]);
                    if (ts != gpsTime && gpsTime > 0) {
                        Color color = new Color(0f, 0f, 0.9019607843137255f, 0.5f);

                        LinePrimitive line = new LinePrimitive();
                        line.setType(0);
                        line.setColor(color);
                        line.setThickness(0.5f);
                        line.setPoints(pointList);

                        lineList.clear();
                        lineList.add(line);
                        log.info("-----------------------------" + lineList.size() + "\t" + pointList.size());
                        pointList.clear();
                        Thread.sleep(frequency);
                    }
                    gpsTime = ts;
                    Point3 point3 = Point3.builder().x(Float.parseFloat(data[9])).y(Float.parseFloat(data[10])).z(0f).build();
                    pointList.add(point3);
                }
            }
        }
    }
}
