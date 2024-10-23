package com.study.ecommerce.integration.order;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.application.order.OrderAndPaymentService;
import com.study.ecommerce.domain.balance.Balance;
import com.study.ecommerce.domain.balance.BalanceException;
import com.study.ecommerce.domain.balance.service.BalanceValidator;
import com.study.ecommerce.domain.order.dto.OrderInfo;
import com.study.ecommerce.domain.order.service.OrderItemManager;
import com.study.ecommerce.domain.order.service.OrderManager;
import com.study.ecommerce.domain.order.service.OrderSendManager;
import com.study.ecommerce.domain.order.service.PaymentManager;
import com.study.ecommerce.domain.product.Product;
import com.study.ecommerce.domain.product.ProductException;
import com.study.ecommerce.domain.product.ProductInventory;
import com.study.ecommerce.domain.product.service.ProductValidator;
import com.study.ecommerce.presentation.order.dto.OrderAndPaymentCommand;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
class OrderAndPaymentServiceTest {

	@Autowired
	private OrderAndPaymentService orderAndPaymentService;

	@Autowired
	private BalanceValidator balanceValidator;

	@Autowired
	private ProductValidator productValidator;

	@Autowired
	private OrderManager orderManager;

	@Autowired
	private OrderItemManager orderItemManager;

	@Autowired
	private PaymentManager paymentManager;

	@Autowired
	private OrderSendManager orderSendManager;

	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	void setUp() {
		// 사용자 잔액 설정
		Balance userBalance = Balance.builder()
			.userId("test-user")
			.amount(5000L)
			.build();
		entityManager.persist(userBalance);

		// 상품 설정
		Product product = Product.builder()
			.name("Test Product")
			.price(500L)
			.build();
		entityManager.persist(product);

		// 재고 설정
		ProductInventory productInventory = ProductInventory.builder()
			.productId(product.getId())
			.amount(10)
			.build();
		entityManager.persist(productInventory);

		// 영속성 컨텍스트 비우기
		entityManager.flush();
		entityManager.clear();
	}

	@Test
	@DisplayName("주문이 정상적으로 처리되어야 한다.")
	void testOrder_Success() {
		// given
		String userId = "test-user";
		Long productId = 1L;
		int amount = 2;
		int price = 500;

		OrderAndPaymentCommand command = new OrderAndPaymentCommand(userId, LocalDateTime.now(), productId, amount,
			price);

		// when
		OrderInfo result = orderAndPaymentService.order(command);

		// then
		assertThat(result).isNotNull();
		assertThat(result.userId()).isEqualTo(userId);
		assertThat(result.totalPrice()).isEqualTo((long)amount * price);

		// 서비스들이 정상적으로 호출되었는지 검증
		assertThat(balanceValidator).isNotNull();
		assertThat(productValidator).isNotNull();
		assertThat(orderManager).isNotNull();
		assertThat(orderItemManager).isNotNull();
		assertThat(paymentManager).isNotNull();
		assertThat(orderSendManager).isNotNull();
	}

	@Test
	@DisplayName("재고 부족 시 주문이 실패해야 한다.")
	void testOrder_Failure_InsufficientInventory() {
		// given
		String userId = "test-user";
		Long productId = 1L;
		int amount = 20; // 재고보다 많은 양
		int price = 500;

		OrderAndPaymentCommand command = new OrderAndPaymentCommand(userId, LocalDateTime.now(), productId, amount,
			price);

		// when & then
		assertThatThrownBy(() -> orderAndPaymentService.order(command))
			.isInstanceOf(ProductException.class);

		// 잔액, 주문 생성 등의 로직이 호출되지 않아야 함
	}

	@Test
	@DisplayName("잔액 부족 시 주문이 실패해야 한다.")
	void testOrder_Failure_InsufficientBalance() {
		// given
		String userId = "test-user";
		Long productId = 1L;
		int amount = 20; // 잔액보다 많은 가격
		int price = 500;

		OrderAndPaymentCommand command = new OrderAndPaymentCommand(userId, LocalDateTime.now(), productId, amount,
			price);

		// when & then
		assertThatThrownBy(() -> orderAndPaymentService.order(command))
			.isInstanceOf(BalanceException.class);

		// 주문 생성, 결제 등의 로직이 호출되지 않아야 함
	}

	@Test
	@DisplayName("외부 데이터 플랫폼으로 주문 전송 중 오류가 발생해도 주문 처리가 완료되어야 한다.")
	void testOrder_Failure_DataPlatformError() {
		// given
		String userId = "test-user";
		Long productId = 1L;
		int amount = 2;
		int price = 500;

		OrderAndPaymentCommand command = new OrderAndPaymentCommand(userId, LocalDateTime.now(), productId, amount,
			price);

		// when
		OrderInfo result = orderAndPaymentService.order(command);

		// then
		assertThat(result).isNotNull();
		assertThat(result.userId()).isEqualTo(userId);
		assertThat(result.totalPrice()).isEqualTo((long)amount * price);

		// 주문과 결제는 성공, 데이터 플랫폼 전송만 실패
	}
}