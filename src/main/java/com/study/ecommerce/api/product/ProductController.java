package com.study.ecommerce.api.product;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import openapi.api.ProductApi;
import openapi.model.PopularProductsResponseInner;
import openapi.model.ProductsResponseInner;

@RestController
public class ProductController implements ProductApi {
	@Override
	public Optional<NativeWebRequest> getRequest() {
		return ProductApi.super.getRequest();
	}

	@Override
	public ResponseEntity<List<PopularProductsResponseInner>> getPopularProducts() {
		return ProductApi.super.getPopularProducts();
	}

	@Override
	public ResponseEntity<List<ProductsResponseInner>> getProducts() {
		return ProductApi.super.getProducts();
	}
}
