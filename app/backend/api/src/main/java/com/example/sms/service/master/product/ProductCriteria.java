package com.example.sms.service.master.product;

import com.example.sms.domain.type.product.*;
import lombok.Builder;
import lombok.Value;

/**
 * 商品検索条件
 */
@Value
@Builder
public class ProductCriteria {
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
