package com.study.orderservice.infra.order;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.orderservice.domain.order.Order;
import com.study.orderservice.domain.order.OrderCommand;
import com.study.orderservice.domain.order.OrderItem;
import com.study.orderservice.domain.order.OrderRepository;
import com.study.orderservice.domain.order.OrderResult;
import com.study.orderservice.domain.order.Payment;
import com.study.orderservice.domain.order.QOrder;
import com.study.orderservice.domain.order.QOrderItem;
import com.study.orderservice.domain.order.QPayment;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

	private final OrderJpaRepository orderJpaRepository;
	private final OrderItemJpaRepository orderItemJpaRepository;
	private final PaymentJpaRepository paymentJpaRepository;
	private final JPAQueryFactory queryFactory;

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

	@Override
	public List<OrderResult.GroupByProduct> getOrderAmountByOrderDateAndLimit(OrderCommand.Search command) {
		var qOrder = QOrder.order;
		var qOrderItem = QOrderItem.orderItem;

		return queryFactory.select(Projections.constructor(
					OrderResult.GroupByProduct.class,
					qOrderItem.productId,
					qOrderItem.amount.sum()
				)
			).from(qOrderItem)
			.join(qOrder).on(qOrder.id.eq(qOrderItem.orderId))
			.where(
				qOrder.orderDate.between(
					command.fromOrderDate(),
					command.toOrderDate()
				)
			)
			.groupBy(
				qOrderItem.productId
			)
			.orderBy(qOrderItem.amount.sum().desc())
			.limit(command.limit())
			.fetch();
	}

	@Override
	public List<OrderItem> getOrderItemsByOrderId(long orderId) {
		var qOrderItem = QOrderItem.orderItem;
		return queryFactory.selectFrom(qOrderItem)
			.where(qOrderItem.orderId.eq(orderId))
			.fetch();
	}

	@Override
	public void remove(long orderId) {
		orderJpaRepository.deleteById(orderId);
	}

	@Override
	public void removeOrderItem(long orderId) {
		var qOrderItem = QOrderItem.orderItem;
		queryFactory.delete(qOrderItem)
			.where(qOrderItem.orderId.eq(orderId))
			.execute();
	}

	@Override
	public void removePayment(long orderId) {
		var qPayment = QPayment.payment;
		queryFactory.delete(qPayment)
			.where(qPayment.orderId.eq(orderId))
			.execute();
	}
}
