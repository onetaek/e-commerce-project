package com.study.ecommerce.domain.balance;

import com.study.ecommerce.common.exception.RollbackTriggeredException;

public class BalanceNotFoundException extends RollbackTriggeredException {

	private static final String MESSAGE = "잔액정보를 찾을 수 없습니다.";

	public BalanceNotFoundException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 404;
	}
}
