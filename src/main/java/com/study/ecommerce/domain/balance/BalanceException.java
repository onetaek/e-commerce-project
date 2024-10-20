package com.study.ecommerce.domain.balance;

import org.springframework.http.HttpStatus;

import com.study.ecommerce.common.exception.RollbackTriggeredException;

public class BalanceException extends RollbackTriggeredException {

	public BalanceException(String message, HttpStatus code) {
		super(message, code);
	}

	public static BalanceException exceed() {
		return new BalanceException(
			"잔액을 초과하였습니다.",
			HttpStatus.BAD_REQUEST
		);
	}

	public static BalanceException notFound() {
		return new BalanceException(
			"잔액정보를 찾을 수 없습니다.",
			HttpStatus.NOT_FOUND
		);
	}
}