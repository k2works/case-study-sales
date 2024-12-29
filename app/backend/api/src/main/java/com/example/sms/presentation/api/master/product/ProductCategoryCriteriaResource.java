package com.example.sms.presentation.api.master.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Schema(description = "商品分類検索条件")
public class ProductCategoryCriteriaResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    String productCategoryCode;
    String productCategoryName;
    String productCategoryPath;
}
