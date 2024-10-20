package com.study.ecommerce.domain.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.product.ProductException;
import com.study.ecommerce.infra.product.ProductInventoryQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductInventoryValidationService {

	private final ProductInventoryQueryRepository productInventoryQueryRepository;

	public void validateAmount(Long productId, long amount) {
		var productInventory = productInventoryQueryRepository.getOne(productId)
			.orElseThrow(ProductException::notFound);

		if (productInventory.getAmount() < amount) {
			throw ProductException.inventoryAmountExceed();
		}
	}

}
