package com.study.ecommerce.presentation.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.ecommerce.application.OrderFacade;
import com.study.ecommerce.presentation.order.dto.OrderAndPaymentCommand;
import com.study.ecommerce.presentation.order.dto.OrderResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderFacade orderFacade;

	@PostMapping
	public ResponseEntity<OrderResponse> order(@RequestBody OrderAndPaymentCommand request) {
		return ResponseEntity.ok().body(
			OrderResponse.fromInfo(orderFacade.order(request))
		);
	}
}
