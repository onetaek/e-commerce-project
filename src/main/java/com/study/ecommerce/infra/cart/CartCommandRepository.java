package com.study.ecommerce.infra.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.cart.Cart;

public interface CartCommandRepository extends JpaRepository<Cart, Long> {
}
