package com.example.sms.infrastructure.datasource.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.type.user.RoleName;
import com.example.sms.infrastructure.datasource.system.user.Usr;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ApplicationExecutionHistoryEntityMapper {
    public ApplicationExecutionHistory mapToDomainModel(ApplicationExecutionHistoryEntity applicationExecutionHistoryEntity) {
        Function<ApplicationExecutionHistoryEntity, User> user = (ApplicationExecutionHistoryEntity applicationExecutionHistory) -> {
            if (applicationExecutionHistory.getUser() == null) {
                return null;
            }
            Usr userEntity = applicationExecutionHistory.getUser();
            return User.of(
                    userEntity.getUserId(),
                    userEntity.getPassword(),
                    userEntity.getFirstName(),
                    userEntity.getLastName(),
                    RoleName.valueOf(userEntity.getRoleName())
            );
        };

        return com.example.sms.domain.model.system.audit.ApplicationExecutionHistory.of(
                applicationExecutionHistoryEntity.getId(),
                applicationExecutionHistoryEntity.getProcessName(),
                applicationExecutionHistoryEntity.getProcessCode(),
                ApplicationExecutionHistoryType.valueOf(applicationExecutionHistoryEntity.getProcessType()),
                applicationExecutionHistoryEntity.getProcessStart(),
                applicationExecutionHistoryEntity.getProcessEnd(),
                applicationExecutionHistoryEntity.getProcessFlag(),
                applicationExecutionHistoryEntity.getProcessDetails(),
                user.apply(applicationExecutionHistoryEntity)
        );
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
        applicationExecutionHistoryEntity.setUserId(applicationExecutionHistory.getUser().getUserId().getValue());
        return applicationExecutionHistoryEntity;
    }
}
