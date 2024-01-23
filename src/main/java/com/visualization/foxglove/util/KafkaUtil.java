package com.visualization.foxglove.util;

import com.visualization.foxglove.config.DataConfig;
import com.visualization.foxglove.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

import static java.util.jar.Pack200.Packer.LATEST;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.apache.kafka.clients.consumer.OffsetResetStrategy.EARLIEST;

@Slf4j
public class KafkaUtil {

    public static Properties getConsumerProperties(KafkaConfig serverConfig) {
        Properties props = new Properties();
        props.put("bootstrap.servers", serverConfig.getServer());
        props.put("group.id", serverConfig.getGroup());
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("max.poll.records", "100");
        props.put("group.max.session.timeout.ms", "35000");
        props.put("batch.size", "10240");
        props.put("buffer.memory", "40960");
        props.put(ENABLE_AUTO_COMMIT_CONFIG, "true");

        String autoOffsetConfig = serverConfig.isLatest() ? LATEST : EARLIEST.toString();
        props.put(AUTO_OFFSET_RESET_CONFIG, autoOffsetConfig);
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return props;
    }

}
