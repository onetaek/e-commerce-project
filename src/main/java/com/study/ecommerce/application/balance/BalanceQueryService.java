package com.study.ecommerce.application.balance;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.balance.dto.BalanceInfo;
import com.study.ecommerce.domain.balance.service.BalanceReader;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BalanceQueryService {

	private final BalanceReader balanceReader;

	public BalanceInfo getOne(String userId) {
		return balanceReader.getOne(userId);
	}

}
