package com.study.ecommerce.unit.balance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.study.ecommerce.domain.balance.Balance;
import com.study.ecommerce.domain.balance.dto.BalanceInfo;
import com.study.ecommerce.domain.balance.exception.BalanceNotFoundException;
import com.study.ecommerce.domain.balance.service.BalanceQueryService;
import com.study.ecommerce.infra.balance.BalanceQueryRepository;

@ExtendWith(MockitoExtension.class)
class BalanceQueryServiceTest {

	@InjectMocks
	private BalanceQueryService balanceQueryService;

	@Mock
	private BalanceQueryRepository balanceQueryRepository;

	@Test
	@DisplayName("주어진 사용자 ID에 대한 잔액을 정상적으로 조회할 수 있어야 한다.")
	void testGetOne_Success() {
		// given
		String userId = "test-user";
		Balance balance = Balance.builder()
			.id(1L)
			.userId(userId)
			.amount(1000L)
			.build();

		when(balanceQueryRepository.getOne(anyString())).thenReturn(Optional.of(balance));

		// when
		BalanceInfo result = balanceQueryService.getOne(userId);

		// then
		assertThat(result)
			.extracting(BalanceInfo::id, BalanceInfo::userId, BalanceInfo::amount)
			.containsExactly(1L, userId, 1000L);

		verify(balanceQueryRepository, times(1)).getOne(anyString());
	}

	@Test
	@DisplayName("존재하지 않는 사용자 ID에 대해 잔액을 조회할 때 BalanceNotFoundException 예외가 발생해야 한다.")
	void testGetOne_BalanceNotFoundException() {
		// given
		String userId = "non-existing-user";
		when(balanceQueryRepository.getOne(anyString())).thenReturn(Optional.empty());

		// when & then
		assertThrows(BalanceNotFoundException.class, () -> balanceQueryService.getOne(userId));

		verify(balanceQueryRepository, times(1)).getOne(anyString());
		verifyNoMoreInteractions(balanceQueryRepository);
	}
}