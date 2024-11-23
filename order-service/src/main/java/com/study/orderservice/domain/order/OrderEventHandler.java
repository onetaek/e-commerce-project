package com.study.orderservice.domain.order;

public interface OrderEventHandler {

	<T> void send(String topic, String key, T payload);

	<T> void publish(T event);
}
