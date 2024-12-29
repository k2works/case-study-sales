package com.example.sms.infrastructure.datasource.system.audit;

import com.example.sms.infrastructure.datasource.autogen.model.ApplicationExecutionHistory;
import com.example.sms.infrastructure.datasource.autogen.model.Usr;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationExecutionHistoryCustomEntity extends ApplicationExecutionHistory {
    private Usr user;
}
