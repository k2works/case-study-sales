package com.example.sms.domain.model.sales_order;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 受注明細
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SalesOrderLine {
    OrderNumber orderNumber; // 受注番号
    Integer orderLineNumber; // 受注行番号
    String productCode; // 商品コード
    String productName; // 商品名
    Integer salesUnitPrice; // 販売単価
    Integer orderQuantity; // 受注数量
    Integer taxRate; // 消費税率
    Integer allocationQuantity; // 引当数量
    Integer shipmentInstructionQuantity; // 出荷指示数量
    Integer shippedQuantity; // 出荷済数量
    Integer completionFlag; // 完了フラグ
    Integer discountAmount; // 値引金額
    DeliveryDate deliveryDate; // 納期

    public static SalesOrderLine of(String orderNumber, Integer orderLineNumber, String productCode, String productName, Integer salesUnitPrice, Integer salesQuantity, Integer taxRate, Integer allocationQuantity, Integer shipmentInstructionQuantity, Integer shippedQuantity, Integer completionFlag, Integer discountAmount, LocalDateTime deliveryDate) {
        return new SalesOrderLine(OrderNumber.of(orderNumber), orderLineNumber, productCode, productName, salesUnitPrice, salesQuantity, taxRate, allocationQuantity, shipmentInstructionQuantity, shippedQuantity, completionFlag, discountAmount, DeliveryDate.of(deliveryDate));
    }
}