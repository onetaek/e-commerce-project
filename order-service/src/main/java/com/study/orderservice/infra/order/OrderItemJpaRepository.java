package com.study.orderservice.infra.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.orderservice.domain.order.OrderItem;

public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {
}
