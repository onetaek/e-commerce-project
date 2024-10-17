package com.study.ecommerce.domain.balance.dto;

public record BalanceChargeCommand(
	String userId,
	long amount
) {
}
