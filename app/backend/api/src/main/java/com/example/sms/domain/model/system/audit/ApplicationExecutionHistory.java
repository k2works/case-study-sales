package com.example.sms.domain.model.system.audit;

import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ApplicationExecutionHistory {
    /**
     * ID
     */
    Integer id;

    /**
     * プロセス名
     */
    String processName;

    /**
     * プロセスコード
     */
    String processCode;

    /**
     * プロセス種別
     */
    ApplicationExecutionHistoryType processType;

    /**
     * プロセス開始日時
     */
    LocalDateTime processStart;

    /**
     * プロセス終了日時
     */
    LocalDateTime processEnd;

    /**
     * プロセスフラグ
     */
    Integer processFlag;

    /**
     * プロセス詳細
     */
    String processDetails;

    public static ApplicationExecutionHistory of(Integer id, String processName, String processCode, ApplicationExecutionHistoryType processType, LocalDateTime processStart, LocalDateTime processEnd, int processFlag, String processDetails) {
        return new ApplicationExecutionHistory(id, processName, processCode, processType, processStart, processEnd, processFlag, processDetails);
    }
}
