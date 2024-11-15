package com.study.userservice.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class RollbackTriggeredException extends RuntimeException {
	private final String message;
	private final HttpStatus code;

	public RollbackTriggeredException(String message, HttpStatus code) {
		this.message = message;
		this.code = code;
	}
}
