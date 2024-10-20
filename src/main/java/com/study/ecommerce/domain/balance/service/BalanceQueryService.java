package com.study.ecommerce.domain.balance.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.balance.BalanceException;
import com.study.ecommerce.domain.balance.dto.BalanceInfo;
import com.study.ecommerce.infra.balance.BalanceQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BalanceQueryService {

	private final BalanceQueryRepository balanceQueryRepository;

	/**
	 * 잔액 조회
	 */
	public BalanceInfo getOne(String userId) {
		return BalanceInfo.fromDomain(
			balanceQueryRepository.getOne(userId).orElseThrow(BalanceException::notFound)
		);
	}
}
