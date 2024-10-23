package com.study.ecommerce.presentation.cart.dto;

public record CartAddRequest(
	String userId,
	String productId,
	Long amount
) {
}
