package com.study.ecommerce.domain.product;

public class ProductInfo {
	public record Amount(
		Long id,
		String name,
		Long price,
		int amount
	) {
	}

	public record OrderAmount(
		Long productId,
		String productName,
		Long productPrice,
		Integer orderAmount
	) {
	}

	public record Data(
		Long id,
		String name,
		Long price
	) {

	}
}
