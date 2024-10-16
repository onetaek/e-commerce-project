package com.study.ecommerce.domain.product.dto;

public record ProductDetailInfo(
	Long id,
	String name,
	Long price,
	int amount
) {
}
