package com.study.orderservice.presentation.order;

import java.time.LocalDateTime;
import java.util.List;

import com.study.orderservice.domain.order.OrderCommand;
import com.study.orderservice.domain.order.OrderInfo;

public class OrderDto {

	public record PopularRequest(
		LocalDateTime fromOrderDate,
		LocalDateTime toOrderDate,
		Long limit
	) {
		public OrderCommand.Search toCommand() {
			return new OrderCommand.Search(
				fromOrderDate,
				toOrderDate,
				limit
			);
		}
	}

	public record OrderAmountResponse(
		Long productId,
		String productName,
		Long productPrice,
		Integer orderAmount
	) {
		public static List<OrderAmountResponse> from(
			List<OrderInfo.Product> products
		) {
			return products.stream()
				.map(product -> new OrderDto.OrderAmountResponse(
					product.productId(),
					product.productName(),
					product.productPrice(),
					product.orderAmount()
				)).toList();
		}
	}

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
