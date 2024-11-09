package com.study.ecommerce.domain.product;

import org.springframework.http.HttpStatus;

import com.study.ecommerce.common.exception.RollbackTriggeredException;

public class ProductException extends RollbackTriggeredException {

	public ProductException(String message, HttpStatus code) {
		super(message, code);
	}

	public static ProductException notFound() {
		return new ProductException(
			"제품정보를 찾을 수 없습니다.",
			HttpStatus.NOT_FOUND
		);
	}

	public static ProductException inventoryAmountExceed() {
		return new ProductException(
			"재고수량을 초과하였습니다.",
			HttpStatus.BAD_REQUEST
		);
	}
}
