package com.study.ecommerce.domain.order;

import lombok.Getter;

@Getter
public enum PaymentStatus {

	STANDBY("대기", 0, ""),
	COMPLETE("완료", 10, "");

	private final String code;
	private final String displayValue;
	private final Integer sequence;
	private final String description;

	PaymentStatus(String displayValue, Integer sequence, String description) {
		this.code = this.name();
		this.displayValue = displayValue;
		this.sequence = sequence;
		this.description = description;
	}
}
