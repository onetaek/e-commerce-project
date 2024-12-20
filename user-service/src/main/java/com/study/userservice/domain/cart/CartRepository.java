package com.study.userservice.domain.cart;

import java.util.List;

public interface CartRepository {

	List<Cart> getListByUserId(String userId);

	Cart save(Cart cart);

	void deleteById(Long id);
}
