package com.study.ecommerce.domain.balance.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.balance.Balance;
import com.study.ecommerce.domain.balance.BalanceException;
import com.study.ecommerce.infra.balance.BalanceQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BalanceValidator {

	private final BalanceQueryRepository balanceQueryRepository;

	public void validateAmount(String userId, long amount) {
		Balance balance = balanceQueryRepository.getOne(userId).orElseGet(() ->
			Balance.builder()
				.amount(0L)
				.userId(userId)
				.build());

		if (balance.getAmount() < amount) {
			throw BalanceException.exceed();
		}
	}

}
