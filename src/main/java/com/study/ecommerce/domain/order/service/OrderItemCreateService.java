package com.study.ecommerce.domain.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.order.dto.OrderItemCommand;
import com.study.ecommerce.domain.order.dto.OrderItemInfo;
import com.study.ecommerce.infra.order.OrderItemCommandRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemCreateService {

	private final OrderItemCommandRepository orderItemCommandRepository;

	public OrderItemInfo create(OrderItemCommand command) {
		return OrderItemInfo.fromDomain(
			orderItemCommandRepository.save(command.toDomain())
		);
	}
}
