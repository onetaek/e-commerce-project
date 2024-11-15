package com.study.userservice.common.aop;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.study.userservice.common.exception.ErrorResponse;
import com.study.userservice.common.exception.RollbackTriggeredException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvisor {

	/**
	 * Spring Validation Exception
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
		log.info("Spring Validation Exception: %s", e);
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
	 * Custom Exception
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(RollbackTriggeredException.class)
	public ErrorResponse rollBackException(RollbackTriggeredException e) {
		log.info("Custom Exception: %s", e);
		return ErrorResponse.builder()
			.code(e.getCode().toString())
			.message(e.getMessage())
			.build();
	}

	/**
	 * DataAccess Exception
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(DataAccessException.class)
	public ErrorResponse dataAccessException(DataAccessException e) {
		log.error("DataAccess Exception: %s", e);
		return ErrorResponse.builder()
			.code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
			.message(e.getMessage())
			.build();
	}

	/**
	 * Unexpected Exception
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResponse unexpectedException(Exception e) {
		log.error("Unexpected Exception: %s", e);
		return ErrorResponse.builder()
			.code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
			.message(e.getMessage())
			.build();
	}

}
