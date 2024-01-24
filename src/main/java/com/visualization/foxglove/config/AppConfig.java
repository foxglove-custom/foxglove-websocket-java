package com.visualization.foxglove.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.annotation.Resource;

@Slf4j
@Configuration
public class AppConfig {

    @Resource
    private KafkaConfig kafkaConfig;

    @Resource
    private DataConfig dataConfig;

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() throws JsonProcessingException {
        log.info("====================data visualization started success========================");
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        log.info("kafka server config:\n" + writer.writeValueAsString(kafkaConfig));
        log.info("kafka message config:\n" + writer.writeValueAsString(dataConfig));
    }
}
