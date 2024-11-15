package com.study.userservice.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.study.userservice.common.interceptor.EncodingInterceptor;
import com.study.userservice.common.interceptor.LoggingInterceptor;

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
