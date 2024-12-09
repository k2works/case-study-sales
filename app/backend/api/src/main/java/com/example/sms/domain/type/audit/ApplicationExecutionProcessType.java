package com.example.sms.domain.type.audit;

import lombok.Getter;

/**
 * アプリケーション実行プロセス区分
 */
@Getter
public enum ApplicationExecutionProcessType {
    USER_REGIS("ユーザー登録", "0001"),
    OTHER("その他", "9999");

    private final String name;

    private final String code;

    ApplicationExecutionProcessType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static ApplicationExecutionProcessType fromNameAndCode(String name, String code) {
        for (ApplicationExecutionProcessType type : ApplicationExecutionProcessType.values()) {
            if (type.getName().equals(name) && type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("無効な名前またはコード: " + name + ", " + code);
    }
}
