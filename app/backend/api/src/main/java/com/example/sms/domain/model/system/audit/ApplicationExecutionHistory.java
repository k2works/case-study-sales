package com.example.sms.domain.model.system.audit;

import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
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
     * プロセス名
     */
    String processName;

    /**
     * プロセスコード
     */
    String processCode;

    /**
     * アプリケーション実行履歴区分
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

    /**
     * ユーザー
     */
     User user;

    public static ApplicationExecutionHistory of(Integer id, String processName, String processCode, ApplicationExecutionHistoryType processType, LocalDateTime processStart, LocalDateTime processEnd, int processFlag, String processDetails, User user) {
        return new ApplicationExecutionHistory(id, processName, processCode, processType, processStart, processEnd, processFlag, processDetails, user);
    }
}
