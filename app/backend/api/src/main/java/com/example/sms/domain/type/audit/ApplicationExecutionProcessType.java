package com.example.sms.domain.type.audit;

import lombok.Getter;

/**
 * アプリケーション実行プロセス区分
 */
@Getter
public enum ApplicationExecutionProcessType {
    ユーザー登録("ユーザー登録", "0001"),
    ユーザー更新("ユーザー更新", "0002"),
    ユーザー削除("ユーザー削除", "0003"),
    部門登録("部門登録", "0004"),
    部門更新("部門更新", "0005"),
    部門削除("部門削除", "0006"),
    社員登録("社員登録", "0007"),
    社員更新("社員更新", "0008"),
    社員削除("社員削除", "0009"),
    商品分類登録("商品分類登録", "0010"),
    商品分類更新("商品分類更新", "0011"),
    商品分類削除("商品分類削除", "0012"),
    商品登録("商品登録", "0013"),
    商品更新("商品更新", "0014"),
    商品削除("商品削除", "0015"),
    データダウンロード("データダウンロード", "9001"),
    その他("その他", "9999");

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
