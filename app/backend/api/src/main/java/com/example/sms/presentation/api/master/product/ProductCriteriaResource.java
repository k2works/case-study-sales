package com.example.sms.presentation.api.master.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "商品検索条件")
public class ProductCriteriaResource {
    @Schema(description = "商品コード")
    String productCode;
    @Schema(description = "商品正式名")
    String productNameFormal;
    @Schema(description = "商品略称")
    String productNameAbbreviation;
    @Schema(description = "商品名カナ")
    String productNameKana;
    @Schema(description = "商品分類コード")
    String productCategoryCode;
    @Schema(description = "仕入先コード")
    String vendorCode;
    @Schema(description = "商品区分")
    String productType;
    @Schema(description = "税区分")
    String taxType;
    @Schema(description = "雑区分")
    String miscellaneousType;
    @Schema(description = "在庫管理対象区分")
    String stockManagementTargetType;
    @Schema(description = "在庫引当区分")
    String stockAllocationType;
}
