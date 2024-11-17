package com.study.orderservice.infra.product;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.study.orderservice.domain.product.ProductClient;
import com.study.orderservice.domain.product.ProductInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductHttpClient implements ProductClient {

	private final static String PRODUCT_SERVICE = "product-service";

	private final WebClient webClient;

	@Override
	public List<ProductInfo.Data> getProductList(Long... productIds) {
		return webClient.get()
			.uri(uriBuilder -> {
				uriBuilder.path(PRODUCT_SERVICE + "/api/products");
				for (Long productId : productIds) {
					uriBuilder.queryParam("ids", productId);
				}
				return uriBuilder.build();
			})
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<List<ProductInfo.Data>>() {
			}).block();
	}
}
