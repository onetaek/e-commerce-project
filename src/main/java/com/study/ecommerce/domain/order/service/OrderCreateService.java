package com.study.ecommerce.domain.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.order.dto.OrderCommand;
import com.study.ecommerce.domain.order.dto.OrderInfo;
import com.study.ecommerce.infra.order.OrderCommandRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderCreateService {

	private final OrderCommandRepository orderCommandRepository;

	public OrderInfo create(OrderCommand command) {
		return OrderInfo.fromDomain(
			orderCommandRepository.save(command.toDomain())
		);
	}

}
