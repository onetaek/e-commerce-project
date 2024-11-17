package com.study.orderservice.presentation;

import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.orderservice.domain.product.ProductInfo;
import com.study.orderservice.infra.product.ProductHttpClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/health-check")
@RequiredArgsConstructor
public class HealthCheckController {

	private final Environment env;
	private final ProductHttpClient productHttpClient;

	@GetMapping
	public String check() {
		return String.format("It's Working in Order Service"
			+ ", port(server.port)=" + env.getProperty("server.port"));
	}

	@GetMapping("test")
	public void get() {
		List<ProductInfo.Data> productList = productHttpClient.getProductList(new Long[] {1L, 2L, 3L});
		System.out.println("productList = " + productList);
	}
}
