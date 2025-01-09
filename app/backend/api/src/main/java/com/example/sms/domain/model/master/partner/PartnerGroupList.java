package com.example.sms.domain.model.master.partner;

import java.util.Collections;
import java.util.List;

/**
 * 取引先グループ一覧
 */
public class PartnerGroupList {
    List<PartnerGroup> value;

    public PartnerGroupList(List<PartnerGroup> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public PartnerGroupList add(PartnerGroup partnerGroup) {
        List<PartnerGroup> newValue = value;
        newValue.add(partnerGroup);
        return new PartnerGroupList(newValue);
    }

    public List<PartnerGroup> asList() {
        return value;
    }
}
