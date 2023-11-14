package com.jiaruiblog.foxglove.util;

import com.jiaruiblog.foxglove.schema.Timestamp;

import java.time.Instant;

public class DateUtil {

    public static Timestamp createTimestamp() {
        Timestamp timestamp = new Timestamp();
        int nano = Instant.now().getNano();
        long second = Instant.now().getEpochSecond();
        timestamp.setSec(second);
        timestamp.setNsec((long) nano);
        return timestamp;
    }
}
