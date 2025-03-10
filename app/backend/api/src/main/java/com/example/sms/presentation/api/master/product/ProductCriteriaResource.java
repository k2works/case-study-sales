package com.example.sms.presentation.api.master.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "商品検索条件")
public class ProductCriteriaResource {
    String productCode;
    String productNameFormal;
    String productNameAbbreviation;
    String productNameKana;
    String productCategoryCode;
    String vendorCode;
    String productType;
    String taxType;
    String miscellaneousType;
    String stockManagementTargetType;
    String stockAllocationType;
}
