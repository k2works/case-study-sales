package com.example.sms.domain.type.audit;

/**
 * アプリケーション実行履歴区分
 */
public enum ApplicationExecutionHistoryType {
    同期,
    非同期;

    public String getName() {
        return this.name();
    }

    public static ApplicationExecutionHistoryType fromName(String name) {
        for (ApplicationExecutionHistoryType type : ApplicationExecutionHistoryType.values()) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("不正なアプリケーション実行履歴区分です。");
    }
}
