package com.example.sms.domain.model.order;

/**
 * 完了フラグ
 */
public enum CompletionFlag {
    未完了(0),
    完了(1);

    private final int value;

    CompletionFlag(int value) {
        this.value = value;
    }

    public static CompletionFlag of(int completionFlag) {
        return completionFlag == 1 ? 完了 : 未完了;
    }

    public int getValue() {
        return value;
    }
}
