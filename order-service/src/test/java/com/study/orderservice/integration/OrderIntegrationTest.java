package com.study.orderservice.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.study.orderservice.domain.order.Order;
import com.study.orderservice.domain.order.OrderCommand;
import com.study.orderservice.domain.order.OrderItem;
import com.study.orderservice.domain.order.OrderService;
import com.study.orderservice.domain.order.Payment;
import com.study.orderservice.domain.point.Point;
import com.study.orderservice.domain.point.PointHistory;
import com.study.orderservice.domain.point.PointRepository;
import com.study.orderservice.domain.product.Product;
import com.study.orderservice.domain.product.ProductInventory;
import com.study.orderservice.infra.order.OrderItemJpaRepository;
import com.study.orderservice.infra.order.OrderJpaRepository;
import com.study.orderservice.infra.order.PaymentJpaRepository;
import com.study.orderservice.infra.point.PointHistoryJpaRepository;
import com.study.orderservice.infra.product.ProductInventoryJpaRepository;
import com.study.orderservice.infra.product.ProductJpaRepository;

@Transactional
@SpringBootTest
public class OrderIntegrationTest {
	@Autowired
	private OrderService orderService;

	@Autowired
	private PointRepository pointRepository;

	@Autowired
	private PointHistoryJpaRepository pointHistoryJpaRepository;

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@Autowired
	private ProductInventoryJpaRepository productInventoryJpaRepository;

	@Autowired
	private OrderJpaRepository orderJpaRepository;

	@Autowired
	private OrderItemJpaRepository orderItemJpaRepository;

	@Autowired
	private PaymentJpaRepository paymentJpaRepository;

	@BeforeEach
	public void setUp() {
		Product product1 = Product.builder()
			.name("Product1")
			.price(1000L)
			.build();
		Product product2 = Product.builder()
			.name("Product2")
			.price(2000L)
			.build();
		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		ProductInventory inventory1 = ProductInventory.builder()
			.productId(product1.getId())
			.amount(10)
			.build();
		ProductInventory inventory2 = ProductInventory.builder()
			.productId(product2.getId())
			.amount(20)
			.build();
		productInventoryJpaRepository.save(inventory1);
		productInventoryJpaRepository.save(inventory2);

		Point point = Point.builder()
			.userId("testUser")
			.amount(5000L)
			.build();
		pointRepository.save(point);
	}

	@Test
	@DisplayName("주문 프로세스 - 상품 재고 차감, 주문 생성 및 결제 처리")
	public void testOrderProcess() {
		// given
		OrderCommand.Order.Product orderProduct1 = new OrderCommand.Order.Product(1L, 2);
		OrderCommand.Order.Product orderProduct2 = new OrderCommand.Order.Product(2L, 1);
		OrderCommand.Order command = new OrderCommand.Order(
			"testUser",
			LocalDateTime.now(), List.of(orderProduct1, orderProduct2)
		);

		// when
		orderService.order(command);

		// then
		List<Order> orders = orderJpaRepository.findAll();
		assertEquals(1, orders.size());
		Order savedOrder = orders.get(0);
		assertNotNull(savedOrder);
		assertEquals("testUser", savedOrder.getUserId());

		List<OrderItem> orderItems = orderItemJpaRepository.findAll()
			.stream()
			.filter(v -> v.getOrderId().equals(savedOrder.getId()))
			.toList();
		assertEquals(2, orderItems.size());

		Payment payment = paymentJpaRepository.findAll()
			.stream()
			.filter(v -> v.getOrderId().equals(savedOrder.getId()))
			.findFirst().orElseThrow();
		long expectedSumPrice = (1000L * 2) + (2000L * 1);
		assertEquals(expectedSumPrice, payment.getPrice());

		ProductInventory inventory1 = productInventoryJpaRepository.findAll()
			.stream()
			.filter(v -> v.getProductId().equals(1L))
			.findFirst()
			.orElseThrow();
		ProductInventory inventory2 = productInventoryJpaRepository.findAll()
			.stream()
			.filter(v -> v.getProductId().equals(2L))
			.findFirst()
			.orElseThrow();
		assertEquals(8, inventory1.getAmount()); // 10 - 2
		assertEquals(19, inventory2.getAmount()); // 20 - 1

		Point point = pointRepository.getOne("testUser").orElseThrow();
		assertEquals(5000L - expectedSumPrice, point.getAmount());

		List<PointHistory> pointHistories = pointHistoryJpaRepository.findAll()
			.stream()
			.filter(v -> v.getPointId().equals(point.getId()))
			.toList();
		assertEquals(1, pointHistories.size());
		assertEquals(expectedSumPrice, pointHistories.get(0).getAmount());
		assertEquals(Point.Type.USE, pointHistories.get(0).getType());
	}
}
