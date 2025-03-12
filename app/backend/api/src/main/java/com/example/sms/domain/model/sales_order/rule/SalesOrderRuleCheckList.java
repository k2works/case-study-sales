package com.example.sms.domain.model.sales_order.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 受注ルールチェックリスト
 */
public class SalesOrderRuleCheckList {
    List<Map<String, String>> value;

    public SalesOrderRuleCheckList(List<Map<String, String>> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public SalesOrderRuleCheckList add(Map<String, String> error) {
        List<Map<String, String>> newValue = new ArrayList<>(value);
        newValue.add(error);
        return new SalesOrderRuleCheckList(newValue);
    }

    public List<Map<String, String>> asList() {
        return value;
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }
}
