package com.example.sms.service.master.product;

import lombok.Builder;
import lombok.Value;

/**
 * 商品分類検索条件
 */
@Value
@Builder
public class ProductCategoryCriteria {
        String productCategoryCode;
        String productCategoryName;
        String productCategoryPath;
}
