package com.jiaruiblog.foxglove.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.schema.LocationFix;
import com.jiaruiblog.foxglove.schema.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.yeauty.pojo.Session;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

@Slf4j
public class SendGPSThread extends SendDataThread {

    private List<LocationFix> gpsList;

    public SendGPSThread(int index, int frequency, Session session) {
        super(index, frequency, session);
        gpsList = this.readMapData();
    }

    private List<LocationFix> readMapData() {
        List<LocationFix> dataList = new ArrayList<>();
        String file = "E:\\foxglove\\shanghai_gps.data";
        String str;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((str = br.readLine()) != null) {
                String[] data = str.split(",");
                LocationFix gps = new LocationFix(null, "map", Float.parseFloat(data[0]), Float.parseFloat(data[1]), Float.parseFloat(data[2]));
                dataList.add(gps);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("==============解析完毕，共有" + dataList.size() + "条记录======================");
        return dataList;
    }

    @Override
    public void run() {
        int i = 0, size = gpsList.size();
        while (running) {
            if (i >= size) {
                i = 0;
                log.info(Thread.currentThread().getName() + "==============播放完毕，新的轮回======================");
            }
            LocationFix locationFix = gpsList.get(i);
            i++;
            Timestamp timestamp = new Timestamp();
            int nano = Instant.now().getNano();
            long second = Instant.now().getEpochSecond();
            timestamp.setSec((int) second);
            timestamp.setNsec(nano);
            locationFix.setTimestamp(timestamp);
            JSONObject jsonObject = (JSONObject) JSON.toJSON(locationFix);
            byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), index);
            this.session.sendBinary(bytes);
            printLog(100);
            try {
                Thread.sleep(frequency);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
