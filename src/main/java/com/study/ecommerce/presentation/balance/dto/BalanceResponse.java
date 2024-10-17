package com.study.ecommerce.presentation.balance.dto;

import com.study.ecommerce.domain.balance.dto.BalanceInfo;

public record BalanceResponse(
	Long id,
	Long amount,
	String userId
) {
	public static BalanceResponse fromInfo(BalanceInfo info) {
		return new BalanceResponse(
			info.id(),
			info.amount(),
			info.userId()
		);
	}
}
