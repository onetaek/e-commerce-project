package com.study.userservice.application;

import org.springframework.transaction.annotation.Transactional;

import com.study.userservice.common.annotation.Facade;
import com.study.userservice.domain.point.PointCommand;
import com.study.userservice.domain.point.PointEventCommand;
import com.study.userservice.domain.point.PointEventService;
import com.study.userservice.domain.point.PointService;

import lombok.RequiredArgsConstructor;

@Facade
@Transactional
@RequiredArgsConstructor
public class PointFacade {

	private final PointService pointService;
	private final PointEventService pointEventService;

	public void usePoint(PointCommand.Use command, String key) {
		// 포인트 사용
		pointService.use(command);

		// 이벤트 발행
		pointEventService.publishEvent(
			new PointEventCommand.Use(
				key,
				command.userId(),
				command.orderId(),
				command.totalPrice()
			)
		);
	}
}
