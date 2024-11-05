package com.study.ecommerce.presentation.product;

import java.util.List;

import com.study.ecommerce.domain.product.ProductInfo;

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
	}

	public record OrderAmountResponse(
		Long productId,
		String productName,
		Long productPrice,
		Integer orderAmount
	) {
		public static List<ProductDto.OrderAmountResponse> from(
			List<ProductInfo.OrderAmount> products) {
			return products.stream()
				.map(product -> new ProductDto.OrderAmountResponse(
					product.productId(),
					product.productName(),
					product.productPrice(),
					product.orderAmount()
				)).toList();
		}
	}
}
