package com.study.ecommerce.infra.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.order.Payment;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
}
