package com.example.sms.infrastructure.datasource.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import org.springframework.stereotype.Component;

@Component
public class ApplicationExecutionHistoryEntityMapper {
    public ApplicationExecutionHistory mapToDomainModel(ApplicationExecutionHistoryEntity applicationExecutionHistoryEntity) {
        return com.example.sms.domain.model.system.audit.ApplicationExecutionHistory.of(
                applicationExecutionHistoryEntity.getId(),
                applicationExecutionHistoryEntity.getProcessName(),
                applicationExecutionHistoryEntity.getProcessCode(),
                ApplicationExecutionHistoryType.valueOf(applicationExecutionHistoryEntity.getProcessType()),
                applicationExecutionHistoryEntity.getProcessStart(),
                applicationExecutionHistoryEntity.getProcessEnd(),
                applicationExecutionHistoryEntity.getProcessFlag(),
                applicationExecutionHistoryEntity.getProcessDetails());
    }

    public ApplicationExecutionHistoryEntity mapToEntity(ApplicationExecutionHistory applicationExecutionHistory) {
        ApplicationExecutionHistoryEntity applicationExecutionHistoryEntity = new ApplicationExecutionHistoryEntity();
        applicationExecutionHistoryEntity.setId(applicationExecutionHistory.getId());
        applicationExecutionHistoryEntity.setProcessName(applicationExecutionHistory.getProcessName());
        applicationExecutionHistoryEntity.setProcessCode(applicationExecutionHistory.getProcessCode());
        applicationExecutionHistoryEntity.setProcessType(applicationExecutionHistory.getProcessType().name());
        applicationExecutionHistoryEntity.setProcessStart(applicationExecutionHistory.getProcessStart());
        applicationExecutionHistoryEntity.setProcessEnd(applicationExecutionHistory.getProcessEnd());
        applicationExecutionHistoryEntity.setProcessFlag(applicationExecutionHistory.getProcessFlag());
        applicationExecutionHistoryEntity.setProcessDetails(applicationExecutionHistory.getProcessDetails());
        return applicationExecutionHistoryEntity;
    }
}
