package com.example.sms.domain.model.master.product;

import java.util.List;

/**
 * 商品分類一覧
 */
public class ProductCategoryList {
    List<ProductCategory> value;

    public ProductCategoryList(List<ProductCategory> value) {
        this.value = value;
    }

    public int size() {
        return value.size();
    }

    public List<ProductCategory> asList() {
        return value;
    }
}
