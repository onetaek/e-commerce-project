package com.study.ecommerce.domain.order.dto;

import java.time.LocalDateTime;

import com.study.ecommerce.domain.order.Order;

public record OrderCommand(
	String userId,
	LocalDateTime orderDate,
	Long totalPrice
) {
	public Order toDomain() {
		return Order.builder()
			.userId(userId)
			.orderDate(orderDate)
			.totalPrice(totalPrice)
			.build();
	}
}
