package com.study.ecommerce.domain.order;

import com.study.ecommerce.domain.product.ProductInventory;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.point.Point;
import com.study.ecommerce.domain.point.PointException;
import com.study.ecommerce.domain.point.PointHistory;
import com.study.ecommerce.domain.point.PointRepository;
import com.study.ecommerce.domain.product.Product;
import com.study.ecommerce.domain.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final PointRepository pointRepository;
	private final OrderSend orderSend;

	/**
	 * <h1>주문 프로세스</h1>
	 * <ol>
	 *     <li>상품 재고 차감(유효성)</li>
	 *     <li>주문생성</li>
	 *     <li>주문 아이템 생성</li>
	 *     <li>결제 생성</li>
	 *     <li>유저 포인트 차감(유효성)</li>
	 *     <li>유저 포인트 이력 저장</li>
	 * </ol>
	 */
	@Transactional
	public void order(OrderCommand.Order command) {
		// 상품 재고 차감
		var productIds = command.products().stream().map(OrderCommand.Order.Product::productId).toArray(Long[]::new);
		productInventorySubtract(command, productIds);

		// 주문 생성
		Long orderId = orderRepository.save(command.toOrder()).getId();
		Map<Long, Long> productPriceMap = productRepository.getList(productIds).stream()
			.collect(Collectors.toMap(Product::getId, Product::getPrice));

		// 주문 아이템 생성
		long sumPrice = orderRepository.saveItemAll(
				command.toOrderItem(orderId, productPriceMap)
			).stream()
			.mapToLong(orderItem -> orderItem.getAmount() * orderItem.getPrice())
			.sum();

		// 결제 생성
		orderRepository.savePayment(
			command.toPayment(orderId, sumPrice)
		);

		// 유저 포인트 차감
		var point = pointRepository.getOne(command.userId()).orElseThrow(() -> PointException.notFound(command.userId()));
		point.use(sumPrice);

		// 유저 포인트 이력 저장
		pointRepository.saveHistory(PointHistory.builder()
			.pointId(point.getId())
			.amount(sumPrice)
			.type(Point.Type.USE)
			.build());

		// 외부 플랫폼 전송
		orderSend.send();
	}

	/**
	 * <p>
	 * ProductInventory 클래스의 subtract 메서드가 중요한 로직이고
	 * 지금 메서드는 단순히 매핑하는 것이므로 테스트가 필요하지 않다고 생각하였다.
	 * </p>
	 */
	private void productInventorySubtract(OrderCommand.Order command, Long[] productIds) {
		var commandProducts = command.products().stream()
			.collect(Collectors.toMap(OrderCommand.Order.Product::productId, Function.identity()));
		var inventoryList = productRepository.getInventoryList(productIds);
//		var inventoryList = productRepository.getInventoryListForUpdate(productIds);

		for (ProductInventory inventory : inventoryList) {
			var commandProduct = commandProducts.get(inventory.getProductId());
			inventory.subtract(commandProduct.amount());
		}
	}
}
