package com.study.orderservice.application;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.study.orderservice.common.annotation.Facade;
import com.study.orderservice.domain.order.OrderCommand;
import com.study.orderservice.domain.order.OrderService;
import com.study.orderservice.domain.product.ProductInfo;
import com.study.orderservice.domain.product.ProductService;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class OrderFacade {

	private final ProductService productService;
	private final OrderService orderService;

	/**
	 * <h1>주문 프로세스</h1>
	 * <ol>
	 *     <li>주문, 결제 생성</li>
	 *     <li>상품 재고 차감</li>
	 *     <li>사용자 포인트 차감</li>
	 *     <li>외부 데이터 플랫폼에 전송</li>
	 * </ol>
	 */
	@Transactional
	public Long order(OrderCommand.Order command) {

		// 제품 서비스에 제품 정보 조회
		Map<Long, ProductInfo.Data> productMap = productService.getProductList(
				command.products().stream().map(OrderCommand.Order.Product::productId).toArray(Long[]::new)
			).stream()
			.collect(Collectors.toMap(
					ProductInfo.Data::id,
					v -> new ProductInfo.Data(v.id(), v.name(), v.price())
				)
			);

		// 주문, 결재 생성
		Long orderId = orderService.order(command, productMap);

		// TODO: Outbox 패턴으로 kafka 메시지 발생

		return orderId;
	}
}
