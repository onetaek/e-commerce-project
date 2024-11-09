package com.study.ecommerce.infra.product;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.ecommerce.domain.order.QOrder;
import com.study.ecommerce.domain.order.QOrderItem;
import com.study.ecommerce.domain.product.Product;
import com.study.ecommerce.domain.product.ProductInfo;
import com.study.ecommerce.domain.product.ProductInventory;
import com.study.ecommerce.domain.product.ProductRepository;
import com.study.ecommerce.domain.product.QProduct;
import com.study.ecommerce.domain.product.QProductInventory;

import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

	private final JPAQueryFactory queryFactory;

	public List<Product> getList() {
		var qProduct = QProduct.product;
		return queryFactory.selectFrom(qProduct).fetch();
	}

	@Override
	public List<Product> getList(Long... ids) {
		var qProduct = QProduct.product;
		return queryFactory.selectFrom(qProduct)
			.where(qProduct.id.in(ids))
			.fetch();
	}

	@Transactional
	@Override
	public List<ProductInventory> getInventoryList(Long... productIds) {
		var productInventory = QProductInventory.productInventory;
		return queryFactory.selectFrom(productInventory)
			.where(productInventory.productId.in(productIds))
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)  // 비관적 읽기 락 설정
			.fetch();
	}

	@Override
	public List<ProductInfo.OrderAmount> getOrderAmountByRecent3DayAndTop5() {
		var qOrder = QOrder.order;
		var qOrderItem = QOrderItem.orderItem;
		var qProduct = QProduct.product;

		return queryFactory.select(Projections.constructor(
					ProductInfo.OrderAmount.class,
					qProduct.id,
					qProduct.name,
					qProduct.price,
					qOrderItem.amount.sum()
				)
			).from(qOrderItem).join(qOrder).on(qOrderItem.orderId.eq(qOrder.id))
			.from(qOrderItem).join(qProduct).on(qProduct.id.eq(qOrderItem.productId))
			.where(
				qOrder.orderDate.between(
					LocalDateTime.now().minusDays(3L),
					LocalDateTime.now()
				)
			)
			.groupBy(
				qProduct.id,
				qProduct.name,
				qProduct.price
			)
			.orderBy(qOrderItem.amount.sum().desc())
			.limit(5)
			.fetch();
	}

	@Override
	public Product getById(Long productId) {
		var qProduct = QProduct.product;
		return queryFactory.selectFrom(qProduct)
			.where(qProduct.id.eq(productId))
			.fetchOne();
	}

	@Override
	public ProductInventory getInventoryByProductId(Long productId) {
		var qProductInventory = QProductInventory.productInventory;
		return queryFactory.selectFrom(qProductInventory)
			.where(qProductInventory.productId.eq(productId))
			.fetchOne();
	}
}
