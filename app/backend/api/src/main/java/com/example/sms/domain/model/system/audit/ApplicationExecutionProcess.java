package com.example.sms.domain.model.system.audit;

import com.example.sms.domain.type.audit.ApplicationExecutionProcessType;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Objects;

/**
 * アプリケーション実行プロセス
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ApplicationExecutionProcess {
    ApplicationExecutionProcessType processType;
    String name;
    String code;

    public static ApplicationExecutionProcess of(String processName, String processCode) {
        ApplicationExecutionProcessType processType = ApplicationExecutionProcessType.fromNameAndCode(processName, processCode);
        if (!processType.getCode().equals(processCode)) {
            throw new IllegalArgumentException("不正なプロセスコードです。");
        }
        return new ApplicationExecutionProcess(processType, processName, processCode);
    }

    public String getName() {
        return Objects.requireNonNull(processType).getName();
    }

    public String getCode() {
        return Objects.requireNonNull(processType).getCode();
    }
}
