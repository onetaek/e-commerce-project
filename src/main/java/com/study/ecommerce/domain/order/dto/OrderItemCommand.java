package com.study.ecommerce.domain.order.dto;

import com.study.ecommerce.domain.order.OrderItem;

public record OrderItemCommand(
	Long orderId,
	Long productId,
	int amount,
	int price
) {
	public OrderItem toDomain() {
		return OrderItem.builder()
			.orderId(orderId)
			.productId(productId)
			.amount(amount)
			.price(price)
			.build();
	}
}
