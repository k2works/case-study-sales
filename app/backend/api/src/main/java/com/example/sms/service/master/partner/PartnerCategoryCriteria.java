package com.example.sms.service.master.partner;

import lombok.Builder;
import lombok.Value;

/**
 * 取引先分類種別検索条件
 */
@Value
@Builder
public class PartnerCategoryCriteria {
    String partnerCategoryTypeCode; //取引先分類種別コード
    String partnerCategoryTypeName; //取引先分類種別名
    String partnerCategoryItemCode; //取引先分類コード
    String partnerCategoryItemName; //取引先分類名
    String partnerCode;        // 取引先コード
}
