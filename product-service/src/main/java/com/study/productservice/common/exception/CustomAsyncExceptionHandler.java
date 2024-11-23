package com.study.productservice.common.exception;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	private final ErrorMessageSender errorMessageSender;

	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {

		if (ex instanceof SendAlertException) {
			errorMessageSender.sendErrorMessage(ex.getMessage());
		}
	}
}
