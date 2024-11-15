package com.study.userservice.infra.cart;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.userservice.domain.cart.Cart;
import com.study.userservice.domain.cart.CartRepository;
import com.study.userservice.domain.cart.QCart;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {

	private final JPAQueryFactory queryFactory;
	private final CartJpaRepository cartJpaRepository;

	@Override
	public List<Cart> getListByUserId(String userId) {
		var qCart = QCart.cart;
		return queryFactory.selectFrom(qCart)
			.where(qCart.userId.eq(userId))
			.fetch();
	}

	@Override
	public Cart save(Cart cart) {
		return cartJpaRepository.save(cart);
	}

	@Override
	public void deleteById(Long id) {
		cartJpaRepository.deleteById(id);
	}

}
