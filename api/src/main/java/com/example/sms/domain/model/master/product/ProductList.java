package com.example.sms.domain.model.master.product;

import java.util.List;

/**
 * 商品一覧
 */
public class ProductList {
    List<Product> value;

    public ProductList(List<Product> value) {
        this.value = value;
    }

    public int size() {
        return value.size();
    }

    public List<Product> asList() {
        return value;
    }
}
