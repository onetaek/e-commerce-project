package com.study.ecommerce.domain.balance.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.balance.dto.BalanceHistoryAddCommand;
import com.study.ecommerce.infra.balance.BalanceHistoryCommandRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BalanceHistoryManager {

	private final BalanceHistoryCommandRepository balanceHistoryCommandRepository;

	public void add(BalanceHistoryAddCommand command) {
		balanceHistoryCommandRepository.save(command.toDomain());
	}
}
