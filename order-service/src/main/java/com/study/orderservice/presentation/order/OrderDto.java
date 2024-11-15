package com.study.orderservice.presentation.order;

import java.time.LocalDateTime;
import java.util.List;

import com.study.orderservice.domain.order.OrderCommand;

public class OrderDto {

	public record Request(
		String userId,
		LocalDateTime orderDate,
		List<Product> products
	) {
		public record Product(
			Long productId,
			int amount
		) {
		}

		public OrderCommand.Order toCommand() {
			return new OrderCommand.Order(
				userId,
				orderDate,
				products.stream().map(v ->
					new OrderCommand.Order.Product(
						v.productId(),
						v.amount()
					)
				).toList()
			);
		}
	}

	public record Event(
		Long orderId,
		Long totalPrice
	) {

	}

}
