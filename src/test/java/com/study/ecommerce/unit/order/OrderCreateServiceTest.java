package com.study.ecommerce.unit.order;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.study.ecommerce.domain.order.Order;
import com.study.ecommerce.domain.order.dto.OrderCommand;
import com.study.ecommerce.domain.order.dto.OrderInfo;
import com.study.ecommerce.domain.order.service.OrderCreateService;
import com.study.ecommerce.infra.order.OrderCommandRepository;

@ExtendWith(MockitoExtension.class)
class OrderCreateServiceTest {

	@InjectMocks
	private OrderCreateService orderCreateService;

	@Mock
	private OrderCommandRepository orderCommandRepository;

	@Test
	@DisplayName("주문을 정상적으로 생성할 수 있어야 한다.")
	void testCreateOrder_Success() {
		// given
		String userId = "test-user";
		LocalDateTime orderDate = LocalDateTime.now();
		Long totalPrice = 5000L;

		OrderCommand orderCommand = new OrderCommand(userId, orderDate, totalPrice);
		Order order = orderCommand.toDomain();

		// OrderCommandRepository가 save를 호출하면 order를 반환하도록 설정
		when(orderCommandRepository.save(any(Order.class))).thenReturn(order);

		// when
		OrderInfo result = orderCreateService.create(orderCommand);

		// then
		assertThat(result).isNotNull();
		assertThat(result.userId()).isEqualTo(userId);
		assertThat(result.orderDate()).isEqualTo(orderDate);
		assertThat(result.totalPrice()).isEqualTo(totalPrice);

		// Repository의 save 메서드가 호출되었는지 검증
		verify(orderCommandRepository, times(1)).save(any(Order.class));
	}

	@Test
	@DisplayName("주문 생성 중 저장 오류가 발생하면 예외가 발생해야 한다.")
	void testCreateOrder_Failure_SaveError() {
		// given
		String userId = "test-user";
		LocalDateTime orderDate = LocalDateTime.now();
		Long totalPrice = 5000L;

		OrderCommand orderCommand = new OrderCommand(userId, orderDate, totalPrice);

		// Repository가 save를 호출할 때 예외를 던지도록 설정
		when(orderCommandRepository.save(any(Order.class))).thenThrow(new RuntimeException("DB 저장 오류"));

		// when & then
		assertThatThrownBy(() -> orderCreateService.create(orderCommand))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("DB 저장 오류");

		// Repository의 save 메서드가 호출되었는지 검증
		verify(orderCommandRepository, times(1)).save(any(Order.class));
	}
}