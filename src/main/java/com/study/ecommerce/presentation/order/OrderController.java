package com.study.ecommerce.presentation.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.ecommerce.application.order.OrderAndPaymentService;
import com.study.ecommerce.presentation.order.dto.OrderAndPaymentCommand;
import com.study.ecommerce.presentation.order.dto.OrderResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderAndPaymentService orderAndPaymentService;

	/**
	 * 주문과 결제를 수행한다.
	 * @param request 주문정보
	 * @return 주문 정보
	 */
	@PostMapping
	public ResponseEntity<OrderResponse> order(@RequestBody OrderAndPaymentCommand request) {
		return ResponseEntity.ok().body(
			OrderResponse.fromInfo(orderAndPaymentService.order(request))
		);
	}
}
