package com.example.sms.domain.model.master.partner;

import java.util.Collections;
import java.util.List;

/**
 * 仕入先一覧
 */
public class VendorList {
    List<Vendor> value;

    public VendorList(List<Vendor> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public VendorList add(Vendor vendor) {
        List<Vendor> newValue = value;
        newValue.add(vendor);
        return new VendorList(newValue);
    }

    public List<Vendor> asList() {
        return value;
    }
}
