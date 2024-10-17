package com.study.ecommerce.infra.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.order.Order;

public interface OrderCommandRepository extends JpaRepository<Order, Long> {
}
