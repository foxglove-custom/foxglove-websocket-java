package com.jiaruiblog.foxglove.message.test;

import com.jiaruiblog.foxglove.message.MessageGenerator;
import com.jiaruiblog.foxglove.schema.RawMessage;

import java.time.LocalTime;
import java.util.Random;

public class RawMessageGenerator implements MessageGenerator<RawMessage> {

    @Override
    public RawMessage consume() {
        RawMessage message = new RawMessage();
        message.setCount(new Random().nextInt(1000));
        message.setNumber(new Random().nextInt(1000));
        message.setMsg("Hello at " + LocalTime.now());
        return message;
    }
}
