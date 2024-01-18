package com.visualization.foxglove.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class PointCloudUtil {

    public static byte[] readFile(String fileName) {
        List<String> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains("nan")) {
                    continue;
                }
                String[] data = line.split("\\s+");
                int length = data.length;
                if (length != 4) {
                    continue;
                }
                dataList.add(data[0]);
                dataList.add(data[1]);
                dataList.add(data[2]);
                dataList.add(data[3]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteBuffer buffer = ByteBuffer.allocate(dataList.size() * 4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int count = 0;
        for (String obj : dataList) {
            count++;
            if (count % 4 == 0) {
                count = 0;
                buffer.putInt(Integer.parseInt(obj));
            } else {
                buffer.putFloat(Float.parseFloat(obj));
            }
        }
        return buffer.array();
    }
}
