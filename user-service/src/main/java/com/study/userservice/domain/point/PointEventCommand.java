package com.study.userservice.domain.point;

import java.util.List;

public class PointEventCommand {

	public record Use(
		String transactionKey,
		String userId,
		long orderId,
		long sumPrice
	) {
	}

	public record UseFailure(
		String transactionKey,
		String errorMessage,
		String userId,
		long orderId,
		List<Product> products
	) {
		public record Product(
			Long productId,
			int amount,
			long price
		) {
		}
	}
}
