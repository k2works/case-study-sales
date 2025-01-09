package com.example.sms.domain.model.master.partner;

import java.util.Collections;
import java.util.List;

/**
 * 取引先分類一覧
 */
public class PartnerCategoryList {
    List<PartnerCategoryType> value;

    public PartnerCategoryList(List<PartnerCategoryType> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public PartnerCategoryList add(PartnerCategoryType partnerCategoryType) {
        List<PartnerCategoryType> newValue = value;
        newValue.add(partnerCategoryType);
        return new PartnerCategoryList(newValue);
    }

    public List<PartnerCategoryType> asList() {
        return value;
    }
}
