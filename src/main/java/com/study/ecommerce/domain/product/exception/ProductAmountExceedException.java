package com.study.ecommerce.domain.product.exception;

import com.study.ecommerce.common.exception.RollbackTriggeredException;

public class ProductAmountExceedException extends RollbackTriggeredException {
	private static final String MESSAGE = "상품 재고가 부족합니다.";

	public ProductAmountExceedException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 404;
	}
}
