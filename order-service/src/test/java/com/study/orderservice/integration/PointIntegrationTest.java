package com.study.orderservice.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.study.orderservice.domain.point.Point;
import com.study.orderservice.domain.point.PointCommand;
import com.study.orderservice.domain.point.PointInfo;
import com.study.orderservice.domain.point.PointRepository;
import com.study.orderservice.domain.point.PointService;

@Transactional
@SpringBootTest
public class PointIntegrationTest {

	@Autowired
	private PointService pointService;

	@Autowired
	private PointRepository pointRepository;

	@BeforeEach
	public void setUp() {
		Point point = Point.builder()
			.id(1L)
			.userId("testUser")
			.amount(100L)
			.build();
		pointRepository.save(point);
	}

	@Test
	@DisplayName("포인트 조회 - 존재하는 유저의 포인트 정보를 조회한다")
	public void testGetOne() {
		PointInfo.Info pointInfo = pointService.getOne("testUser");
		assertEquals("testUser", pointInfo.userId());
		assertEquals(100L, pointInfo.amount());
	}

	@Test
	@DisplayName("포인트 충전 - 기존 유저의 포인트를 충전한다")
	public void testChargeExistingUser() {
		PointCommand.Charge command = new PointCommand.Charge("testUser", 50L);
		pointService.charge(command);

		PointInfo.Info pointInfo = pointService.getOne("testUser");
		assertEquals(150L, pointInfo.amount());
	}

	@Test
	@DisplayName("포인트 충전 - 신규 유저의 포인트를 충전한다")
	public void testChargeNewUser() {
		PointCommand.Charge command = new PointCommand.Charge("newUser", 50L);
		pointService.charge(command);

		PointInfo.Info pointInfo = pointService.getOne("newUser");
		assertEquals(50L, pointInfo.amount());
	}

}
