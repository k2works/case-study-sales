package com.example.sms.domain.model.system.audit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * アプリケーション実行履歴一覧
 */
public class ApplicationExecutionHistoryList {
    List<ApplicationExecutionHistory> value;

    public ApplicationExecutionHistoryList(List<ApplicationExecutionHistory> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public ApplicationExecutionHistoryList add(ApplicationExecutionHistory applicationExecutionHistory) {
        List<ApplicationExecutionHistory> newValue = new ArrayList<>(value);
        newValue.add(applicationExecutionHistory);
        return new ApplicationExecutionHistoryList(newValue);
    }

    public List<ApplicationExecutionHistory> asList() {
        return value;
    }
}
