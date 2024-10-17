package com.study.ecommerce.infra.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.order.OrderItem;

public interface OrderItemCommandRepository extends JpaRepository<OrderItem, Long> {
}
