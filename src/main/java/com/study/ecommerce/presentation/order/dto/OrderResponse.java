package com.study.ecommerce.presentation.order.dto;

import java.time.LocalDateTime;

import com.study.ecommerce.domain.order.dto.OrderInfo;

public record OrderResponse(
	Long id,
	String userId,
	LocalDateTime orderDate,
	Long totalPrice
) {
	public static OrderResponse fromInfo(OrderInfo order) {
		return new OrderResponse(
			order.id(),
			order.userId(),
			order.orderDate(),
			order.totalPrice()
		);
	}
}
