package com.study.ecommerce.domain.point;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointService {

	private final PointRepository pointRepository;

	/**
	 * <h1>포인트 조회</h1>
	 */
	public PointInfo.Info getOne(String userId) {
		return PointInfo.Info.from(
			pointRepository.getOne(userId).orElseThrow(() -> PointException.notFound(userId))
		);
	}

	/**
	 * <h1>포인트 충전</h1>
	 * <ul>
	 *     <li>충전할 때 따로 유효성은 없고 insert or update 만 진행한다.</li>
	 *     <li>한명의 유저가 각각 자신의 포인트를 충전하는 것이므로 동시성 제어는 하지 않는다.</li>
	 * </ul>
	 */
	@Transactional
	public void charge(PointCommand.Charge command) {
		var userId = command.userId();

		// 포인트 충전
		var point = pointRepository.getOne(userId)
			.orElseGet(() -> Point.builder()
				.userId(userId)
				.amount(0L)
				.build());
		point.charge(command.amount());
		pointRepository.save(point);

		// 포인트 이력 저장
		PointHistory pointHistory = PointHistory.builder()
			.pointId(point.getId())
			.amount(command.amount())
			.type(Point.Type.CHARGE)
			.build();
		pointRepository.saveHistory(pointHistory);
	}
}
