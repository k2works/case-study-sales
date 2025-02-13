package com.example.sms.domain.model.master.partner;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

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
    List<PartnerCategoryAffiliation> partnerCategoryAffiliations; //取引先分類所属

    public static PartnerCategoryItem of(String partnerCategoryTypeCode, String partnerCategoryItemCode, String partnerCategoryItemName) {
        return new PartnerCategoryItem(partnerCategoryTypeCode, partnerCategoryItemCode, partnerCategoryItemName, List.of());
    }

    public static PartnerCategoryItem of(PartnerCategoryItem partnerCategoryItem, List<PartnerCategoryAffiliation> partnerCategoryAffiliations) {
        return new PartnerCategoryItem(partnerCategoryItem.getPartnerCategoryTypeCode(), partnerCategoryItem.getPartnerCategoryItemCode(), partnerCategoryItem.getPartnerCategoryItemName(), partnerCategoryAffiliations);
    }
}
