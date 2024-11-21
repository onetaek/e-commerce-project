package com.study.productservice.domain.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductEventService {

	private final ProductEventHandler productEventHandler;

	@Transactional
	public <T> void publishEvent(T event) {
		productEventHandler.publish(event);
	}

	public <T> void send(
		String productInventoryDeductTopic,
		String key,
		T event
	) {
		productEventHandler.send(productInventoryDeductTopic, key, event);
	}
}
