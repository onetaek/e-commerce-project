package com.study.ecommerce.domain.order.dto;

import java.time.LocalDateTime;

import com.study.ecommerce.domain.order.Order;

public record OrderInfo(
	Long id,
	Long userId,
	LocalDateTime orderDate,
	Long totalPrice
) {
	public static OrderInfo fromDomain(Order order) {
		return new OrderInfo(
			order.getId(),
			order.getUserId(),
			order.getOrderDate(),
			order.getTotalPrice()
		);
	}
}
