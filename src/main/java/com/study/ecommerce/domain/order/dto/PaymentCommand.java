package com.study.ecommerce.domain.order.dto;

import com.study.ecommerce.domain.order.Payment;
import com.study.ecommerce.domain.order.PaymentStatus;

public record PaymentCommand(
	Long orderId,
	Long amount,
	PaymentStatus status
) {
	public Payment toDomain() {
		return Payment.builder()
			.orderId(orderId)
			.amount(amount)
			.status(status)
			.build();
	}
}
