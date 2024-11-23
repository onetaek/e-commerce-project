package com.study.orderservice.infra.order;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.study.orderservice.domain.order.OrderEventHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventHandlerImpl implements OrderEventHandler {

	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	public <T> void send(String topic, String key, T payload) {
		kafkaTemplate.send(topic, key, payload);
	}

	@Override
	public <T> void publish(T event) {
		applicationEventPublisher.publishEvent(event);
	}
}
