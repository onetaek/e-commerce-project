package com.study.ecommerce.api.balance;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import openapi.api.BalanceApi;
import openapi.model.BalanceResponse;
import openapi.model.ChargeBalanceRequest;

@RestController
public class BalanceController implements BalanceApi {
	@Override
	public Optional<NativeWebRequest> getRequest() {
		return BalanceApi.super.getRequest();
	}

	@Override
	public ResponseEntity<BalanceResponse> chargeBalance(ChargeBalanceRequest chargeBalanceRequest) {
		return BalanceApi.super.chargeBalance(chargeBalanceRequest);
	}

	@Override
	public ResponseEntity<BalanceResponse> getBalance(String authorization) {
		return BalanceApi.super.getBalance(authorization);
	}
}
