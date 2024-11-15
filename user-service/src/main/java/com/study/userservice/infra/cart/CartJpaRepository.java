package com.study.userservice.infra.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.userservice.domain.cart.Cart;

public interface CartJpaRepository extends JpaRepository<Cart, Long> {
}
