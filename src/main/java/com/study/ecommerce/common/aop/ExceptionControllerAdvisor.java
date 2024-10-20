package com.study.ecommerce.common.aop;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.study.ecommerce.common.exception.ErrorResponse;
import com.study.ecommerce.common.exception.RollbackTriggeredException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvisor {

	private final HttpServletRequest httpServletRequest;

	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
		ErrorResponse response = ErrorResponse.builder()
			.code(HttpStatus.BAD_REQUEST.toString())
			.message("잘못된 요청입니다.")
			.build();

		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
		}

		return response;
	}

	@ExceptionHandler(RollbackTriggeredException.class)
	public ResponseEntity<ErrorResponse> rollBackException(RollbackTriggeredException e) throws IOException {
		if (isAjax(httpServletRequest)) {
			int statusCode = e.getStatusCode();

			ErrorResponse body = ErrorResponse.builder()
				.code(String.valueOf(statusCode))
				.message(e.getMessage())
				.validation(e.getValidation())
				.build();

			return ResponseEntity.status(statusCode)
				.body(body);
		} else {
			throw e;
		}
	}

	private boolean isAjax(HttpServletRequest request) {
		String accept = request.getHeader("accept");
		return accept != null && accept.contains("application/json");
	}

}
