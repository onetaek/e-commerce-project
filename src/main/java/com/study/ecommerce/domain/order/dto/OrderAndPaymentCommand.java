package com.study.ecommerce.domain.order.dto;

import java.time.LocalDateTime;

public record OrderAndPaymentCommand(
	String userId,
	LocalDateTime orderDate,
	Long productId,
	int amount,
	int price
) {
}
