package com.example.sms.domain.model.system.audit;

import java.util.List;

/**
 * アプリケーション実行履歴一覧
 */
public class ApplicationExecutionHistoryList {
    List<ApplicationExecutionHistory> value;

    public ApplicationExecutionHistoryList(List<ApplicationExecutionHistory> value) {
        this.value = value;
    }

    public int size() {
        return value.size();
    }

    public List<ApplicationExecutionHistory> asList() {
        return value;
    }
}
