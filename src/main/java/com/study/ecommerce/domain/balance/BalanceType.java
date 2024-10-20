package com.study.ecommerce.domain.balance;

import lombok.Getter;

@Getter
public enum BalanceType {

	CHARGE("충전", 0, ""),
	USE("사용", 10, "");

	private final String code;
	private final String displayValue;
	private final Integer sequence;
	private final String description;

	BalanceType(String displayValue, Integer sequence, String description) {
		this.code = this.name();
		this.displayValue = displayValue;
		this.sequence = sequence;
		this.description = description;
	}
}
