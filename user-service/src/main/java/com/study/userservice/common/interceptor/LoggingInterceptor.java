package com.study.userservice.common.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
		log.info("=== Request Logging Start ===");
		log.info("Timestamp: {}", currentTime);
		log.info("Request URL: {}", request.getRequestURL());
		log.info("HTTP Method: {}", request.getMethod());
		log.info("Remote Address: {}", request.getRemoteAddr());
		log.info("Protocol: {}", request.getProtocol());

		// 헤더 로깅
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			log.info("Header - {}: {}", headerName, request.getHeader(headerName));
		}

		// 파라미터 로깅
		Map<String, String[]> parameterMap = request.getParameterMap();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String paramName = entry.getKey();
			String[] paramValues = entry.getValue();
			log.info("Parameter - {}: {}", paramName, String.join(", ", paramValues));
		}

		// 요청 본문 로깅 (Post, Put일 경우)
//		if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
//			try (BufferedReader reader = request.getReader()) {
//				StringBuilder body = new StringBuilder();
//				String line;
//				while ((line = reader.readLine()) != null) {
//					body.append(line);
//				}
//				log.info("Request Body: {}", body);
//			} catch (IOException e) {
//				log.error("Failed to read the request body", e);
//			}
//		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
		org.springframework.web.servlet.ModelAndView modelAndView) {
		// 필요시 추가 작업 가능
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
		Exception ex) {
		String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
		log.info("Timestamp: {}", currentTime);
		log.info("Response Status: {}", response.getStatus());
		log.info("=== Request Logging End ===");
	}
}
