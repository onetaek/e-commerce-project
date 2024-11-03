package com.study.ecommerce.domain.order;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.point.Point;
import com.study.ecommerce.domain.point.PointException;
import com.study.ecommerce.domain.point.PointHistory;
import com.study.ecommerce.domain.point.PointRepository;
import com.study.ecommerce.domain.product.Product;
import com.study.ecommerce.domain.product.ProductInventory;
import com.study.ecommerce.domain.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final PointRepository pointRepository;
	private final OrderSend orderSend;
	private final RedissonClient redissonClient;

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
	public void order(OrderCommand.Order command) {
		var productIds = command.products().stream().map(OrderCommand.Order.Product::productId).toArray(Long[]::new);

		// 락 획득
		List<RLock> locks = Arrays.stream(productIds).map(v -> redissonClient.getLock("productId:" + v)).toList();
		try {
			// 모든 락을 획득 (최대 10초 동안 기다리고, 10초 동안 유지)
			for (RLock lock : locks) {
				lock.lock(10, TimeUnit.SECONDS);
			}

			// 락을 획득한 이후 트랜잭션 시작
			processOrderWithTransaction(command, productIds);
		} finally {
			// 락 해제
			for (RLock lock : locks) {
				lock.unlock();
			}
		}
	}

	@Transactional
	public void processOrderWithTransaction(OrderCommand.Order command, Long[] productIds) {
		// 상품 재고 차감
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
		var point = pointRepository.getOne(command.userId())
			.orElseThrow(() -> PointException.notFound(command.userId()));
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

	private void productInventorySubtract(OrderCommand.Order command, Long[] productIds) {
		var commandProducts = command.products().stream()
			.collect(Collectors.toMap(OrderCommand.Order.Product::productId, Function.identity()));
		var inventoryList = productRepository.getInventoryList(productIds);

		for (ProductInventory inventory : inventoryList) {
			var commandProduct = commandProducts.get(inventory.getProductId());
			inventory.subtract(commandProduct.amount());
		}
	}
}
