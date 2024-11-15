package com.study.userservice.infra.point;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.userservice.domain.point.Point;

public interface PointJpaRepository extends JpaRepository<Point, String> {
}
