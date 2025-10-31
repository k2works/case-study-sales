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
    取引先グループ("partner_group"),
    取引先("partner"),
    顧客("customer"),
    仕入先("vendor"),
    受注("order"),
    出荷("shipment"),
    売上("sales"),
    請求("invoice"),
    入金("payment"),
    口座("payment_account"),
    発注("purchase_order"),
    仕入("purchase"),
    在庫("inventory"),
    倉庫("warehouse"),
    棚番("location_number");

    private final String value;

    DownloadTarget(String value) {
        this.value = value;
    }
}
