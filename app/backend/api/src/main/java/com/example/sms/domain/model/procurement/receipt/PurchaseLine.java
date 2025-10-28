package com.example.sms.domain.model.procurement.receipt;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 仕入明細
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class PurchaseLine {
    PurchaseNumber purchaseNumber; // 仕入番号
    Integer purchaseLineNumber; // 仕入行番号
    Integer purchaseLineDisplayNumber; // 仕入行表示番号
    Integer purchaseOrderLineNumber; // 発注行番号
    ProductCode productCode; // 商品コード
    WarehouseCode warehouseCode; // 倉庫コード
    String productName; // 商品名
    Money purchaseUnitPrice; // 仕入単価
    Quantity purchaseQuantity; // 仕入数量
    LocalDateTime createdDateTime; // 作成日時
    String createdBy; // 作成者名
    LocalDateTime updatedDateTime; // 更新日時
    String updatedBy; // 更新者名
    Integer version; // バージョン
    Product product; // 商品

    public static PurchaseLine of(String purchaseNumber, Integer purchaseLineNumber, Integer purchaseLineDisplayNumber, Integer purchaseOrderLineNumber, String productCode, String warehouseCode, String productName, Integer purchaseUnitPrice, Integer purchaseQuantity) {
        return PurchaseLine.builder()
                .purchaseNumber(PurchaseNumber.of(purchaseNumber))
                .purchaseLineNumber(purchaseLineNumber)
                .purchaseLineDisplayNumber(purchaseLineDisplayNumber)
                .purchaseOrderLineNumber(purchaseOrderLineNumber)
                .productCode(ProductCode.of(productCode))
                .warehouseCode(WarehouseCode.of(warehouseCode))
                .productName(productName)
                .purchaseUnitPrice(Money.of(purchaseUnitPrice))
                .purchaseQuantity(Quantity.of(purchaseQuantity))
                .build();
    }

    /**
     * 仕入金額計算（消費税抜き）
     */
    public Money calcPurchaseAmount() {
        return purchaseUnitPrice.multiply(purchaseQuantity);
    }

    /**
     * 消費税計算
     */
    public Money calcConsumptionTax() {
        // 仮の消費税率（10%）
        double taxRate = 0.10;
        int lineTotal = purchaseUnitPrice.getAmount() * purchaseQuantity.getAmount();
        return Money.of((int) (lineTotal * taxRate));
    }
}
