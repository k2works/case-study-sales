package com.example.sms.service.sales_order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 受注アップロードエラーリスト
 */
public class SalesOrderUploadErrorList {
    List<Map<String, String>> value;

    public SalesOrderUploadErrorList(List<Map<String, String>> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public SalesOrderUploadErrorList add(Map<String, String> error) {
        List<Map<String, String>> newValue = new ArrayList<>(value);
        newValue.add(error);
        return new SalesOrderUploadErrorList(newValue);
    }

    public List<Map<String, String>> asList() {
        return value;
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }
}
