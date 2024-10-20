package com.study.ecommerce.presentation.balance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.ecommerce.application.balance.BalanceChargeUseCase;
import com.study.ecommerce.domain.balance.service.BalanceQueryService;
import com.study.ecommerce.presentation.balance.dto.BalanceChargeRequest;
import com.study.ecommerce.presentation.balance.dto.BalanceResponse;

import lombok.RequiredArgsConstructor;
import openapi.api.BalanceApi;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/balances")
public class BalanceController implements BalanceApi {

	private final BalanceQueryService balanceQueryService;
	private final BalanceChargeUseCase balanceChargeUseCase;

	@GetMapping
	public ResponseEntity<BalanceResponse> getOne(
		@RequestParam String userId
	) {
		// TODO: 조회시 사용죄는 파라미터 객체로 수정하기 -> Request? Cond? 어떻게 나누어서 사용하는게 좋을지 고민중
		return ResponseEntity.ok().body(
			BalanceResponse.fromInfo(balanceQueryService.getOne(userId))
		);
	}

	@PatchMapping("charge")
	public ResponseEntity<BalanceResponse> charge(
		BalanceChargeRequest request
	) {
		return ResponseEntity.ok().body(
			BalanceResponse.fromInfo(balanceChargeUseCase.charge(request.toCommand()))
		);
	}
}
