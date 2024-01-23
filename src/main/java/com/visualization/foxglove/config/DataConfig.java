package com.visualization.foxglove.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "message")
public class DataConfig {

    private Text text;
    private Map map;
    private ThreeDim threeDim;

    @Data
    public static class Text {
        private int frequency;
        private String topic;
        private int pollDuration;
    }

    @Data
    public static class Map {
        private int frequency;
        private String topic;
        private int pollDuration;
    }

    @Data
    public static class ThreeDim {
        private int frequency;
        private String topic;
        private int pollDuration;
    }
}
