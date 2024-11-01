package com.study.ecommerce.presentation.order;

import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.ecommerce.domain.order.OrderService;

import lombok.RequiredArgsConstructor;

@Slf4j
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
		try {
			orderService.order(request.toCommand());
		} catch (OptimisticLockingFailureException e) {
			log.error("비관적락 예외 발생!",e);
		}
		return ResponseEntity.ok().build();
	}
}
