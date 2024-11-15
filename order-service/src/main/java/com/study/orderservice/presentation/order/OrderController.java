package com.study.orderservice.presentation.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.orderservice.application.OrderFacade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderFacade orderFacade;

	/**
	 * 주문과 결제를 수행한다.
	 * @param request 주문정보
	 * @return 주문 정보
	 */
	@PostMapping
	public ResponseEntity<Void> order(@RequestBody com.study.orderservice.presentation.order.OrderDto.Request request) {
		orderFacade.order(request.toCommand());
		return ResponseEntity.ok().build();
	}
}
