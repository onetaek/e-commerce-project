package com.study.ecommerce.presentation.point;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.ecommerce.domain.point.PointService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
public class PointController {

	private final PointService pointService;

	/**
	 * 사용자의 잔액 정보를 조회한다.
	 * @param userId 사용자 식별자
	 * @return 사용자의 잔액 정보
	 */
	@GetMapping
	public ResponseEntity<PointDto.Response> getOne(
		@RequestParam String userId
	) {
		return ResponseEntity.ok().body(
			PointDto.Response.fromInfo(pointService.getOne(userId))
		);
	}

	/**
	 * 사용자의 잔액을 충전한다.
	 * @param request 잔액 충전
	 * @return 사용자의 잔액 정보
	 */
	@PatchMapping("charge")
	public ResponseEntity<Void> charge(
		PointDto.Request request
	) {
		pointService.charge(request.toCommand());
		return ResponseEntity.ok().build();
	}
}
