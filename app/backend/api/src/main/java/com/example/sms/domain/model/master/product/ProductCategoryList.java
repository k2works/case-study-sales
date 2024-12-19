package com.example.sms.domain.model.master.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 商品分類一覧
 */
public class ProductCategoryList {
    List<ProductCategory> value;

    public ProductCategoryList(List<ProductCategory> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public ProductCategoryList add(ProductCategory productCategory) {
        List<ProductCategory> newValue = new ArrayList<>(value);
        newValue.add(productCategory);
        return new ProductCategoryList(newValue);
    }

    public List<ProductCategory> asList() {
        return value;
    }
}
