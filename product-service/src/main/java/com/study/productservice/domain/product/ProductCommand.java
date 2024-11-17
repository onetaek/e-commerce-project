package com.study.productservice.domain.product;

import java.time.LocalDateTime;
import java.util.List;

public class ProductCommand {

	public record Search(
		LocalDateTime fromOrderDate,
		LocalDateTime toOrderDate,
		Long limit
	) {
	}

	public record Deduct(
		String userId,
		List<Product> products
	) {
		public record Product(
			Long productId,
			int amount
		) {
		}
	}
}
