package com.study.ecommerce.application.cart;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.cart.dto.CartDetailInfo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartQueryService {
	public CartDetailInfo getCart(String userId) {
		return null;
	}
}
