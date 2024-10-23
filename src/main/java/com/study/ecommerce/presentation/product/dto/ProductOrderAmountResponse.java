package com.study.ecommerce.presentation.product.dto;

import java.util.List;

import com.study.ecommerce.domain.product.dto.ProductOrderAmountInfo;

public record ProductOrderAmountResponse(
	Long productId,
	String productName,
	Long productPrice,
	Long orderAmount
) {
	public static List<ProductOrderAmountResponse> from(List<ProductOrderAmountInfo> infos) {
		return infos.stream()
			.map(info -> new ProductOrderAmountResponse(
				info.productId(),
				info.productName(),
				info.productPrice(),
				info.orderAmount()
			)).toList();
	}
}
