package com.study.ecommerce.domain.order;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.product.ProductInfo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderSender orderSender;

	/**
	 * <h1>주문 프로세스</h1>
	 * <ol>
	 *     <li>주문생성</li>
	 *     <li>주문 아이템 생성</li>
	 *     <li>결제 생성</li>
	 * </ol>
	 */
	@Transactional
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

	public void send(OrderCommand.SendData sendData) {
		orderSender.send(sendData.orderId(), sendData.totalPrice());
	}
}
