package com.study.orderservice.common.config;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

	@Value("${web-client.base-url}")
	private String baseUrl;

	@Bean
	public WebClient customWebClient() {
		HttpClient httpClient = HttpClient.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000) // 연결 타임아웃 설정
			.responseTimeout(Duration.ofMillis(30000)) // 응답 타임아웃 설정
			.doOnConnected(conn ->
				conn.addHandlerLast(new ReadTimeoutHandler(30000, TimeUnit.MILLISECONDS))
					.addHandlerLast(new WriteTimeoutHandler(30000, TimeUnit.MILLISECONDS)));

		return WebClient.builder()
			.baseUrl(baseUrl) // 기본 URL 설정
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // 기본 헤더 설정
			.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE) // Accept 헤더 설정
			.clientConnector(new ReactorClientHttpConnector(httpClient)) // 커넥터 설정
			.build();
	}

}
