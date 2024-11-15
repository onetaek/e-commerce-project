package com.study.orderservice.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.study.orderservice.common.interceptor.EncodingInterceptor;
import com.study.orderservice.common.interceptor.LoggingInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final EncodingInterceptor encodingInterceptor;
	private final LoggingInterceptor loggingInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//		registry.addInterceptor(encodingInterceptor).addPathPatterns("/**");
		//		registry.addInterceptor(loggingInterceptor).addPathPatterns("/**");
	}
}
