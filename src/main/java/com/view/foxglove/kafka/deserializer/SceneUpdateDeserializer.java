package com.view.foxglove.kafka.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.view.foxglove.schema.SceneUpdate;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.UnsupportedEncodingException;

public class SceneUpdateDeserializer implements Deserializer<SceneUpdate> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SceneUpdate deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(new String(data, "UTF-8"), SceneUpdate.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
