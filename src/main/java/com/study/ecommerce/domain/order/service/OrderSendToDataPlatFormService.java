package com.study.ecommerce.domain.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.order.dto.OrderInfo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderSendToDataPlatFormService {

	public void send(OrderInfo orderInfo) {
		System.out.println("외부의 데이터 플랫폼으로 전송 :" + orderInfo);
	}

}
