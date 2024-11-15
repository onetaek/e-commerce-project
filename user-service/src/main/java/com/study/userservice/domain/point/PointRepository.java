package com.study.userservice.domain.point;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository {
	Optional<Point> getOne(String userId);

	Point save(Point point);

	PointHistory saveHistory(PointHistory pointHistory);
}
