package com.study.ecommerce.api.order;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import openapi.api.OrderApi;
import openapi.model.OrderResponse;
import openapi.model.PlaceOrderRequest;

@RestController
public class OrderController implements OrderApi {
	@Override
	public Optional<NativeWebRequest> getRequest() {
		return OrderApi.super.getRequest();
	}

	@Override
	public ResponseEntity<OrderResponse> placeOrder(PlaceOrderRequest placeOrderRequest) {
		return OrderApi.super.placeOrder(placeOrderRequest);
	}
}
