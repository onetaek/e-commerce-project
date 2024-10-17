package com.study.ecommerce.unit.balance;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.study.ecommerce.domain.balance.Balance;
import com.study.ecommerce.domain.balance.exception.BalanceAmountExceedException;
import com.study.ecommerce.domain.balance.service.BalanceValidationService;
import com.study.ecommerce.infra.balance.BalanceQueryRepository;

@ExtendWith(MockitoExtension.class)
class BalanceValidationServiceTest {

	@InjectMocks
	private BalanceValidationService balanceValidationService;

	@Mock
	private BalanceQueryRepository balanceQueryRepository;

	@Test
	@DisplayName("잔액이 충분하면 예외가 발생하지 않고 검증을 통과해야 한다.")
	void testValidateAmount_Success() {
		// given
		String userId = "test-user";
		long amountToValidate = 500L;

		Balance balance = Balance.builder()
			.userId(userId)
			.amount(1000L)
			.build();

		when(balanceQueryRepository.getOne(userId)).thenReturn(Optional.of(balance));

		// when & then
		balanceValidationService.validateAmount(userId, amountToValidate);

		// 잔액이 충분하므로 예외가 발생하지 않음
		verify(balanceQueryRepository, times(1)).getOne(userId);
	}

	@Test
	@DisplayName("잔액이 부족하면 BalanceAmountExceedException 예외가 발생해야 한다.")
	void testValidateAmount_Failure_InsufficientBalance() {
		// given
		String userId = "test-user";
		long amountToValidate = 1500L;

		Balance balance = Balance.builder()
			.userId(userId)
			.amount(1000L)
			.build();

		when(balanceQueryRepository.getOne(userId)).thenReturn(Optional.of(balance));

		// when & then
		assertThatThrownBy(() -> balanceValidationService.validateAmount(userId, amountToValidate))
			.isInstanceOf(BalanceAmountExceedException.class);

		// 잔액이 부족하므로 예외 발생
		verify(balanceQueryRepository, times(1)).getOne(userId);
	}

	@Test
	@DisplayName("잔액이 없는 사용자일 경우, 잔액이 0으로 처리되고 예외가 발생해야 한다.")
	void testValidateAmount_Failure_NoBalance() {
		// given
		String userId = "new-user";
		long amountToValidate = 500L;

		when(balanceQueryRepository.getOne(userId)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> balanceValidationService.validateAmount(userId, amountToValidate))
			.isInstanceOf(BalanceAmountExceedException.class);

		// 잔액이 없으므로 예외 발생
		verify(balanceQueryRepository, times(1)).getOne(userId);
	}
}