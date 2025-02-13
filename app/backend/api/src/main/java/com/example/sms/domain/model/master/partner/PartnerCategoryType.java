package com.example.sms.domain.model.master.partner;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

/**
 * 取引先分類種別
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PartnerCategoryType {
    String partnerCategoryTypeCode; //取引先分類種別コード
    String partnerCategoryTypeName; //取引先分類種別名
    List<PartnerCategoryItem> partnerCategoryItems; //取引先分類

    public static PartnerCategoryType of(String partnerCategoryTypeCode, String partnerCategoryTypeName) {
        return new PartnerCategoryType(partnerCategoryTypeCode, partnerCategoryTypeName, List.of());
    }

    public static PartnerCategoryType of(PartnerCategoryType partnerCategoryType, List<PartnerCategoryItem> partnerCategoryItems) {
        return new PartnerCategoryType(partnerCategoryType.getPartnerCategoryTypeCode(), partnerCategoryType.getPartnerCategoryTypeName(), partnerCategoryItems);
    }
}
