package com.example.sms.domain.model.master.product;

import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.matchesPattern;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * 商品コード
 */
@Value
@NoArgsConstructor(force = true)
public class ProductCode {
    String value;
    BusinessType businessType; // 事業区分
    ItemType itemType; // 品目区分
    LiveStockType livestockType; // 畜産区分
    Integer serialNumber; // 連番

    public ProductCode(String productCode) {
        notNull(productCode, "商品コードは必須です");

        if (productCode.matches("^[A-Z].*")) {
            this.value = productCode;
            this.businessType = BusinessType.その他;
            this.itemType = ItemType.その他;
            this.livestockType = LiveStockType.その他;
            this.serialNumber = 0;
            return;
        }

        matchesPattern(
                productCode,
                "^[0-9]{3}$|^[0-9]{8}$",
                "商品コードは3桁または8桁の数字である必要があります: %s",
                productCode
        );

        if (productCode.length() == 3) {
            this.value = productCode;
            this.businessType = BusinessType.その他;
            this.itemType = ItemType.その他;
            this.livestockType = LiveStockType.その他;
            this.serialNumber = 0;
            return;
        }
        this.businessType = BusinessType.fromCode(productCode.substring(0, 1));
        this.itemType = ItemType.fromCode(productCode.substring(1, 3));
        this.livestockType = LiveStockType.fromCode(productCode.substring(3, 5));
        this.serialNumber = Integer.parseInt(productCode.substring(5, 8));
        this.value = productCode;
    }

    public static ProductCode of(String productCode) {
        return new ProductCode(productCode);
    }
}