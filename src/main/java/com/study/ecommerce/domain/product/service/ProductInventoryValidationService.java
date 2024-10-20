package com.study.ecommerce.domain.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.product.exception.ProductAmountExceedException;
import com.study.ecommerce.domain.product.exception.ProductNotFoundException;
import com.study.ecommerce.infra.product.ProductInventoryQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductInventoryValidationService {

	private final ProductInventoryQueryRepository productInventoryQueryRepository;

	public void validateAmount(Long productId, long amount) {
		var productInventory = productInventoryQueryRepository.getOne(productId)
			.orElseThrow(ProductNotFoundException::new);

		if (productInventory.getAmount() < amount) {
			throw new ProductAmountExceedException();
		}
	}

}
