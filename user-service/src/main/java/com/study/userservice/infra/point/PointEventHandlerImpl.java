package com.study.userservice.infra.point;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.study.userservice.domain.point.PointEventHandler;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointEventHandlerImpl implements PointEventHandler {
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
