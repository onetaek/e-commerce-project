package com.study.ecommerce.unit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.study.ecommerce.domain.point.Point;
import com.study.ecommerce.domain.point.PointException;

public class PointUnitTest {
	private Point point;

	@BeforeEach
	public void setUp() {
		point = Point.builder()
			.id(1L)
			.userId("user1")
			.amount(100L)
			.build();
	}

	@Test
	@DisplayName("포인트 충전 - 주어진 금액만큼 포인트가 증가해야 한다")
	public void testCharge() {
		long chargeAmount = 50L;
		point.charge(chargeAmount);
		assertEquals(150L, point.getAmount());
	}

	@Test
	@DisplayName("포인트 사용 - 잔액 내에서 사용 시 포인트가 감소해야 한다")
	public void testUseWithinBalance() {
		long useAmount = 30L;
		point.use(useAmount);
		assertEquals(70L, point.getAmount());
	}

	@Test
	@DisplayName("포인트 사용 - 사용하려는 금액이 잔액을 초과할 경우 예외가 발생해야 한다")
	public void testUseExceedingBalance() {
		long useAmount = 150L;
		PointException exception = assertThrows(PointException.class, () -> {
			point.use(useAmount);
		});
		assertEquals(PointException.exceed().getMessage(), exception.getMessage());

	}
}
