package com.study.userservice.infra.point;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.userservice.domain.point.Point;
import com.study.userservice.domain.point.PointHistory;
import com.study.userservice.domain.point.PointRepository;
import com.study.userservice.domain.point.QPoint;

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
