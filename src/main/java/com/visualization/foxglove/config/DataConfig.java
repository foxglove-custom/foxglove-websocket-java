package com.visualization.foxglove.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "message")
public class DataConfig {

    private Chassis chassis;
    private Text text;
    private Map map;
    private ThreeDim threeDim;
    private RTSP rtsp;

    @Data
    public static class Chassis {
        private int frequency;
        private String group;
    }

    @Data
    public static class BaseMQ {
        private int partition;
        private int frequency;
        private String topic;
        private int pollDuration;
    }

    @Data
    public static class Text extends BaseMQ {
    }

    @Data
    public static class Map extends BaseMQ {
    }

    @Data
    public static class ThreeDim extends BaseMQ {
    }

    @Data
    public static class RTSP {
        private int frequency;
        private String url;
    }
}
