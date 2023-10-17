package com.jiaruiblog.foxglove.util;

public class DataUtil {

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
