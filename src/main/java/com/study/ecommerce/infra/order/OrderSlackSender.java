package com.study.ecommerce.infra.order;

import org.springframework.stereotype.Component;

import com.study.ecommerce.domain.order.OrderSender;

@Component
public class OrderSlackSender implements OrderSender {

	@Override
	public void send(Long orderId, Long totalPrice) {
		try {
			System.out.println("슈웃");
		} catch (Exception ignore) {
		}
	}
}
