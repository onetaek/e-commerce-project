package com.study.userservice.common.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.study.userservice.common.exception.CustomAsyncExceptionHandler;
import com.study.userservice.common.exception.ErrorMessageSender;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {

	private final ErrorMessageSender errorMessageSender;

	// 비동기 작업을 처리할 Executor 설정
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);    // 기본 스레드 수
		executor.setMaxPoolSize(10);   // 최대 스레드 수
		executor.setQueueCapacity(25); // 큐 크기
		executor.setThreadNamePrefix("Async-"); // 스레드 이름 접두사
		executor.initialize();
		return executor;
	}

	// 비동기 예외 처리 핸들러 설정
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new CustomAsyncExceptionHandler(errorMessageSender);

	}
}
