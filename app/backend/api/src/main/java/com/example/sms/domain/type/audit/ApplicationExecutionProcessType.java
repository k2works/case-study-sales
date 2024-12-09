package com.example.sms.domain.type.audit;

import lombok.Getter;

/**
 * アプリケーション実行プロセス区分
 */
@Getter
public enum ApplicationExecutionProcessType {
    USER_CREATE("ユーザー登録", "0001"),
    USER_UPDATE("ユーザー更新", "0002"),
    USER_DELETE("ユーザー削除", "0003"),
    DEPARTMENT_CREATE("部門登録", "0004"),
    DEPARTMENT_UPDATE("部門更新", "0005"),
    DEPARTMENT_DELETE("部門削除", "0006"),
    EMPLOYEE_CREATE("社員登録", "0007"),
    EMPLOYEE_UPDATE("社員更新", "0008"),
    EMPLOYEE_DELETE("社員削除", "0009"),
    PRODUCT_CATEGORY_CREATE("商品分類登録", "0010"),
    PRODUCT_CATEGORY_UPDATE("商品分類更新", "0011"),
    PRODUCT_CATEGORY_DELETE("商品分類削除", "0012"),
    PRODUCT_CREATE("商品登録", "0013"),
    PRODUCT_UPDATE("商品更新", "0014"),
    PRODUCT_DELETE("商品削除", "0015"),
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
