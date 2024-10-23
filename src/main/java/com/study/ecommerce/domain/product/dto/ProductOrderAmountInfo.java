package com.study.ecommerce.domain.product.dto;

public record ProductOrderAmountInfo(
	Long productId,
	String productName,
	Long productPrice,
	Long orderAmount
) {
}
