package com.study.orderservice.domain.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductClient productClient;

    public List<ProductInfo.Data> getProductList(Long... ids) {
        return productClient.getProductList(ids);
    }

}
