package com.example.sms.service.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 在庫アップロードエラーリスト
 */
public class InventoryUploadErrorList {
    List<Map<String, String>> value;

    public InventoryUploadErrorList() {
        this.value = new ArrayList<>();
    }

    public InventoryUploadErrorList(List<Map<String, String>> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public InventoryUploadErrorList add(Map<String, String> error) {
        List<Map<String, String>> newValue = new ArrayList<>(value);
        newValue.add(error);
        return new InventoryUploadErrorList(newValue);
    }

    public List<Map<String, String>> asList() {
        return value;
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }
}