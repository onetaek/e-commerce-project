package com.study.ecommerce.presentation.cart;


import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import openapi.api.CartApi;
import openapi.model.AddToCartRequest;
import openapi.model.CartResponseInner;

@RestController
public class CartController implements CartApi {
	@Override
	public Optional<NativeWebRequest> getRequest() {
		return CartApi.super.getRequest();
	}

	@Override
	public ResponseEntity<List<CartResponseInner>> addToCart(AddToCartRequest addToCartRequest) {
		return CartApi.super.addToCart(addToCartRequest);
	}

	@Override
	public ResponseEntity<List<CartResponseInner>> getCart(String authorization) {
		return CartApi.super.getCart(authorization);
	}

	@Override
	public ResponseEntity<Void> removeFromCart(Integer cartItemId) {
		return CartApi.super.removeFromCart(cartItemId);
	}
}
