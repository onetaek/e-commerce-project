package com.study.ecommerce.domain.order.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.study.ecommerce.domain.order.OrderItem;
import com.study.ecommerce.domain.order.dto.OrderItemCommand;
import com.study.ecommerce.domain.order.dto.OrderItemInfo;
import com.study.ecommerce.infra.order.OrderItemCommandRepository;

@ExtendWith(MockitoExtension.class)
class OrderItemCreateServiceTest {

	@InjectMocks
	private OrderItemCreateService orderItemCreateService;

	@Mock
	private OrderItemCommandRepository orderItemCommandRepository;

	@Test
	@DisplayName("주문 항목을 정상적으로 생성할 수 있어야 한다.")
	void testCreateOrderItem_Success() {
		// given
		Long orderId = 1L;
		Long productId = 2L;
		int amount = 3;
		int price = 1000;

		OrderItemCommand orderItemCommand = new OrderItemCommand(orderId, productId, amount, price);
		OrderItem orderItem = orderItemCommand.toDomain();

		// OrderItemCommandRepository가 save를 호출하면 orderItem을 반환하도록 설정
		when(orderItemCommandRepository.save(any(OrderItem.class))).thenReturn(orderItem);

		// when
		OrderItemInfo result = orderItemCreateService.create(orderItemCommand);

		// then
		assertThat(result).isNotNull();
		assertThat(result.orderId()).isEqualTo(orderId);
		assertThat(result.productId()).isEqualTo(productId);
		assertThat(result.amount()).isEqualTo(amount);
		assertThat(result.price()).isEqualTo(price);

		// Repository의 save 메서드가 호출되었는지 검증
		verify(orderItemCommandRepository, times(1)).save(any(OrderItem.class));
	}

	@Test
	@DisplayName("주문 항목 생성 중 저장 오류가 발생하면 예외가 발생해야 한다.")
	void testCreateOrderItem_Failure_SaveError() {
		// given
		Long orderId = 1L;
		Long productId = 2L;
		int amount = 3;
		int price = 1000;

		OrderItemCommand orderItemCommand = new OrderItemCommand(orderId, productId, amount, price);

		// Repository가 save를 호출할 때 예외를 던지도록 설정
		when(orderItemCommandRepository.save(any(OrderItem.class))).thenThrow(new RuntimeException("DB 저장 오류"));

		// when & then
		assertThatThrownBy(() -> orderItemCreateService.create(orderItemCommand))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("DB 저장 오류");

		// Repository의 save 메서드가 호출되었는지 검증
		verify(orderItemCommandRepository, times(1)).save(any(OrderItem.class));
	}

}