package com.example.sms.service.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryList;

/**
 * 監査リポジトリ
 */
public interface AuditRepository {
    void deleteAll();

    void save(ApplicationExecutionHistory history);

    ApplicationExecutionHistoryList selectAll();
}
