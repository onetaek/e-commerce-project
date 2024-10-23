package com.study.ecommerce.unit.balance;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.study.ecommerce.domain.balance.Balance;
import com.study.ecommerce.domain.balance.dto.BalanceChargeCommand;
import com.study.ecommerce.domain.balance.dto.BalanceInfo;
import com.study.ecommerce.domain.balance.service.BalanceManager;
import com.study.ecommerce.infra.balance.BalanceCommandRepository;
import com.study.ecommerce.infra.balance.BalanceQueryRepository;

@ExtendWith(MockitoExtension.class)
class BalanceManagerTest {

	@InjectMocks
	private BalanceManager balanceManager;

	@Mock
	private BalanceCommandRepository balanceCommandRepository;

	@Mock
	private BalanceQueryRepository balanceQueryRepository;

	@Test
	@DisplayName("존재하는 사용자 ID에 대해 잔액을 정상적으로 충전할 수 있어야 한다.")
	void testCharge_ExistingUser() {
		// given
		String userId = "test-user";
		Long initialAmount = 1000L;
		long chargeAmount = 500L;

		Balance balance = Balance.builder()
			.id(1L)
			.userId(userId)
			.amount(initialAmount)
			.build();

		BalanceChargeCommand command = new BalanceChargeCommand(userId, chargeAmount);

		when(balanceQueryRepository.getOne(userId)).thenReturn(Optional.of(balance));

		// when
		BalanceInfo result = balanceManager.charge(command);

		// then
		assertThat(result.userId()).isEqualTo(userId);
		assertThat(result.amount()).isEqualTo(1500L);

		verify(balanceCommandRepository, times(1)).save(balance);
	}

	@Test
	@DisplayName("존재하지 않는 사용자 ID에 대해 잔액을 처음으로 충전할 수 있어야 한다.")
	void testCharge_NewUser() {
		// given
		String userId = "new-user";
		Long chargeAmount = 1000L;

		BalanceChargeCommand command = new BalanceChargeCommand(userId, chargeAmount);

		when(balanceQueryRepository.getOne(userId)).thenReturn(Optional.empty());

		// when
		BalanceInfo result = balanceManager.charge(command);

		// then
		assertThat(result.userId()).isEqualTo(userId);
		assertThat(result.amount()).isEqualTo(chargeAmount);

		verify(balanceCommandRepository, times(1)).save(any(Balance.class));
	}

}