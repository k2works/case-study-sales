package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 商品分類
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ProductCategory {
    String productCategoryCode; // 商品分類コード
    String productCategoryName; // 商品分類名
    Integer productCategoryHierarchy; // 商品分類階層
    String productCategoryPath; // 商品分類パス
    Integer lowestLevelDivision; // 最下層区分
}
