package com.study.orderservice.infra.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.orderservice.domain.order.Payment;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
}
