package com.study.ecommerce.common.aop;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.study.ecommerce.common.exception.ErrorResponse;
import com.study.ecommerce.common.exception.RollbackTriggeredException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvisor {

	/**
	 * Spring Validation Exception
	 */
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

	/**
	 * custom Exception
	 */
	@ExceptionHandler(RollbackTriggeredException.class)
	public ErrorResponse rollBackException(RollbackTriggeredException e) {
		return ErrorResponse.builder()
			.code(e.getCode().toString())
			.message(e.getMessage())
			.build();
	}

}
