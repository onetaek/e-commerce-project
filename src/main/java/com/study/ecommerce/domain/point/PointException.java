package com.study.ecommerce.domain.point;

import org.springframework.http.HttpStatus;

import com.study.ecommerce.common.exception.RollbackTriggeredException;

public class PointException extends RollbackTriggeredException {

	public PointException(String message, HttpStatus code) {
		super(message, code);
	}

	public static PointException exceed() {
		return new PointException(
			"잔액을 초과하였습니다.",
			HttpStatus.BAD_REQUEST
		);
	}

	public static PointException notFound() {
		return new PointException(
			"잔액정보를 찾을 수 없습니다.",
			HttpStatus.NOT_FOUND
		);
	}
}