package com.study.ecommerce.domain.cart.dto;

import java.util.List;

public record CartDetailInfo(
	Long id,
	List<CartItemInfo> cartItemInfoList
) {
}
