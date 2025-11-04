package com.example.sms.domain.model.system.audit;

import com.example.sms.domain.model.system.user.User;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * アプリケーション実行履歴
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ApplicationExecutionHistory {
    /**
     * ID
     */
    Integer id;

    /**
     * アプリケーション実行プロセス
     */
    ApplicationExecutionProcess process;

    /**
     * アプリケーション実行履歴区分
     */
    ApplicationExecutionHistoryType type;

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
    ApplicationExecutionProcessFlag processFlag;

    /**
     * プロセス詳細
     */
    String processDetails;

    /**
     * ユーザー
     */
     User user;

    public static ApplicationExecutionHistory of(Integer id, String processName, String processCode, ApplicationExecutionHistoryType type, LocalDateTime processStart, LocalDateTime processEnd, ApplicationExecutionProcessFlag processFlag, String processDetails, User user) {
        ApplicationExecutionProcess process = ApplicationExecutionProcess.of(processName, processCode);
        return new ApplicationExecutionHistory(id, process, type, processStart, processEnd, processFlag, processDetails, user);
    }
}
