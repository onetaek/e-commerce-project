package com.study.ecommerce.domain.cart;

public class CartCommand {

	public record Search(
		String userId
	) {
	}

	public record Add(
		Long productId,
		int amount,
		String userId
	) {
		public Cart toCart() {
			return Cart.builder()
				.productId(productId)
				.amount(amount)
				.userId(userId)
				.build();
		}
	}

	public record Remove(
		Long id
	) {
	}
}
