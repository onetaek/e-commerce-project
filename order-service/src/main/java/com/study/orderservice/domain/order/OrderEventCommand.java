package com.study.orderservice.domain.order;

import java.util.List;
import java.util.Map;

import com.study.orderservice.domain.product.ProductInfo;

public class OrderEventCommand {

	public record OrderCreate(
		String userId,
		Long orderId,
		List<OrderItem> orderItems
	) {
		public record OrderItem(
			long productId,
			int amount,
			long price
		) {
		}
	}

	public record SendOrderProcessedAlert(
		String userId,
		long orderId,
		long sumPrice,
		List<OrderItems> orderItems
	) {
		public record OrderItems(
			long id,
			long productId,
			String productName,
			int amount,
			long price
		) {
		}

		public static SendOrderProcessedAlert from(
			String userId,
			List<OrderResult.Detail> orderItemResult,
			Map<Long, ProductInfo.Data> productMap
		) {
			return new SendOrderProcessedAlert(
				userId,
				orderItemResult.get(0).orderId(),
				orderItemResult.stream().mapToLong(value -> value.price() * value.amount()).sum(),
				orderItemResult.stream().map(v -> {
					ProductInfo.Data product = productMap.get(v.productId());
					return new OrderItems(
						v.orderItemId(),
						v.productId(),
						product.name(),
						v.amount(),
						v.price()
					);
				}).toList()
			);
		}
	}

	public record SendOrderFailureAlert(
		String userId,
		String errorMessage
	) {
	}
}
