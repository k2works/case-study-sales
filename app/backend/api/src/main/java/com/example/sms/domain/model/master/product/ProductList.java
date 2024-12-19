package com.example.sms.domain.model.master.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 商品一覧
 */
public class ProductList {
    List<Product> value;

    public ProductList(List<Product> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public ProductList add(Product product) {
        List<Product> newValue = new ArrayList<>(value);
        newValue.add(product);
        return new ProductList(newValue);
    }

    public List<Product> asList() {
        return value;
    }
}
