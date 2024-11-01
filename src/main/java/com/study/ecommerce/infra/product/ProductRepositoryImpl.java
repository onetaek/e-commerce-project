package com.study.ecommerce.infra.product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

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

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

	private final JPAQueryFactory queryFactory;
	private final ProductInventoryJpaRepository productInventoryJpaRepository;

	public Optional<Product> getInventory(Long id) {
		var qProduct = QProduct.product;
		return Optional.ofNullable(
			queryFactory.selectFrom(qProduct)
				.where(qProduct.id.eq(id))
				.fetchOne()
		);
	}

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

	@Override
	public List<ProductInventory> getInventoryList(Long... productIds) {
		var productInventory = QProductInventory.productInventory;
		return queryFactory.selectFrom(productInventory)
			.where(productInventory.productId.in(productIds))
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
			.groupBy(qOrderItem.productId)
			.orderBy(qOrderItem.amount.sum().desc())
			.limit(5)
			.fetch();
	}

	public List<ProductInventory> getInventoryListForUpdate(Long... productIds) {
		return productInventoryJpaRepository.findByProductIdsForUpdate(productIds);
	}

}
