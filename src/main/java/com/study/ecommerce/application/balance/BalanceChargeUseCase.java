package com.study.ecommerce.application.balance;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.balance.BalanceType;
import com.study.ecommerce.domain.balance.dto.BalanceChargeCommand;
import com.study.ecommerce.domain.balance.dto.BalanceHistoryAddCommand;
import com.study.ecommerce.domain.balance.dto.BalanceInfo;
import com.study.ecommerce.domain.balance.service.BalanceChargeService;
import com.study.ecommerce.domain.balance.service.BalanceHistoryAddService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BalanceChargeUseCase {

	private final BalanceChargeService balanceChargeService;
	private final BalanceHistoryAddService balanceHistoryAddService;

	public BalanceInfo charge(BalanceChargeCommand command) {
		var balanceInfo = balanceChargeService.charge(command);
		balanceHistoryAddService.add(
			new BalanceHistoryAddCommand(
				balanceInfo.id(),
				command.amount(),
				BalanceType.CHARGE
			)
		);
		return balanceInfo;
	}

}
