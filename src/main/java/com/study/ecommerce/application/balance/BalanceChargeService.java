package com.study.ecommerce.application.balance;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.balance.BalanceType;
import com.study.ecommerce.domain.balance.dto.BalanceChargeCommand;
import com.study.ecommerce.domain.balance.dto.BalanceHistoryAddCommand;
import com.study.ecommerce.domain.balance.dto.BalanceInfo;
import com.study.ecommerce.domain.balance.service.BalanceHistoryManager;
import com.study.ecommerce.domain.balance.service.BalanceManager;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BalanceChargeService {

	private final BalanceManager balanceManager;
	private final BalanceHistoryManager balanceHistoryManager;

	public BalanceInfo charge(BalanceChargeCommand command) {
		var balanceInfo = balanceManager.charge(command);
		balanceHistoryManager.add(
			new BalanceHistoryAddCommand(
				balanceInfo.id(),
				command.amount(),
				BalanceType.CHARGE
			)
		);
		return balanceInfo;
	}

}
