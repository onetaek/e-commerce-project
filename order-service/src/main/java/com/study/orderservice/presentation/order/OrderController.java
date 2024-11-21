package com.study.orderservice.presentation.order;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.orderservice.application.OrderFacade;
import com.study.orderservice.domain.eventbox.OutboxService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderFacade orderFacade;
	private final OutboxService outboxService;

	/**
	 * 주문과 결제를 수행한다.
	 * @param request 주문정보
	 * @return 주문 정보
	 */
	@PostMapping
	public ResponseEntity<Void> order(@RequestBody OrderDto.Request request) {
		orderFacade.order(request.toCommand());
		return ResponseEntity.ok().build();
	}

	/**
	 * 상위 5개 주문
	 *
	 * @return 상품주문수량정보 목록
	 */
	@GetMapping("popular")
	public ResponseEntity<List<OrderDto.OrderAmountResponse>> getPopularProducts(
		OrderDto.PopularRequest request
	) {
		return ResponseEntity.ok().body(
			OrderDto.OrderAmountResponse.from(
				orderFacade.getPopularProducts(request.toCommand())
			)
		);
	}

	@PostMapping("retry")
	public ResponseEntity<Integer> order() {
		return ResponseEntity.ok().body(
			outboxService.retryFailedOutboxes()
		);
	}
}
