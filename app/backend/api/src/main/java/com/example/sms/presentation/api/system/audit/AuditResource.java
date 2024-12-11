package com.example.sms.presentation.api.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionProcess;
import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.type.audit.ApplicationExecutionProcessFlag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Schema(description = "認可")
public class AuditResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    Integer id;
    ApplicationExecutionProcess process;
    ApplicationExecutionHistoryType type;
    LocalDateTime processStart;
    LocalDateTime processEnd;
    ApplicationExecutionProcessFlag processFlag;
    String processDetails;
}
