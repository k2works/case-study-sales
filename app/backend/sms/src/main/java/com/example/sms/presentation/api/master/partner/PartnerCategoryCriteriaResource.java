package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "取引先グループ検索条件")
public class PartnerCategoryCriteriaResource {
    @Schema(description = "取引先分類種別コード")
    String partnerCategoryTypeCode;
    @Schema(description = "取引先分類種別名")
    String partnerCategoryTypeName;
    @Schema(description = "取引先分類コード")
    String partnerCategoryItemCode;
    @Schema(description = "取引先分類名")
    String partnerCategoryItemName;
    @Schema(description = "取引先コード")
    String partnerCode;
}
