package com.example.sms.service.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

/**
 * 監査リポジトリ
 */
public interface AuditRepository {
    void deleteAll();

    void save(ApplicationExecutionHistory history);

    ApplicationExecutionHistoryList selectAll();

    Optional<ApplicationExecutionHistory> findById(Integer id);

    void deleteById(Integer id);

    PageInfo<ApplicationExecutionHistory> selectAllWithPageInfo();

    ApplicationExecutionHistory start(ApplicationExecutionHistory history);

    PageInfo<ApplicationExecutionHistory> searchWithPageInfo(AuditCriteria criteria);
}
