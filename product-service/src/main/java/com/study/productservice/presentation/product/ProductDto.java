package com.study.productservice.presentation.product;

import java.util.List;

import com.study.productservice.domain.product.ProductCommand;
import com.study.productservice.domain.product.ProductInfo;

public class ProductDto {

	public record AmountResponse(
		Long id,
		String name,
		Long price,
		int amount
	) {
		public static List<ProductDto.AmountResponse> from(
			List<ProductInfo.Amount> products) {
			return products.stream()
				.map(product -> new ProductDto.AmountResponse(
					product.id(),
					product.name(),
					product.price(),
					product.amount()
				)).toList();
		}

		public static ProductDto.AmountResponse from(
			ProductInfo.Amount product) {
			return new ProductDto.AmountResponse(
				product.id(),
				product.name(),
				product.price(),
				product.amount());
		}
	}

	public record OrderCreatedMessage(
		String userId,
		long orderId,
		List<Product> orderItems
	) {
		public record Product(
			Long productId,
			int amount,
			long price
		) {
		}

		public ProductCommand.Deduct toCommand() {
			return new ProductCommand.Deduct(
				userId,
				orderId,
				orderItems.stream()
					.map(orderItem -> new ProductCommand.Deduct.Product(
						orderItem.productId(),
						orderItem.amount(),
						orderItem.price()
					)).toList()
			);
		}

		public ProductCommand.InventoryRecovery toRecoveryCommand() {
			return new ProductCommand.InventoryRecovery(
				userId,
				orderId,
				orderItems.stream()
					.map(orderItem -> new ProductCommand.InventoryRecovery.Product(
						orderItem.productId(),
						orderItem.amount(),
						orderItem.price()
					)).toList()
			);
		}
	}

	public record InventoryDeductMessage(
		long orderId,
		String userId,
		long totalPrice
	) {
	}

	public record PointUseFailureMessage(
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

		public ProductCommand.InventoryRecovery toCommand() {
			return new ProductCommand.InventoryRecovery(
				userId,
				orderId,
				products.stream()
					.map(product -> new ProductCommand.InventoryRecovery.Product(
						product.productId(),
						product.amount(),
						product.price()
					)).toList()
			);
		}
	}

	public record InventoryDeductFailureMessage(
		long orderId
	) {
	}
}