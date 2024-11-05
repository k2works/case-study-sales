package com.example.sms.service.master.product;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductList;

import java.util.Optional;

public interface ProductRepository {
    void deleteAll();

    void save(Product product);

    ProductList selectAll();

    Optional<Product> findById(String productCode);

    void deleteById(String productCode);
}
