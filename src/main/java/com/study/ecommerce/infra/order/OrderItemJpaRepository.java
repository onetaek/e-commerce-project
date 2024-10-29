package com.study.ecommerce.infra.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.order.OrderItem;

public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {
}
