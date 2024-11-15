package com.study.orderservice.infra.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.orderservice.domain.order.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
