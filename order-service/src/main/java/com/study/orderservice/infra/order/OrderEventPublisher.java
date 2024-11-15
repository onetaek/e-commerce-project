package com.study.orderservice.infra.order;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.study.orderservice.domain.order.OrderSender;
import com.study.orderservice.presentation.order.OrderDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher implements OrderSender {

	private final ApplicationEventPublisher eventPublisher;

	@Override
	public void sendEvent(Long orderId, Long totalPrice) {
		eventPublisher.publishEvent(new OrderDto.Event(orderId, totalPrice));
	}
}
