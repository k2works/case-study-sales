package com.example.sms.presentation.api.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionProcess;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessFlag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "監査検索条件")
public class AuditCriteriaResource {
    ApplicationExecutionProcess process;
    ApplicationExecutionHistoryType type;
    ApplicationExecutionProcessFlag processFlag;
}
