package com.example.sms.domain.type.audit;

import lombok.Getter;

/**
 * アプリケーション実行プロセス区分
 */
@Getter
public enum ApplicationExecutionProcessType {
    USER_REGIS("ユーザー登録", "0001");

    private final String name;

    private final String code;

    ApplicationExecutionProcessType(String name, String code) {
        this.name = name;
        this.code = code;
    }
}