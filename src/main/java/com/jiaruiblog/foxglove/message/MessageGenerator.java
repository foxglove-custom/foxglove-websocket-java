package com.jiaruiblog.foxglove.message;

public interface MessageGenerator<T> {
    T consume();
}
