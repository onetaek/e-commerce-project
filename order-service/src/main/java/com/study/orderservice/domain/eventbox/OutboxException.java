package com.study.orderservice.domain.eventbox;

import org.springframework.http.HttpStatus;

import com.study.orderservice.common.exception.RollbackTriggeredException;

public class OutboxException extends RollbackTriggeredException {
	public OutboxException(String message, HttpStatus code) {
		super(message, code);
	}

	public static OutboxException saveFailed() {
		return new OutboxException(
			"Failed to save outbox",
			HttpStatus.INTERNAL_SERVER_ERROR
		);
	}
}
