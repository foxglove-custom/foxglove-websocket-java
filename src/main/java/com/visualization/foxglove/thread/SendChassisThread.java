package com.visualization.foxglove.thread;

import com.alibaba.fastjson.JSONObject;
import com.visualization.foxglove.schema.ChassisInfo;
import com.visualization.foxglove.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yeauty.pojo.Session;

import static com.visualization.foxglove.util.DataUtil.getFormattedBytes;

@Slf4j
public class SendChassisThread extends SendDataThread {

    private String oldCode;

    public SendChassisThread(int index, int frequency, Session session) {
        super(index, frequency, session);
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (!StringUtils.equals(oldCode, chassisCode)) {
                    oldCode = chassisCode;
                    ChassisInfo chassis = new ChassisInfo();
                    chassis.setTimestamp(DateUtil.createTimestamp());
                    chassis.setChassisCode(chassisCode);
                    chassis.setRtspUrl(getRtspURL());
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(chassis);
                    byte[] bytes = getFormattedBytes(jsonObject.toJSONString().getBytes(), index);
                    this.session.sendBinary(bytes);
                    log.info("---------------chassis info:\t" + chassis);
                }
                sleep(frequency);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getRtspURL() {
        String url = "http://127.0.0.1:8888/demo1";
        return url;
    }
}
