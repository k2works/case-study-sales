package com.example.sms.domain.model.master.product;

import com.example.sms.domain.type.quantity.Quantity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 部品表
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Bom {
    ProductCode productCode; // 商品コード
    ProductCode componentCode; // 部品コード
    Quantity componentQuantity; // 部品数量

    public static Bom of(String productCode, String componentCode, Integer componentQuantity) {
        return new Bom(ProductCode.of(productCode), ProductCode.of(componentCode), Quantity.of(componentQuantity));
    }
}
