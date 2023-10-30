package com.jiaruiblog.foxglove.message.test;

import com.jiaruiblog.foxglove.message.MessageGenerator;
import com.jiaruiblog.foxglove.schema.LocationFix;
import com.jiaruiblog.foxglove.schema.Timestamp;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GPSGenerator implements MessageGenerator<LocationFix> {

    private int index;
    private List<LocationFix> gpsList;

    public GPSGenerator() {
        gpsList = this.readMapData();
    }

    @Override
    public LocationFix consume() {
        if (index == gpsList.size()) {
            index = 0;
            log.info(Thread.currentThread().getName() + "==============播放完毕，新的轮回======================");
        }
        LocationFix locationFix = gpsList.get(index);
        index++;
        Timestamp timestamp = new Timestamp();
        int nano = Instant.now().getNano();
        long second = Instant.now().getEpochSecond();
        timestamp.setSec((int) second);
        timestamp.setNsec(nano);
        locationFix.setTimestamp(timestamp);
        return locationFix;
    }

    private List<LocationFix> readMapData() {
        List<LocationFix> dataList = new ArrayList<>();
        String file = "E:\\foxglove\\shanghai_gps.data";
        String str;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((str = br.readLine()) != null) {
                String[] data = str.split(",");
                LocationFix gps = new LocationFix(null, "map",
                        Float.parseFloat(data[0]), Float.parseFloat(data[1]), Float.parseFloat(data[2]));
                dataList.add(gps);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("==============解析完毕，共有" + dataList.size() + "条GPS记录======================");
        return dataList;
    }
}
