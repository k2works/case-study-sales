package com.example.sms.domain.model.sales.shipping.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 出荷ルールチェックリスト
 */
public class ShippingRuleCheckList {
    List<Map<String, String>> value;

    public ShippingRuleCheckList(List<Map<String, String>> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public ShippingRuleCheckList add(Map<String, String> error) {
        List<Map<String, String>> newValue = new ArrayList<>(value);
        newValue.add(error);
        return new ShippingRuleCheckList(newValue);
    }

    public List<Map<String, String>> asList() {
        return value;
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }
}