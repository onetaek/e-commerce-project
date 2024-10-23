package com.study.ecommerce.application.cart;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.cart.service.CartManager;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartRemoveService {

	private final CartManager cartManager;

	public void remove(Long id) {

	}
}
