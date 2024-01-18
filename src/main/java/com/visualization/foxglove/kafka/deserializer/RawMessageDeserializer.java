package com.visualization.foxglove.kafka.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visualization.foxglove.schema.RawMessage;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.UnsupportedEncodingException;

public class RawMessageDeserializer implements Deserializer<RawMessage> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public RawMessage deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(new String(data, "UTF-8"), RawMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
