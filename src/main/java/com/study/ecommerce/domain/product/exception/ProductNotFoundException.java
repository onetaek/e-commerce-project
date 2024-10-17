package com.study.ecommerce.domain.product.exception;

import com.study.ecommerce.common.exception.RollbackTriggeredException;

public class ProductNotFoundException extends RollbackTriggeredException {

	private static final String MESSAGE = "상품을 찾을 수 없습니다.";

	public ProductNotFoundException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 404;
	}
}
