package com.study.orderservice.domain.order;

import java.util.List;

public interface OrderRepository {

	Order save(Order order);

	List<OrderItem> saveItemAll(List<OrderItem> orderItem);

	Payment savePayment(Payment payment);

	List<OrderResult.GroupByProduct> getOrderAmountByOrderDateAndLimit(OrderCommand.Search command);

	List<OrderItem> getOrderItemsByOrderId(long orderId);

	void remove(long orderId);

	void removeOrderItem(long orderId);

	void removePayment(long orderId);
}
