package com.study.productservice.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class SendAlertException extends RuntimeException {
	private final String message;
	private final HttpStatus code;

	public SendAlertException(String message) {
		this.message = message;
		this.code = HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
