package com.study.ecommerce.domain.order.dto;

import com.study.ecommerce.domain.order.Payment;
import com.study.ecommerce.domain.order.PaymentStatus;

public record PaymentInfo(
	Long id,
	Long orderId,
	Long amount,
	PaymentStatus status
) {
	public static PaymentInfo fromDomain(Payment payment) {
		return new PaymentInfo(
			payment.getId(),
			payment.getOrderId(),
			payment.getAmount(),
			payment.getStatus()
		);
	}
}
