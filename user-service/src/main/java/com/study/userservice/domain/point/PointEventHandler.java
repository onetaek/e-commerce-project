package com.study.userservice.domain.point;

public interface PointEventHandler {

	<T> void send(String topic, String key, T payload);

	<T> void publish(T event);
}
