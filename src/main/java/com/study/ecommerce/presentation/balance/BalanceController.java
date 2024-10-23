package com.study.ecommerce.presentation.balance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.ecommerce.application.balance.BalanceChargeService;
import com.study.ecommerce.application.balance.BalanceQueryService;
import com.study.ecommerce.presentation.balance.dto.BalanceChargeRequest;
import com.study.ecommerce.presentation.balance.dto.BalanceResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/balances")
public class BalanceController {

	private final BalanceQueryService balanceQueryService;
	private final BalanceChargeService balanceChargeService;

	/**
	 * 사용자의 잔액 정보를 조회한다.
	 * @param userId 사용자 식별자
	 * @return 사용자의 잔액 정보
	 */
	@GetMapping
	public ResponseEntity<BalanceResponse> getOne(
		@RequestParam String userId
	) {
		return ResponseEntity.ok().body(
			BalanceResponse.fromInfo(balanceQueryService.getOne(userId))
		);
	}

	/**
	 * 사용자의 잔액을 충전한다.
	 * @param request 잔액 충전
	 * @return 사용자의 잔액 정보
	 */
	@PatchMapping("charge")
	public ResponseEntity<BalanceResponse> charge(
		BalanceChargeRequest request
	) {
		return ResponseEntity.ok().body(
			BalanceResponse.fromInfo(balanceChargeService.charge(request.toCommand()))
		);
	}
}
