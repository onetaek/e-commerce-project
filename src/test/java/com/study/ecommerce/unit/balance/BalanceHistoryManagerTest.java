package com.study.ecommerce.unit.balance;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.study.ecommerce.domain.balance.BalanceHistory;
import com.study.ecommerce.domain.balance.BalanceType;
import com.study.ecommerce.domain.balance.dto.BalanceHistoryAddCommand;
import com.study.ecommerce.domain.balance.service.BalanceHistoryManager;
import com.study.ecommerce.infra.balance.BalanceHistoryCommandRepository;

@ExtendWith(MockitoExtension.class)
class BalanceHistoryManagerTest {

	@InjectMocks
	private BalanceHistoryManager balanceHistoryManager;

	@Mock
	private BalanceHistoryCommandRepository balanceHistoryCommandRepository;

	@Test
	@DisplayName("잔액 이력 기록을 정상적으로 저장할 수 있어야 한다.")
	void testAdd_BalanceHistory() {
		// given
		Long balanceId = 1L;
		long amount = 500L;
		BalanceType balanceType = BalanceType.CHARGE;

		BalanceHistoryAddCommand command = new BalanceHistoryAddCommand(balanceId, amount, balanceType);
		BalanceHistory balanceHistory = command.toDomain();

		// when
		balanceHistoryManager.add(command);

		// then
		verify(balanceHistoryCommandRepository, times(1)).save(eq(balanceHistory));
	}
}