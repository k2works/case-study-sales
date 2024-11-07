package com.example.sms.domain.model.master.product;

import com.example.sms.domain.type.BusinessType;
import com.example.sms.domain.type.ItemType;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 商品コード
 */
@Value
@NoArgsConstructor(force = true)
public class ProductCode {
    String value;
    BusinessType businessType; // 事業区分
    ItemType itemType; // 品目区分
    String livestockType; // 畜産区分
    Integer serialNumber; // 連番

    public ProductCode(String productCode) {
        if (productCode == null) {
            throw new IllegalArgumentException("商品コードは必須です");
        }
        if (!productCode.matches("[0-9]{8}")) {
            throw new IllegalArgumentException("商品コードは8桁の数字である必要があります");
        }
        this.businessType = BusinessType.fromCode(productCode.substring(0, 1));
        this.itemType = ItemType.fromCode(productCode.substring(1, 3));
        this.livestockType = productCode.substring(3, 5);
        this.serialNumber = Integer.parseInt(productCode.substring(5, 8));
        this.value = productCode;
    }

    public static ProductCode of(String productCode) {
        return new ProductCode(productCode);
    }
}
