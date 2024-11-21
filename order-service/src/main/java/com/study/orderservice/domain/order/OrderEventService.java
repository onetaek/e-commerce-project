package com.study.orderservice.domain.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderEventService {

	private final OrderEventHandler orderEventHandler;

	public <T> void send(
		String orderOrderCreatedTopic,
		String key,
		T event
	) {
		orderEventHandler.send(orderOrderCreatedTopic, key, event);
	}

	@Transactional
	public <T> void publishEvent(T orderCreateEvent) {
		orderEventHandler.publish(orderCreateEvent);
	}

	public void sendOrderProcessedAlert(OrderEventCommand.SendOrderProcessedAlert command) {
		orderEventHandler.sendOrderProcessedAlert(command);
	}

	public void sendOrderFailureAlert(OrderEventCommand.SendOrderFailureAlert command) {
		orderEventHandler.sendOrderFailureAlert(command);
	}
}
