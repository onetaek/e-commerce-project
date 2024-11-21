package com.study.productservice.domain.product;

public interface ProductEventHandler {

	<T> void send(String topic, String key, T payload);

	<T> void publish(T event);
}
