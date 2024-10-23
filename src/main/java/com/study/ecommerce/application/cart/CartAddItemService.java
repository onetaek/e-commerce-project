package com.study.ecommerce.application.cart;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.presentation.cart.dto.CartAddRequest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartAddItemService {

	public Long add(CartAddRequest request) {
		return null;
	}
}
