package com.study.ecommerce.presentation.balance.dto;

import com.study.ecommerce.domain.balance.dto.BalanceChargeCommand;

public record BalanceChargeRequest(
	String userId,
	long amount
) {
	public BalanceChargeCommand toCommand() {
		return new BalanceChargeCommand(userId, amount);
	}
}
