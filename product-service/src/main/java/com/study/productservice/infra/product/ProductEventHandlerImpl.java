package com.study.productservice.infra.product;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.study.productservice.domain.product.ProductEventHandler;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductEventHandlerImpl implements ProductEventHandler {

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
