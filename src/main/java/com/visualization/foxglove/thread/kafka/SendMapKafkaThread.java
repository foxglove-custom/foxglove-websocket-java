package com.visualization.foxglove.thread.kafka;

import com.alibaba.fastjson.JSONObject;
import com.visualization.foxglove.config.AppCtxUtil;
import com.visualization.foxglove.config.DataConfig;
import com.visualization.foxglove.config.KafkaConfig;
import com.visualization.foxglove.entity.GPS;
import com.visualization.foxglove.schema.LocationFix;
import com.visualization.foxglove.schema.Timestamp;
import com.visualization.foxglove.thread.SendDataThread;
import com.visualization.foxglove.util.DateUtil;
import com.visualization.foxglove.util.GPSUtils;
import com.visualization.foxglove.util.KafkaUtil;
import com.visualization.foxglove.util.SysConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.yeauty.pojo.Session;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.visualization.foxglove.util.DataUtil.getFormattedBytes;

@Slf4j
public class SendMapKafkaThread extends SendDataThread {

    private DataConfig dataConfig;
    private KafkaConfig kafkaConfig;

    public SendMapKafkaThread(int index, Session session) {
        super(index, session);
        this.kafkaConfig = AppCtxUtil.getBean(KafkaConfig.class);
        this.dataConfig = AppCtxUtil.getBean(DataConfig.class);
        this.frequency = dataConfig.getMap().getFrequency();
        this.noDataSleepCount = dataConfig.getNoDataSleepCount();
    }

    @Override
    public void run() {
        Properties props = KafkaUtil.getConsumerProperties(kafkaConfig);
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            DataConfig.Map config = dataConfig.getMap();
            String topic = config.getTopic();
            int pollDuration = config.getPollDuration();
            TopicPartition topicPartition = new TopicPartition(topic, config.getPartition());
            List<TopicPartition> topics = Arrays.asList(topicPartition);
            consumer.assign(topics);
            if (kafkaConfig.isLatest()) {
                consumer.seekToEnd(topics);
            }
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(pollDuration));
                if (records.isEmpty()) {
                    super.printKafkaNoDataMessage();
                }
                chassisHasData = false;
                for (ConsumerRecord<String, String> record : records) {
                    String[] data = record.value().split(SysConstant.DF_KAFKA_DATA_SEPARATOR);
                    String chassisCode = data[0];
                    if (!this.chassisCode.equals(chassisCode)) {
                        continue;
                    }
                    chassisHasData = true;
                    LocationFix location = this.convertToLocationFix(data);
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(location);
                    byte[] bytes = getFormattedBytes(jsonObject.toJSONString().getBytes(), index);
                    this.session.sendBinary(bytes);
                    Thread.sleep(frequency);
                    printLog();
                }
                if (!chassisHasData) {
                    super.printChassisNoDataMessage();
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
        GPS gps = GPSUtils.transform(latitude, longitude);

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
