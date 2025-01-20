package com.example.sms.domain.model.master.partner.vendor;

import java.util.ArrayList;
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
        List<Vendor> newValue = new ArrayList<>(value);
        newValue.add(vendor);
        return new VendorList(newValue);
    }

    public VendorList add(List<Vendor> vendors) {
        List<Vendor> newValue = new ArrayList<>(value);
        newValue.addAll(vendors);
        return new VendorList(newValue);
    }

    public List<Vendor> asList() {
        return value;
    }
}
