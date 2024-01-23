package com.visualization.foxglove.util;

import com.visualization.foxglove.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

@Slf4j
public class KafkaUtil {

    public static Properties getConsumerProperties(KafkaConfig config) {
        Properties props = new Properties();
        props.put("bootstrap.servers", config.getServer());
        props.put("group.id", config.getGroup());
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("auto.offset.reset", "earliest");
        props.put("group.max.session.timeout.ms", "35000");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return props;
    }

}
