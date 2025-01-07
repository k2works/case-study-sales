package com.example.sms.domain.model.master.partner;

import java.util.Collections;
import java.util.List;

/**
 * 取引先一覧
 */
public class PartnerList {
    List<Partner> value;

    public PartnerList(List<Partner> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public PartnerList add(Partner partner) {
        List<Partner> newValue = value;
        newValue.add(partner);
        return new PartnerList(newValue);
    }

    public List<Partner> asList() {
        return value;
    }
}
