package com.study.userservice.domain.point;

import org.springframework.http.HttpStatus;

import com.study.userservice.common.exception.RollbackTriggeredException;

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

	public static PointException notFound(String userId) {
		return new PointException(
			"잔액정보를 찾을 수 없습니다. userId: " + userId,
			HttpStatus.NOT_FOUND
		);
	}
}