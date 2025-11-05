package com.example.sms.domain.model.inventory;

import lombok.Getter;

/**
 * 良品区分
 */
@Getter
public enum QualityCategory {
    良品("G", "良品（正常品質）"),
    不良品("B", "不良品（品質不良）"),
    返品("R", "返品（お客様返品）");

    private final String code;
    private final String description;

    QualityCategory(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static QualityCategory of(String code) {
        for (QualityCategory qualityCategory : QualityCategory.values()) {
            if (qualityCategory.getCode().equals(code)) {
                return qualityCategory;
            }
        }
        throw new IllegalArgumentException("不正な良品区分です: " + code);
    }
}