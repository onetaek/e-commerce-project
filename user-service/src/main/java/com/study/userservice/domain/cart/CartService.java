package com.study.userservice.domain.cart;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;

	/**
	 * <h1>장바구니 조회</h1>
	 * <ul>
	 *     <li>유저 id 를 통해 장바구니 목록 조회</li>
	 * </ul>
	 */
	public List<CartInfo.Info> getList(CartCommand.Search command) {
		return CartInfo.Info.from(
			cartRepository.getListByUserId(command.userId())
		);
	}

	/**
	 * <h1>장바구니 등록</h1>
	 */
	public void add(CartCommand.Add command) {
		cartRepository.save(command.toCart());
	}

	/**
	 * <h1>장바구니 삭제</h1>
	 */
	public void remove(CartCommand.Remove command) {
		cartRepository.deleteById(command.id());
	}
}
