package com.study.ecommerce.presentation.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.ecommerce.domain.order.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	/**
	 * 주문과 결제를 수행한다.
	 * @param request 주문정보
	 * @return 주문 정보
	 */
	@PostMapping
	public ResponseEntity<Void> order(@RequestBody OrderDto.Request request) {
		orderService.order(request.toCommand());
		return ResponseEntity.ok().build();
	}
}
