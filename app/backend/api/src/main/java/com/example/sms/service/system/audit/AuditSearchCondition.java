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
        String processTypeNameValue = null;
        String processCodeValue = null;
        String processTypeValue = null;
        Integer processFlagValue = null;

        if (process != null) {
            processTypeNameValue = process.getName();
            processCodeValue = process.getCode();
        }
        if (type != null) {
            processTypeValue = type.getName();
        }
        if (processFlag != null) {
            processFlagValue = processFlag.getValue();
        }

        return new AuditSearchCondition(processTypeNameValue, processCodeValue, processTypeValue, processFlagValue);
    }
}
