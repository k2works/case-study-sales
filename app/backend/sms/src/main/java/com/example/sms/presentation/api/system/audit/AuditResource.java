package com.example.sms.presentation.api.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionProcess;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessFlag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Schema(description = "監査")
public class AuditResource {
    Integer id;
    ApplicationExecutionProcess process;
    ApplicationExecutionHistoryType type;
    LocalDateTime processStart;
    LocalDateTime processEnd;
    ApplicationExecutionProcessFlag processFlag;
    String processDetails;
}
