package com.study.ecommerce.application;

import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.common.annotation.Facade;
import com.study.ecommerce.domain.order.OrderCommand;
import com.study.ecommerce.domain.order.OrderService;
import com.study.ecommerce.domain.point.PointService;
import com.study.ecommerce.domain.product.ProductService;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class OrderFacade {

	private final ProductService productService;
	private final PointService pointService;
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
		var productInfoMap = productService.getProductMap(
			command.products().stream().map(OrderCommand.Order.Product::productId).toArray(Long[]::new)
		);

		// 주문, 결재 생성
		var orderId = orderService.order(command, productInfoMap);
		// 상품 재고 차감
		productService.deduct(command);
		// 사용자 포인트 차감
		var totalPrice = pointService.use(command, productInfoMap);
		// 외부 데이터 플랫폼에 전송
		orderService.sendEvent(new OrderCommand.SendData(orderId, totalPrice));

		return orderId;
	}
}
