package com.jiaruiblog.foxglove.thread.kafka;

import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.kafka.deserializer.LocationFixDeserializer;
import com.jiaruiblog.foxglove.schema.LocationFix;
import com.jiaruiblog.foxglove.thread.SendDataThread;
import com.jiaruiblog.foxglove.util.KafkaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.yeauty.pojo.Session;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

@Slf4j
public class SendGPSKafkaThread extends SendDataThread {

    public SendGPSKafkaThread(int index, int frequency, Session session) {
        super(index, frequency, session);
    }

    @Override
    public void run() {
        Properties props = KafkaUtil.getConsumerProperties("group-1", LocationFixDeserializer.class.getName());
        String topic = "drive_gps";
        try (KafkaConsumer<String, LocationFix> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(topic));
            boolean validCode = StringUtils.isNotBlank(code);
            while (running) {
                ConsumerRecords<String, LocationFix> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, LocationFix> record : records) {
                    LocationFix gps = record.value();
                    if (validCode) {
                        gps.setChassisCode(code);
                    }
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(gps);
                    byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), index);
                    this.session.sendBinary(bytes);
                    Thread.sleep(frequency);
                    printLog();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("--------------------Kafka线程已经停止: " + Thread.currentThread().getName());
    }
}
