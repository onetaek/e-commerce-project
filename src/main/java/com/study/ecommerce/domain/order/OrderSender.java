package com.study.ecommerce.domain.order;

public interface OrderSender {
	void send(Long orderId, Long totalPrice);
}
