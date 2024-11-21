package com.study.productservice.domain.product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductEventCommand {
	public record InventoryDeduct(
		String transactionKey,
		long orderId,
		String userId,
		long totalPrice,
		List<Product> products
	) {
		public record Product(
			Long productId,
			int amount,
			long price
		) {
		}

		public static InventoryDeduct from(
			ProductCommand.Deduct command,
			String transactionKey,
			long totalPrice
		) {
			return new InventoryDeduct(
				transactionKey,
				command.orderId(),
				command.userId(),
				totalPrice,
				command.products().stream().map(product ->
					new ProductEventCommand.InventoryDeduct.Product(
						product.productId(),
						product.amount(),
						product.price()
					)).collect(Collectors.toList()
				)
			);
		}
	}

	public record InventoryDeductFailure(
		String transactionKey,
		String errorMessage,
		long orderId,
		String userId
	) {
	}
}
