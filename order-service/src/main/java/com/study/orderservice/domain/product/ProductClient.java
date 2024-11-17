package com.study.orderservice.domain.product;

import java.util.List;

public interface ProductClient {
    List<ProductInfo.Data> getProductList(Long... productIds);
}
