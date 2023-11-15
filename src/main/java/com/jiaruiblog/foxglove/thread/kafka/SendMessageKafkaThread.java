package com.jiaruiblog.foxglove.thread.kafka;

import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.schema.ControlData;
import com.jiaruiblog.foxglove.thread.SendDataThread;
import com.jiaruiblog.foxglove.util.KafkaUtil;
import com.jiaruiblog.foxglove.util.SysConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.yeauty.pojo.Session;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormattedBytes;

@Slf4j
public class SendMessageKafkaThread extends SendDataThread {

    private String topic;
    private String group;

    public SendMessageKafkaThread(int index, int frequency, Session session, String topic, String group) {
        super(index, frequency, session);
        this.topic = topic;
        this.group = group;
    }

    @Override
    public void run() {
        Properties props = KafkaUtil.getConsumerProperties(group, StringDeserializer.class.getName());
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(topic));
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    String[] data = record.value().split(SysConstant.DF_KAFKA_DATA_SEPARATOR);
                    String chassisCode = data[0];
                    if (!this.chassisCode.equals(chassisCode)) {
                        continue;
                    }
                    ControlData controlData = this.convertToControlData(data);
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(controlData);
                    byte[] bytes = getFormattedBytes(jsonObject.toJSONString().getBytes(), index);
                    this.session.sendBinary(bytes);
                    Thread.sleep(frequency);
                    super.printLog();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("--------------------Kafka线程已经停止: " + Thread.currentThread().getName());
    }


    private ControlData convertToControlData(String[] data) {

        ControlData controlData = ControlData.builder()
                .chassisCode(data[0])
                .terminalid(data[1])
                .gpstime(data[2])
                .latitude(data[3])
                .longitude(data[4])
                .height(data[5])
                .sysstADMdmp(data[6])
                .spdPlnlTrgLngErrmp(data[7])
                .spdPlnvTrgSpdmp(data[8])
                .spdPlnaTrgAccmp(data[9])
                .bhvCrdnnumBhvIDmp(data[10])
                .vehDarSlopmp(data[11])
                .vehDamWghtmp(data[12])
                .vehDavegoSpdmp(data[13])
                .vehDaaEgoAccmp(data[14])
                .vehDastTraCurGearmp(data[15])
                .vehDarTraCurGearmp(data[16])
                .vehDastCluSwtmp(data[17])
                .vehDaprcTrqEngNomFricmp(data[18])
                .vehDastSrcBrkmp(data[19])
                .vehDaprcActuTrqmp(data[20])
                .vehDaprcDrvrDmdTrqmp(data[21])
                .vehDastSrcEngCtrlmp(data[22])
                .vehDapFrontLeftmp(data[23])
                .vehDanEngSpdmp(data[24])
                .vehDastTrlrCnctnmp(data[25])
                .vehDastTraEgdmp(data[26])
                .vehDastTraSelGearmp(data[27])
                .vehDastTraShtmp(data[28])
                .vehDarBrkPedlmp(data[29])
                .vehDaprcTrqEstimdLossmp(data[30])
                .vehDastTraTrqLimmp(data[31])
                .vehDarAccrPedlmp(data[32])
                .ACCSvSetFrmSysmp(data[33])
                .vehDanOutpShaftmp(data[34])
                .vehDastBrkReady4Rlsmp(data[35])
                .yawrate(data[36])
                .ax(data[37])
                .ay(data[38])
                .vDCfrDrvForce(data[39])
                .build();
        return controlData;
    }
}
