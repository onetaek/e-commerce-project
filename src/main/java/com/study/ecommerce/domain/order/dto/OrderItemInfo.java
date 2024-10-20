package com.study.ecommerce.domain.order.dto;

import com.study.ecommerce.domain.order.OrderItem;

public record OrderItemInfo(
	Long id,
	Long orderId,
	Long productId,
	int amount,
	int price
) {
	public static OrderItemInfo fromDomain(OrderItem orderItem) {
		return new OrderItemInfo(
			orderItem.getId(),
			orderItem.getOrderId(),
			orderItem.getProductId(),
			orderItem.getAmount(),
			orderItem.getPrice()
		);
	}
}
