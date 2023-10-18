package com.jiaruiblog.foxglove.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class DataUtil {

    public static byte[] getFormatedBytes(byte[] data, long ns,int index) {
        byte constantInfo = 1;
        byte[] constantInfoByte = new byte[]{constantInfo};
        byte[] dataType = getIntBytes(index);
        byte[] nsTime = getLongBytes(ns);
        byte[] packz1 = byteConcat(constantInfoByte, dataType, nsTime);
        byte[] pack2 = byteConcat(packz1, data);
        return pack2;
    }

    public static String loadSchemaJson(String schemaFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream stream = classLoader.getResourceAsStream("schema/" + schemaFile);
            map = objectMapper.readValue(stream, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(map);
    }

    public static final long fx = 0xffL;

    /**
     * int 转 byte[]
     * 小端
     *
     * @param data
     * @return
     */
    public static byte[] getIntBytes(int data) {
        int length = 4;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) ((data >> (i * 8)) & fx);
        }
        return bytes;
    }

    /**
     * long 转 byte[]
     * 小端
     *
     * @param data
     * @return
     */
    public static byte[] getLongBytes(long data) {
        int length = 8;
        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) ((data >> (i * 8)) & fx);
        }
        return bytes;
    }

    /**
     * long 转 byte[]
     * 小端
     *
     * @param data
     * @return
     */
    public static byte[] getLongBytes2(long data) {
        int length = 8;
        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) ((data >> (i * 8)) & fx);
        }
        return bytes;
    }

    public static byte[] byteConcat(byte[] bt1, byte[] bt2) {
        byte[] bt4 = new byte[bt1.length + bt2.length];
        int len = 0;
        System.arraycopy(bt1, 0, bt4, 0, bt1.length);
        len += bt1.length;
        System.arraycopy(bt2, 0, bt4, len, bt2.length);
        return bt4;
    }

    public static byte[] byteConcat(byte[] bt1, byte[] bt2, byte[] bt3) {
        byte[] bt4 = new byte[bt1.length + bt2.length + bt3.length];
        int len = 0;
        System.arraycopy(bt1, 0, bt4, 0, bt1.length);
        len += bt1.length;
        System.arraycopy(bt2, 0, bt4, len, bt2.length);
        len += bt2.length;
        System.arraycopy(bt3, 0, bt4, len, bt3.length);
        return bt4;
    }

}
