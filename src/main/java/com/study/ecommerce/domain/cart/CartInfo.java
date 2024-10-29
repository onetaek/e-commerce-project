package com.study.ecommerce.domain.cart;

import java.util.List;

public class CartInfo {

	public record Info(
		Long id,
		Long productId,
		int amount,
		String userId
	) {
		public static List<Info> from(List<Cart> cartItems) {
			return cartItems.stream().map(v ->
				new Info(
					v.getId(),
					v.getProductId(),
					v.getAmount(),
					v.getUserId()
				)
			).toList();
		}
	}

}
