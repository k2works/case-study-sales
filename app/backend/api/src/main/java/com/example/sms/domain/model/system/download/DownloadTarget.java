package com.example.sms.domain.model.system.download;

import lombok.Getter;

/**
 * ダウンロード対象
 */
@Getter
public enum DownloadTarget {
    部門("department"),
    社員("employee"),
    商品分類("product_category"),
    商品("product"),
    取引先グループ("partner_group");

    private final String value;

    DownloadTarget(String value) {
        this.value = value;
    }
}
