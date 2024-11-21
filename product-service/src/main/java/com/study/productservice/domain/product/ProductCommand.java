package com.study.productservice.domain.product;

import java.util.List;

public class ProductCommand {

	public record Deduct(
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

	public record InventoryRecovery(
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
