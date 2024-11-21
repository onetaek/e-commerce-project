package com.study.orderservice.presentation.order;

import java.time.LocalDateTime;
import java.util.List;

import com.study.orderservice.domain.eventbox.CommonEventBox;
import com.study.orderservice.domain.order.OrderCommand;
import com.study.orderservice.domain.order.OrderEventCommand;
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

		public OrderCommand.OrderCreate toCommand() {
			return new OrderCommand.OrderCreate(
				userId,
				orderDate,
				products.stream().map(v ->
					new OrderCommand.OrderCreate.Product(
						v.productId(),
						v.amount()
					)
				).toList()
			);
		}
	}

	public record OrderCreateMessage(
		String userId,
		Long orderId,
		List<OrderEventCommand.OrderCreate.OrderItem> orderItems
	) {
		public record OrderItem(
			long productId,
			int amount,
			long price
		) {
		}
	}

	public record InventoryDeductFailureMessage(
		long orderId,
		String userId,
		String errorMessage
	) {
		public OrderCommand.CancelOrder toCommand() {
			return new OrderCommand.CancelOrder(orderId, userId);
		}
	}

	public record UserPointUseMessage(
		String userId,
		long orderId,
		long sumPrice
	) {
		public OrderCommand.OrderSendMessageCreate toCommand() {
			return new OrderCommand.OrderSendMessageCreate(
				userId,
				orderId
			);
		}
	}

	public record RetrySendMessage(
		CommonEventBox.Status status
	) {
		public RetrySendMessage {
			if (status == CommonEventBox.Status.PROCESSED) {
				throw new IllegalArgumentException("Status cannot be PROCESSED");
			}
		}
	}
}
