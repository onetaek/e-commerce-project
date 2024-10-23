package com.study.ecommerce.application.product;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.product.dto.ProductDetailInfo;
import com.study.ecommerce.domain.product.dto.ProductOrderAmountInfo;
import com.study.ecommerce.domain.product.service.ProductReader;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductQueryService {

	private final ProductReader productReader;

	public List<ProductDetailInfo> getDetailList() {
		return productReader.getDetailList();
	}

	public List<ProductOrderAmountInfo> getPopularProducts() {
		return productReader.getPopularProducts();
	}
}
