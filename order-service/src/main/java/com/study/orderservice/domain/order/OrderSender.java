package com.study.orderservice.domain.order;

public interface OrderSender {
	void sendEvent(Long orderId, Long totalPrice);
}
