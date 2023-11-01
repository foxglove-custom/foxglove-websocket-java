package com.jiaruiblog.foxglove.thread.kafka;

import com.alibaba.fastjson.JSONObject;
import com.jiaruiblog.foxglove.kafka.deserializer.RawMessageDeserializer;
import com.jiaruiblog.foxglove.schema.RawMessage;
import com.jiaruiblog.foxglove.thread.SendDataThread;
import com.jiaruiblog.foxglove.util.KafkaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.yeauty.pojo.Session;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static com.jiaruiblog.foxglove.util.DataUtil.getFormatedBytes;

@Slf4j
public class SendMessageKafkaThread extends SendDataThread {

    public SendMessageKafkaThread(int index, int frequency, Session session) {
        super(index, frequency, session);
    }

    @Override
    public void run() {
        Properties props = KafkaUtil.getConsumerProperties("group-1", RawMessageDeserializer.class.getName());
        String topic = "raw_message";
        try (KafkaConsumer<String, RawMessage> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(topic));
            boolean validCode = StringUtils.isNotBlank(code);
            int i = 0;
            while (running) {
                ConsumerRecords<String, RawMessage> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, RawMessage> record : records) {
                    RawMessage message = record.value();
                    if (validCode) {
                        message.setMsg(message.getMsg() + "\n底盘号: " + code);
                    }
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(message);
                    byte[] bytes = getFormatedBytes(jsonObject.toJSONString().getBytes(), index);
                    this.session.sendBinary(bytes);
                    if (i == 200) {
                        i = 0;
                        log.info("----------------kafka raw message:\t" + message);
                    }
                }
                i++;
                Thread.sleep(frequency);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("--------------------Kafka线程已经停止: " + Thread.currentThread().getName());
    }
}
