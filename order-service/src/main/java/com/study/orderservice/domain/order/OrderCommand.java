package com.study.orderservice.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.study.orderservice.domain.product.ProductInfo;

public class OrderCommand {

	public record Order(
		String userId,
		LocalDateTime orderDate,
		List<Product> products
	) {
		public com.study.orderservice.domain.order.Order toOrder() {
			return com.study.orderservice.domain.order.Order.builder()
				.userId(userId)
				.orderDate(orderDate)
				.build();
		}

		public List<OrderItem> toOrderItem(Long orderId, Map<Long, ProductInfo.Data> productInfoMap) {
			return products.stream().map(v -> OrderItem.builder()
					.orderId(orderId)
					.productId(v.productId)
					.amount(v.amount)
					.price(productInfoMap.get(v.productId).price())
					.build())
				.toList();
		}

		public Payment toPayment(Long orderId, long price) {
			return Payment.builder()
				.orderId(orderId)
				.price(price)
				.status(Payment.Status.COMPLETE)
				.build();
		}

		public record Product(
			Long productId,
			int amount
		) {
		}
	}
}
