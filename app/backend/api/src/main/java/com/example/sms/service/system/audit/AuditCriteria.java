package com.example.sms.service.system.audit;

import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.type.audit.ApplicationExecutionProcessFlag;
import com.example.sms.domain.type.audit.ApplicationExecutionProcessType;
import lombok.Builder;
import lombok.Value;

/**
 * 監査検索条件
 */
@Value
@Builder
public class AuditCriteria {
    String processName;
    String processCode;
    String processType;
    Integer processFlag;

    public static AuditCriteria of(ApplicationExecutionProcessType process, ApplicationExecutionHistoryType type, ApplicationExecutionProcessFlag processFlag) {
        return AuditCriteria.builder()
                .processName(process != null ? process.getName() : null)
                .processCode(process != null ? process.getCode() : null)
                .processType(type != null ? type.getName() : null)
                .processFlag(processFlag != null ? processFlag.getValue() : null)
                .build();
    }
}