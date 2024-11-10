package com.study.ecommerce.domain.product;

import java.time.LocalDateTime;

public class ProductCommand {

	public record Search(
		LocalDateTime fromOrderDate,
		LocalDateTime toOrderDate,
		Long limit
	){}
}
