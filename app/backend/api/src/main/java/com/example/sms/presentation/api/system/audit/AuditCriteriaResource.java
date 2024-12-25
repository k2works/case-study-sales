package com.example.sms.presentation.api.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionProcess;
import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.type.audit.ApplicationExecutionProcessFlag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Schema(description = "監査検索条件")
public class AuditCriteriaResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    ApplicationExecutionProcess process;
    ApplicationExecutionHistoryType type;
    ApplicationExecutionProcessFlag processFlag;
}
