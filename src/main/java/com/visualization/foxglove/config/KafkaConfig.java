package com.visualization.foxglove.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfig {

    private String server;

    /**
     * 是否消费最新消息
     */
    private boolean latest;

    private String group;

    /**
     * 单词最大拉取数量
     */
    private int maxPollRecords;
}
