package com.jiaruiblog.foxglove.thread.kafka;

import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.kafka.deserializer.SceneUpdateDeserializer;
import com.jiaruiblog.foxglove.schema.SceneUpdate;
import com.jiaruiblog.foxglove.thread.SendDataThread;
import com.jiaruiblog.foxglove.util.KafkaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.yeauty.pojo.Session;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

@Slf4j
public class Send3DKafkaThread extends SendDataThread {

    public Send3DKafkaThread(int index, int frequency, Session session) {
        super(index, frequency, session);
    }

    @Override
    public void run() {
        Properties props = KafkaUtil.getConsumerProperties("group-1", SceneUpdateDeserializer.class.getName());
        String topic = "drive_3d";
        try (KafkaConsumer<String, SceneUpdate> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(topic));
            int i = 0;
            while (running) {
                ConsumerRecords<String, SceneUpdate> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, SceneUpdate> record : records) {
                    SceneUpdate update = record.value();
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(update);
                    byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), index);
                    this.session.sendBinary(bytes);
                    Thread.sleep(frequency);
                    if (i == 200) {
                        i = 0;
                        log.info("----------------kafka 3d message:");
                    }
                    i++;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("--------------------Kafka线程已经停止: " + Thread.currentThread().getName());
    }
}
