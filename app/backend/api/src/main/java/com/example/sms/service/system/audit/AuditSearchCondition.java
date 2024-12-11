package com.example.sms.service.system.audit;

import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.type.audit.ApplicationExecutionProcessFlag;
import com.example.sms.domain.type.audit.ApplicationExecutionProcessType;
import lombok.Value;

/**
 * 監査検索条件
 */
@Value
public class AuditSearchCondition {
    String processName;
    String processCode;
    String processType;
    Integer processFlag;

    public static AuditSearchCondition of(ApplicationExecutionProcessType process, ApplicationExecutionHistoryType type, ApplicationExecutionProcessFlag processFlag) {

        return new AuditSearchCondition(process.getName(), process.getCode(), type.getName(), processFlag.getValue());
    }
}
