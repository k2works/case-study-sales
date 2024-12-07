package com.example.sms.infrastructure.datasource.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryList;
import com.example.sms.service.system.audit.AuditRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApplicationExecutionHistoryDataSource implements AuditRepository {
    final ApplicationExecutionHistoryMapper applicationExecutionHistoryMapper;
    final ApplicationExecutionHistoryEntityMapper applicationExecutionHistoryEntityMapper;

    public ApplicationExecutionHistoryDataSource(ApplicationExecutionHistoryMapper applicationExecutionHistoryMapper, ApplicationExecutionHistoryEntityMapper applicationExecutionHistoryEntityMapper) {
        this.applicationExecutionHistoryMapper = applicationExecutionHistoryMapper;
        this.applicationExecutionHistoryEntityMapper = applicationExecutionHistoryEntityMapper;
    }

    @Override
    public void deleteAll() {
        applicationExecutionHistoryMapper.deleteAll();
    }

    @Override
    public void save(ApplicationExecutionHistory history) {

    }

    @Override
    public ApplicationExecutionHistoryList selectAll() {
        List<ApplicationExecutionHistoryEntity> applicationExecutionHistories = applicationExecutionHistoryMapper.selectAll();
        return new ApplicationExecutionHistoryList(applicationExecutionHistories.stream()
                .map(applicationExecutionHistoryEntityMapper::mapToDomainModel)
                .toList());
    }
}
