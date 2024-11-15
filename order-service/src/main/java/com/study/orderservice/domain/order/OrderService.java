package com.study.orderservice.domain.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderSender orderSender;

	// TODO ProductInfo 수정

	/**
	 * <h1>주문 프로세스</h1>
	 * <ol>
	 *     <li>주문생성</li>
	 *     <li>주문 아이템 생성</li>
	 *     <li>결제 생성</li>
	 * </ol>
	 */
	// @Transactional
	// public Long order(OrderCommand.Order command, Map<Long, ProductInfo.Data> productPriceMap) {
	// 	Long orderId = orderRepository.save(command.toOrder()).getId();
	// 	long sumPrice = orderRepository.saveItemAll(
	// 		command.toOrderItem(orderId, productPriceMap)
	// 	).stream().mapToLong(v -> v.getAmount() * v.getPrice()).sum();
	// 	orderRepository.savePayment(
	// 		command.toPayment(orderId, sumPrice)
	// 	);
	// 	return orderId;
	// 	return 1L;
	// }
	public void sendEvent(OrderCommand.SendData sendData) {
		orderSender.sendEvent(sendData.orderId(), sendData.totalPrice());
	}
}
