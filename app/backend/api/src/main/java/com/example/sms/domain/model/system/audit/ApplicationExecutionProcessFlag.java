package com.example.sms.domain.model.system.audit;

/**
 * アプリケーション実行プロセスフラグ
 */
public enum ApplicationExecutionProcessFlag {
    未実行(0),
    実行中(1),
    実行済(2),
    エラー(3);

    private final int i;

    ApplicationExecutionProcessFlag(int i) {
        this.i = i;
    }

    public int getValue() {
        return i;
    }

    public static ApplicationExecutionProcessFlag fromValue(int i) {
        for (ApplicationExecutionProcessFlag flag : ApplicationExecutionProcessFlag.values()) {
            if (flag.getValue() == i) {
                return flag;
            }
        }
        throw new IllegalArgumentException("無効な値: " + i);
    }
}