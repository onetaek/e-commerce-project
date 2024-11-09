package com.study.ecommerce.infra.order;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.study.ecommerce.domain.order.Order;
import com.study.ecommerce.domain.order.OrderItem;
import com.study.ecommerce.domain.order.OrderRepository;
import com.study.ecommerce.domain.order.Payment;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

	private final OrderJpaRepository orderJpaRepository;
	private final OrderItemJpaRepository orderItemJpaRepository;
	private final PaymentJpaRepository paymentJpaRepository;

	@Override
	public Order save(Order order) {
		return orderJpaRepository.save(order);
	}

	@Override
	public List<OrderItem> saveItemAll(List<OrderItem> orderItem) {
		return orderItemJpaRepository.saveAll(orderItem);
	}

	@Override
	public Payment savePayment(Payment payment) {
		return paymentJpaRepository.save(payment);
	}
}
