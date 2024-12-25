package com.example.sms.domain.type.product;

import lombok.Getter;

/**
 * 商品区分
 */
@Getter
public enum ProductType {
    商品("1"), 製品("2"), 部品("3"), 包材("4"), その他("9");

    private final String code;

    ProductType(String code) {
        this.code = code;
    }

    public static String getCodeByName(String name) {
        for (ProductType productType : ProductType.values()) {
            if (productType.name().equals(name)) {
                return productType.getCode();
            }
        }
        throw new IllegalArgumentException("商品区分未登録:" + name);
    }

    public static ProductType fromCode(String code) {
        for (ProductType productType : ProductType.values()) {
            if (productType.code.equals(code)) {
                return productType;
            }
        }
        throw new IllegalArgumentException("商品区分未登録:" + code);
    }
}
