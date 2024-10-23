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
import com.study.ecommerce.domain.product.QProduct;
import com.study.ecommerce.domain.product.dto.ProductOrderAmountInfo;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductQueryDslRepository implements ProductQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<Product> getList() {
		var qProduct = QProduct.product;
		return queryFactory.selectFrom(qProduct).fetch();
	}

	@Override
	public Optional<Product> getOne(Long id) {
		var qProduct = QProduct.product;
		return Optional.ofNullable(
			queryFactory.selectFrom(qProduct)
				.where(qProduct.id.eq(id))
				.fetchOne()
		);
	}

	@Override
	public List<ProductOrderAmountInfo> getOrderAmountByRecent3DayAndTop5() {
		var qOrder = QOrder.order;
		var qOrderItem = QOrderItem.orderItem;
		var qProduct = QProduct.product;

		return queryFactory.select(Projections.constructor(
					ProductOrderAmountInfo.class,
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

}
