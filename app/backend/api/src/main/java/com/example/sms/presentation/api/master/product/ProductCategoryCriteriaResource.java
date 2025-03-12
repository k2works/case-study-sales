package com.example.sms.presentation.api.master.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "商品分類検索条件")
public class ProductCategoryCriteriaResource {
    @Schema(description = "商品分類コード")
    String productCategoryCode;
    @Schema(description = "商品分類名")
    String productCategoryName;
    @Schema(description = "商品分類パス")
    String productCategoryPath;
}
