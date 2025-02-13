package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Schema(description = "取引先グループ検索条件")
public class PartnerCategoryCriteriaResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    String partnerCategoryTypeCode; //取引先分類種別コード
    String partnerCategoryTypeName; //取引先分類種別名
    String partnerCategoryItemCode; //取引先分類コード
    String partnerCategoryItemName; //取引先分類名
    String partnerCode;        // 取引先コード
}
