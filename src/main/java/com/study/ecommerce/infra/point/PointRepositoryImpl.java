package com.study.ecommerce.infra.point;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.ecommerce.domain.point.Point;
import com.study.ecommerce.domain.point.PointHistory;
import com.study.ecommerce.domain.point.PointRepository;
import com.study.ecommerce.domain.point.QPoint;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

	private final JPAQueryFactory queryFactory;
	private final PointJpaRepository pointJpaRepository;
	private final PointHistoryJpaRepository pointHistoryJpaRepository;

	public Optional<Point> getOne(String userId) {
		var point = QPoint.point;
		return Optional.ofNullable(
			queryFactory.selectFrom(point)
				.where(point.userId.eq(userId))
				.fetchOne()
		);
	}

	@Override
	public Point save(Point point) {
		return pointJpaRepository.save(point);
	}

	@Override
	public PointHistory saveHistory(PointHistory pointHistory) {
		return pointHistoryJpaRepository.save(pointHistory);
	}
}
