package com.study.orderservice.domain.order;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.orderservice.domain.product.ProductInfo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	/**
	 * <h1>주문 프로세스</h1>
	 * <ol>
	 *     <li>주문생성</li>
	 *     <li>주문 아이템 생성</li>
	 *     <li>결제 생성</li>
	 * </ol>
	 */
	public Long order(OrderCommand.Order command, Map<Long, ProductInfo.Data> productPriceMap) {
		Long orderId = orderRepository.save(command.toOrder()).getId();
		long sumPrice = orderRepository.saveItemAll(
			command.toOrderItem(orderId, productPriceMap)
		).stream().mapToLong(v -> v.getAmount() * v.getPrice()).sum();
		orderRepository.savePayment(
			command.toPayment(orderId, sumPrice)
		);
		return orderId;
	}

	/**
	 * <h1>상위 상품 조회</h1>
	 * <ul>
	 *     <li>쿼리를 통해 group by 와 limit 를 사용해서 상위 주문 수량을 집계하였다.</li>
	 * </ul>
	 */
	@Transactional(readOnly = true)
	public List<OrderResult.GroupByProduct> getGroupByOrders(
		OrderCommand.Search command
	) {
		return orderRepository.getOrderAmountByOrderDateAndLimit(command);
	}

}
