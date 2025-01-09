package com.example.sms.domain.model.master.partner;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 取引先分類
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PartnerCategoryItem {
    String partnerCategoryTypeCode; //取引先分類種別コード
    String partnerCategoryItemCode; //取引先分類コード
    String partnerCategoryItemName; //取引先分類名

    public static PartnerCategoryItem of(String partnerCategoryTypeCode, String partnerCategoryItemCode, String partnerCategoryItemName) {
        return new PartnerCategoryItem(partnerCategoryTypeCode, partnerCategoryItemCode, partnerCategoryItemName);
    }
}
