package com.example.sms.infrastructure.datasource.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.type.audit.ApplicationExecutionProcessFlag;
import com.example.sms.domain.type.user.RoleName;
import com.example.sms.infrastructure.datasource.autogen.model.Usr;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ApplicationExecutionHistoryEntityMapper {
    public ApplicationExecutionHistory mapToDomainModel(ApplicationExecutionHistoryCustomEntity applicationExecutionHistoryEntity) {
        Function<ApplicationExecutionHistoryCustomEntity, User> user = (ApplicationExecutionHistoryCustomEntity applicationExecutionHistory) -> {
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
                ApplicationExecutionProcessFlag.fromValue(applicationExecutionHistoryEntity.getProcessFlag()),
                applicationExecutionHistoryEntity.getProcessDetails(),
                user.apply(applicationExecutionHistoryEntity)
        );
    }

    public ApplicationExecutionHistoryCustomEntity mapToEntity(ApplicationExecutionHistory applicationExecutionHistory) {
        ApplicationExecutionHistoryCustomEntity applicationExecutionHistoryEntity = new ApplicationExecutionHistoryCustomEntity();
        applicationExecutionHistoryEntity.setId(applicationExecutionHistory.getId());
        applicationExecutionHistoryEntity.setProcessName(applicationExecutionHistory.getProcess().getProcessType().getName());
        applicationExecutionHistoryEntity.setProcessCode(applicationExecutionHistory.getProcess().getProcessType().getCode());
        applicationExecutionHistoryEntity.setProcessType(applicationExecutionHistory.getType().name());
        applicationExecutionHistoryEntity.setProcessStart(applicationExecutionHistory.getProcessStart());
        applicationExecutionHistoryEntity.setProcessEnd(applicationExecutionHistory.getProcessEnd());
        applicationExecutionHistoryEntity.setProcessFlag(applicationExecutionHistory.getProcessFlag().getValue());
        applicationExecutionHistoryEntity.setProcessDetails(applicationExecutionHistory.getProcessDetails());
        applicationExecutionHistoryEntity.setUserId(applicationExecutionHistory.getUser().getUserId().getValue());
        return applicationExecutionHistoryEntity;
    }
}
