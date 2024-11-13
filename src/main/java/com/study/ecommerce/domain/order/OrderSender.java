package com.study.ecommerce.domain.order;

public interface OrderSender {
	void sendEvent(Long orderId, Long totalPrice);
}
