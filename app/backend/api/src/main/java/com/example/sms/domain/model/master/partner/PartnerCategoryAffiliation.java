package com.example.sms.domain.model.master.partner;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 取引先分類所属
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PartnerCategoryAffiliation {
    String partnerCategoryTypeCode; //取引先分類種別コード
    String partnerCode;          // 取引先コード
    String partnerCategoryItemCode; //取引先分類コード

    public static PartnerCategoryAffiliation of(String partnerCategoryTypeCode, String partnerCode, String partnerCategoryItemCode) {
        return new PartnerCategoryAffiliation(partnerCategoryTypeCode, partnerCode, partnerCategoryItemCode);
    }
}
