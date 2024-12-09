package com.example.sms.service.master.product;

import com.example.sms.domain.model.master.product.ProductCategory;
import com.example.sms.domain.model.master.product.ProductCategoryList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

public interface ProductCategoryRepository {
    void deleteAll();

    void save(ProductCategory product);

    ProductCategoryList selectAll();

    Optional<ProductCategory> findById(String productCategoryCode);

    void deleteById(ProductCategory productCategory);

    PageInfo<ProductCategory> selectAllWithPageInfo();
}
