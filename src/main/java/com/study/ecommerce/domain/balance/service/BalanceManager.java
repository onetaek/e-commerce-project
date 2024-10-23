package com.study.ecommerce.domain.balance.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.balance.Balance;
import com.study.ecommerce.domain.balance.dto.BalanceChargeCommand;
import com.study.ecommerce.domain.balance.dto.BalanceInfo;
import com.study.ecommerce.infra.balance.BalanceCommandRepository;
import com.study.ecommerce.infra.balance.BalanceQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BalanceManager {

	private final BalanceCommandRepository balanceCommandRepository;
	private final BalanceQueryRepository balanceQueryRepository;

	public BalanceInfo charge(BalanceChargeCommand command) {
		var userId = command.userId();

		var balance = balanceQueryRepository.getOne(userId)
			.orElseGet(() -> Balance.builder()
				.userId(userId)
				.amount(0L)
				.build());

		balance.add(command.amount());
		balanceCommandRepository.save(balance);

		return BalanceInfo.fromDomain(balance);
	}

}
