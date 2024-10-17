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
public class BalanceChargeService {

	private final BalanceCommandRepository balanceCommandRepository;
	private final BalanceQueryRepository balanceQueryRepository;

	public BalanceInfo charge(BalanceChargeCommand command) {
		// TODO: 토큰을 도입 후 토큰의 값을 사용하도록 수정
		var userId = command.userId();

		// 유저가 최조로 충전하는 경우 balance 객체가 없기 때문에 객체를 생성한다.
		var balance = balanceQueryRepository.getOne(userId)
			.orElseGet(() -> Balance.builder()
				.userId(userId)
				.amount(0L)
				.build());

		// 잔액을 충전 한다. -> 최초 충전일 경우 Dirty Checking 이 동작하지 않기 때문에 save
		balance.add(command.amount());
		balanceCommandRepository.save(balance);

		return BalanceInfo.fromDomain(balance);
	}

}
