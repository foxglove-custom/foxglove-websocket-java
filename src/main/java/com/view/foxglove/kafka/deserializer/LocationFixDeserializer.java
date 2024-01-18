package com.view.foxglove.kafka.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.view.foxglove.schema.LocationFix;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.UnsupportedEncodingException;

public class LocationFixDeserializer implements Deserializer<LocationFix> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public LocationFix deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(new String(data, "UTF-8"), LocationFix.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
