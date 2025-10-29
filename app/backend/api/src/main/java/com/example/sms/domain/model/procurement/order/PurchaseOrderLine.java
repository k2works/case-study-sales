package com.example.sms.domain.model.procurement.order;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.model.sales.order.CompletionFlag;
import com.example.sms.domain.model.sales.order.OrderNumber;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 発注明細
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class PurchaseOrderLine {
    PurchaseOrderNumber purchaseOrderNumber; // 発注番号
    Integer purchaseOrderLineNumber; // 発注行番号
    Integer purchaseOrderLineDisplayNumber; // 発注行表示番号
    OrderNumber salesOrderNumber; // 受注番号
    Integer salesOrderLineNumber; // 受注行番号
    ProductCode productCode; // 商品コード
    String productName; // 商品名
    Money purchaseUnitPrice; // 発注単価
    Quantity purchaseOrderQuantity; // 発注数量
    Quantity receivedQuantity; // 入荷数量
    CompletionFlag completionFlag; // 完了フラグ
    LocalDateTime createdDateTime; // 作成日時
    String createdBy; // 作成者名
    LocalDateTime updatedDateTime; // 更新日時
    String updatedBy; // 更新者名
    Integer version; // バージョン
    Product product; // 商品

    public static PurchaseOrderLine of(String purchaseOrderNumber, Integer purchaseOrderLineNumber, Integer purchaseOrderLineDisplayNumber, String salesOrderNumber, Integer salesOrderLineNumber, String productCode, String productName, Integer purchaseUnitPrice, Integer purchaseOrderQuantity, Integer receivedQuantity, Integer completionFlag) {
        return PurchaseOrderLine.builder()
                .purchaseOrderNumber(PurchaseOrderNumber.of(purchaseOrderNumber))
                .purchaseOrderLineNumber(purchaseOrderLineNumber)
                .purchaseOrderLineDisplayNumber(purchaseOrderLineDisplayNumber)
                .salesOrderNumber(OrderNumber.of(salesOrderNumber))
                .salesOrderLineNumber(salesOrderLineNumber)
                .productCode(ProductCode.of(productCode))
                .productName(productName)
                .purchaseUnitPrice(Money.of(purchaseUnitPrice))
                .purchaseOrderQuantity(Quantity.of(purchaseOrderQuantity))
                .receivedQuantity(Quantity.of(receivedQuantity))
                .completionFlag(CompletionFlag.of(completionFlag))
                .build();
    }

    /**
     * 発注金額計算（消費税抜き）
     */
    public Money calcPurchaseAmount() {
        return purchaseUnitPrice.multiply(purchaseOrderQuantity);
    }

    /**
     * 消費税計算
     */
    public Money calcConsumptionTax() {
        // 仮の消費税率（10%）
        double taxRate = 0.10;
        int lineTotal = purchaseUnitPrice.getAmount() * purchaseOrderQuantity.getAmount();
        return Money.of((int) (lineTotal * taxRate));
    }

    /**
     * 完了しているかどうか
     */
    public boolean isCompleted() {
        return completionFlag.isCompleted();
    }
}