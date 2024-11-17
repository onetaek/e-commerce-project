package com.study.orderservice.domain.product;

public class ProductInfo {

    public record Data(
            Long id,
            String name,
            Long price
    ) {


    }


}
