package com.study.orderservice.application;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.study.orderservice.common.annotation.Facade;
import com.study.orderservice.domain.order.OrderCommand;
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
	/**
	 * <h1>인기 상품 조회</h1>
	 * <ul>
	 *     <li>order, orderItems 를 조인, group by 하여 인기 주문 정보를 조회한다.</li>
	 *     <li>위 로직으로 가져온 데이터에는 productId는 있지만 다른 정보들은 없다.</li>
	 *     <li>따라서 상품 서비스에 GET 요청을 하여 name, price 정보를 매핑시켜준다.</li>
	 * </ul>
	 */
	// @Cacheable(value = CacheConstants.POPULAR_PRODUCTS_CACHE, key = CacheConstants.RECENT_3_DAY_TOP_5_KEY)
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
}
