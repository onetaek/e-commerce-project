package com.study.ecommerce.domain.order.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.study.ecommerce.domain.order.Payment;
import com.study.ecommerce.domain.order.PaymentStatus;
import com.study.ecommerce.domain.order.dto.PaymentCommand;
import com.study.ecommerce.domain.order.dto.PaymentInfo;
import com.study.ecommerce.infra.order.PaymentCommandRepository;

@ExtendWith(MockitoExtension.class)
class PaymentCreateServiceTest {

	@InjectMocks
	private PaymentCreateService paymentCreateService;

	@Mock
	private PaymentCommandRepository paymentCommandRepository;

	@Test
	@DisplayName("결제를 정상적으로 생성할 수 있어야 한다.")
	void testCreatePayment_Success() {
		// given
		Long orderId = 1L;
		Long amount = 5000L;
		PaymentStatus status = PaymentStatus.COMPLETE;

		PaymentCommand paymentCommand = new PaymentCommand(orderId, amount, status);
		Payment payment = paymentCommand.toDomain();

		// PaymentCommandRepository가 save를 호출하면 payment를 반환하도록 설정
		when(paymentCommandRepository.save(any(Payment.class))).thenReturn(payment);

		// when
		PaymentInfo result = paymentCreateService.create(paymentCommand);

		// then
		assertThat(result).isNotNull();
		assertThat(result.orderId()).isEqualTo(orderId);
		assertThat(result.amount()).isEqualTo(amount);
		assertThat(result.status()).isEqualTo(status);

		// Repository의 save 메서드가 호출되었는지 검증
		verify(paymentCommandRepository, times(1)).save(any(Payment.class));
	}

	@Test
	@DisplayName("결제 생성 중 저장 오류가 발생하면 예외가 발생해야 한다.")
	void testCreatePayment_Failure_SaveError() {
		// given
		Long orderId = 1L;
		Long amount = 5000L;
		PaymentStatus status = PaymentStatus.COMPLETE;

		PaymentCommand paymentCommand = new PaymentCommand(orderId, amount, status);

		// Repository가 save를 호출할 때 예외를 던지도록 설정
		when(paymentCommandRepository.save(any(Payment.class))).thenThrow(new RuntimeException("DB 저장 오류"));

		// when & then
		assertThatThrownBy(() -> paymentCreateService.create(paymentCommand))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("DB 저장 오류");

		// Repository의 save 메서드가 호출되었는지 검증
		verify(paymentCommandRepository, times(1)).save(any(Payment.class));
	}
}