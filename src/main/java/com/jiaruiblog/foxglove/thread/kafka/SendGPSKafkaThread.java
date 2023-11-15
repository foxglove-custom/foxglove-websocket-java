package com.jiaruiblog.foxglove.thread.kafka;

import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.entity.GPS;
import com.jiaruiblog.foxglove.schema.LocationFix;
import com.jiaruiblog.foxglove.schema.Timestamp;
import com.jiaruiblog.foxglove.thread.SendDataThread;
import com.jiaruiblog.foxglove.util.DateUtil;
import com.jiaruiblog.foxglove.util.GPSConverterUtils;
import com.jiaruiblog.foxglove.util.KafkaUtil;
import com.jiaruiblog.foxglove.util.SysConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.yeauty.pojo.Session;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormattedBytes;

@Slf4j
public class SendGPSKafkaThread extends SendDataThread {

    private String topic;
    private String group;

    public SendGPSKafkaThread(int index, int frequency, Session session, String topic, String group) {
        super(index, frequency, session);
        this.topic = topic;
        this.group = group;
    }

    @Override
    public void run() {
        Properties props = KafkaUtil.getConsumerProperties(group, StringDeserializer.class.getName());
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            TopicPartition partition = new TopicPartition(topic, 0);
            consumer.assign(Arrays.asList(partition));
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    String[] data = record.value().split(SysConstant.DF_KAFKA_DATA_SEPARATOR);
                    String chassisCode = data[0];
                    if (!this.chassisCode.equals(chassisCode)) {
                        continue;
                    }
                    LocationFix location = this.convertToLocationFix(data);
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(location);
                    byte[] bytes = getFormattedBytes(jsonObject.toJSONString().getBytes(), index);
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

    private LocationFix convertToLocationFix(String[] data) {
        String chassisCode = data[0];
        double latitude = Double.parseDouble(data[3]) / 1_000_000;
        double longitude = Float.parseFloat(data[4]) / 1_000_000;
        GPS gps = GPSConverterUtils.transform(latitude, longitude);

        Timestamp timestamp = DateUtil.createTimestamp(data[1]);

        LocationFix locationFix = new LocationFix();
        locationFix.setTimestamp(timestamp);
        locationFix.setLatitude(gps.getLat());
        locationFix.setLongitude(gps.getLon());
        locationFix.setAltitude(0);
        locationFix.setChassisCode(chassisCode);

        return locationFix;
    }
}
