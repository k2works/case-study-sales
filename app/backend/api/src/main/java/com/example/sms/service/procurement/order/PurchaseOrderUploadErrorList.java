package com.example.sms.service.procurement.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 発注アップロードエラーリスト
 */
public class PurchaseOrderUploadErrorList {
    List<Map<String, String>> value;

    public PurchaseOrderUploadErrorList() {
        this.value = new ArrayList<>();
    }

    public PurchaseOrderUploadErrorList(List<Map<String, String>> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public PurchaseOrderUploadErrorList add(Map<String, String> error) {
        List<Map<String, String>> newValue = new ArrayList<>(value);
        newValue.add(error);
        return new PurchaseOrderUploadErrorList(newValue);
    }

    public List<Map<String, String>> asList() {
        return value;
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }
}