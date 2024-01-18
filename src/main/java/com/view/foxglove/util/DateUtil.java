package com.view.foxglove.util;

import com.view.foxglove.schema.Timestamp;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    public static Timestamp createTimestamp() {
        Timestamp timestamp = new Timestamp();
        int nano = Instant.now().getNano();
        long second = Instant.now().getEpochSecond();
        timestamp.setSec(second);
        timestamp.setNsec((long) nano);
        return timestamp;
    }

    public static Timestamp createTimestamp(String timeStr) {
        long mills = Long.parseLong(timeStr);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(mills);
        long nanos = TimeUnit.MILLISECONDS.toNanos(mills);
        Timestamp timestamp = Timestamp.builder().sec(seconds).nsec(nanos).build();
        return timestamp;
    }

    public static Timestamp createTimestamp(long mills) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(mills);
        long nanos = TimeUnit.MILLISECONDS.toNanos(mills);
        Timestamp timestamp = Timestamp.builder().sec(seconds).nsec(nanos).build();
        return timestamp;
    }
}
