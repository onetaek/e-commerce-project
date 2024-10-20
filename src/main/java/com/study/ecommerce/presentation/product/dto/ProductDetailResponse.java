package com.study.ecommerce.presentation.product.dto;

import java.util.List;

import com.study.ecommerce.domain.product.dto.ProductDetailInfo;

public record ProductDetailResponse(
	Long id,
	String name,
	Long price,
	int amount
) {
	public static List<ProductDetailResponse> from(List<ProductDetailInfo> products) {
		return products.stream()
			.map(product -> new ProductDetailResponse(
				product.id(),
				product.name(),
				product.price(),
				product.amount()
			)).toList();
	}
}
