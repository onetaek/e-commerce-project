package com.study.ecommerce.domain.balance.dto;

import com.study.ecommerce.domain.balance.BalanceHistory;
import com.study.ecommerce.domain.balance.BalanceType;

public record BalanceHistoryAddCommand(
	Long balanceId,
	long amount,
	BalanceType balanceType
) {
	public BalanceHistory toDomain() {
		return BalanceHistory.builder()
			.balanceId(balanceId)
			.amount(amount)
			.type(balanceType)
			.build();
	}
}
