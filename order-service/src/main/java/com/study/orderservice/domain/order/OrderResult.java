package com.study.orderservice.domain.order;

import java.util.List;

public class OrderResult {

	public record Detail(
		long orderItemId,
		long orderId,
		long productId,
		int amount,
		long price
	) {
		public static List<OrderResult.Detail> from(List<OrderItem> orderItems) {
			return orderItems.stream().map(v ->
				new OrderResult.Detail(
					v.getId(),
					v.getOrderId(),
					v.getProductId(),
					v.getAmount(),
					v.getPrice())
			).toList();
		}
	}

	public record GroupByProduct(
		long productId,
		int orderAmount
	) {
	}
}
