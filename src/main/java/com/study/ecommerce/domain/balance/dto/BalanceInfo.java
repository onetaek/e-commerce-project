package com.study.ecommerce.domain.balance.dto;

import com.study.ecommerce.domain.balance.Balance;

public record BalanceInfo(
	Long id,
	Long amount,
	String userId
) {

	public static BalanceInfo fromDomain(Balance domain) {
		return new BalanceInfo(
			domain.getId(),
			domain.getAmount(),
			domain.getUserId()
		);
	}
}
