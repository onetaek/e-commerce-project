package com.study.ecommerce.infra.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.cart.CartItem;

public interface CartItemCommandRepository extends JpaRepository<CartItem, Long> {
}
