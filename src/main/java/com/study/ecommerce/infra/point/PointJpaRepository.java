package com.study.ecommerce.infra.point;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.point.Point;

public interface PointJpaRepository extends JpaRepository<Point, String> {
}
