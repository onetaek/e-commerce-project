package com.study.ecommerce.domain.balance.exception;

import com.study.ecommerce.common.exception.RollbackTriggeredException;

public class BalanceAmountExceedException extends RollbackTriggeredException {

	private static final String MESSAGE = "잔액을 초과하였습니다.";

	public BalanceAmountExceedException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 400;
	}
}
