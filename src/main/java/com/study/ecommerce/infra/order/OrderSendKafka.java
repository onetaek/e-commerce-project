package com.study.ecommerce.infra.order;

import org.springframework.stereotype.Component;

import com.study.ecommerce.domain.order.OrderSend;

@Component
public class OrderSendKafka implements OrderSend {
	@Override
	public void send() {
		try {
			System.out.println("슈웃");
		} catch (Exception ignore) {
		}
	}
}
