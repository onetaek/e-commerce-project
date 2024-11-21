package com.study.orderservice.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import com.study.orderservice.common.annotation.Facade;
import com.study.orderservice.common.constant.CacheConstants;
import com.study.orderservice.domain.order.OrderCommand;
import com.study.orderservice.domain.order.OrderEventCommand;
import com.study.orderservice.domain.order.OrderEventService;
import com.study.orderservice.domain.order.OrderInfo;
import com.study.orderservice.domain.order.OrderResult;
import com.study.orderservice.domain.order.OrderService;
import com.study.orderservice.domain.product.ProductInfo;
import com.study.orderservice.domain.product.ProductService;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class OrderFacade {

	private final ProductService productService;
	private final OrderService orderService;
	private final OrderEventService orderEventService;

	/**
	 * <h1>주문 로직 + 이벤트 발행</h1>
	 * <p>이벤트가 발행된 후, 아래 세 가지 프로세스가 순차적으로 실행되며 이벤트 발행의 정합성을 보장합니다</p>
	 * <ol>
	 *     <li>Outbox 테이블에 이벤트 기록(BEFORE_COMMIT)</li>
	 *     <li>Kafka 에 메시지 발행(AFTER_COMMIT)</li>
	 *     <li>kafka 의 메시지를 소비하여 Outbox 테이블에 이벤트 상태를 수정</li>
	 * </ol>
	 */
	@Transactional
	public Long order(OrderCommand.OrderCreate command) {

		// 제품 서비스에 제품 정보 조회
		Map<Long, ProductInfo.Data> productMap = productService.getProductList(
				command.products().stream().map(OrderCommand.OrderCreate.Product::productId).toArray(Long[]::new)
			).stream()
			.collect(Collectors.toMap(
					ProductInfo.Data::id,
					v -> new ProductInfo.Data(v.id(), v.name(), v.price())
				)
			);

		// 주문, 결재 생성
		List<OrderResult.Detail> orderedResult = orderService.order(command, productMap);

		// 이벤트 발행
		orderEventService.publishEvent(
			new OrderEventCommand.OrderCreate(
				command.userId(),
				orderedResult.get(0).orderId(),
				orderedResult.stream().map(v ->
					new OrderEventCommand.OrderCreate.OrderItem(
						v.productId(),
						v.amount(),
						v.price()
					)
				).toList()
			)
		);

		return orderedResult.get(0).orderId();
	}

	/**
	 * <h1>인기 상품 조회</h1>
	 * <ul>
	 *     <li>order, orderItems 를 조인, group by 하여 인기 주문 정보를 조회한다.</li>
	 *     <li>위 로직으로 가져온 데이터에는 productId는 있지만 다른 정보들은 없다.</li>
	 *     <li>따라서 상품 서비스에 GET 요청을 하여 name, price 정보를 매핑시켜준다.</li>
	 * </ul>
	 */
	@Cacheable(value = CacheConstants.POPULAR_PRODUCTS_CACHE, key = CacheConstants.RECENT_3_DAY_TOP_5_KEY)
	public List<OrderInfo.Product> getPopularProducts(OrderCommand.Search command) {
		// 주문 정보를 기반으로 집계
		List<OrderResult.GroupByProduct> orderGroupByResult = orderService.getGroupByOrders(command);

		// 상품 정보 조회
		List<ProductInfo.Data> productList = productService.getProductList(
			orderGroupByResult.stream().map(OrderResult.GroupByProduct::productId).toArray(Long[]::new)
		);

		// 위 2개의 데이터를 매핑
		return OrderInfo.Product.from(orderGroupByResult, productList);
	}

	@Transactional
	public void sendOrderProcessedAlert(OrderCommand.OrderSendMessageCreate message) {
		List<OrderResult.Detail> orderItemResult = orderService.getOrderItems(message.orderId());

		// 제품 서비스에 제품 정보 조회
		Map<Long, ProductInfo.Data> productMap = productService.getProductList(
				orderItemResult.stream().map(OrderResult.Detail::productId).toArray(Long[]::new)
			).stream()
			.collect(Collectors.toMap(
					ProductInfo.Data::id,
					v -> new ProductInfo.Data(v.id(), v.name(), v.price())
				)
			);

		// 주문 최종 완료 알림 전송
		orderEventService.sendOrderProcessedAlert(
			OrderEventCommand.SendOrderProcessedAlert.from(
				message.userId(),
				orderItemResult,
				productMap
			)
		);
	}

	public void cancelOrderAndSendErrorMessage(
		OrderCommand.CancelOrder command,
		String errorMessage
	) {
		orderService.cancelOrder(command);
		orderEventService.sendOrderFailureAlert(
			new OrderEventCommand.SendOrderFailureAlert(
				command.userId(),
				errorMessage
			)
		);
	}
}
