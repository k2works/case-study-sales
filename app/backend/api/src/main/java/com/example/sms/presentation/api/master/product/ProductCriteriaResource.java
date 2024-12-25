package com.example.sms.presentation.api.master.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Schema(description = "商品検索条件")
public class ProductCriteriaResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    String productCode;
    String productNameFormal;
    String productNameAbbreviation;
    String productNameKana;
    String productCategoryCode;
    String supplierCode;
    String productType;
    Integer taxType;
    Integer miscellaneousType;
    Integer stockManagementTargetType;
    Integer stockAllocationType;
}
