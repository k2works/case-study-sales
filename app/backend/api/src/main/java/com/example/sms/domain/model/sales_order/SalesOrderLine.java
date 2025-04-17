package com.example.sms.domain.model.sales_order;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 受注明細
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SalesOrderLine {
    OrderNumber orderNumber; // 受注番号
    Integer orderLineNumber; // 受注行番号
    ProductCode productCode; // 商品コード
    String productName; // 商品名
    Money salesUnitPrice; // 販売単価
    Quantity orderQuantity; // 受注数量
    TaxRateType taxRate; // 消費税率
    Quantity allocationQuantity; // 引当数量
    Quantity shipmentInstructionQuantity; // 出荷指示数量
    Quantity shippedQuantity; // 出荷済数量
    CompletionFlag completionFlag; // 完了フラグ
    Money discountAmount; // 値引金額
    DeliveryDate deliveryDate; // 納期
    Product product; // 商品
    SalesAmount salesAmount; // 販売価格
    ConsumptionTaxAmount consumptionTaxAmount; // 消費税額

    public static SalesOrderLine of(String orderNumber, Integer orderLineNumber, String productCode, String productName, Integer salesUnitPrice, Integer salesQuantity, Integer taxRate, Integer allocationQuantity, Integer shipmentInstructionQuantity, Integer shippedQuantity, Integer completionFlag, Integer discountAmount, LocalDateTime deliveryDate) {
        SalesCalculation salesCalculation = SalesCalculation.of(Money.of(salesUnitPrice), Quantity.of(salesQuantity), null, TaxRateType.of(taxRate));

        return new SalesOrderLine(
                OrderNumber.of(orderNumber),
                orderLineNumber,
                ProductCode.of(productCode),
                productName,
                Money.of(salesUnitPrice),
                Quantity.of(salesQuantity),
                TaxRateType.of(taxRate),
                Quantity.of(allocationQuantity),
                Quantity.of(shipmentInstructionQuantity),
                Quantity.of(shippedQuantity),
                CompletionFlag.of(completionFlag),
                Money.of(discountAmount),
                DeliveryDate.of(deliveryDate),
                null,
                salesCalculation.getSalesAmount(),
                salesCalculation.getConsumptionTaxAmount()
        );
    }

    public static SalesOrderLine complete(SalesOrderLine salesOrderLine) {
        SalesCalculation salesCalculation = SalesCalculation.of(salesOrderLine.getSalesUnitPrice(), salesOrderLine.getOrderQuantity(), salesOrderLine.getProduct(), salesOrderLine.getTaxRate());

        return new SalesOrderLine(
                salesOrderLine.getOrderNumber(),
                salesOrderLine.getOrderLineNumber(),
                salesOrderLine.getProductCode(),
                salesOrderLine.getProductName(),
                salesOrderLine.getSalesUnitPrice(),
                salesOrderLine.getOrderQuantity(),
                salesOrderLine.getTaxRate(),
                salesOrderLine.getAllocationQuantity(),
                salesOrderLine.getShipmentInstructionQuantity(),
                salesOrderLine.getShippedQuantity(),
                CompletionFlag.完了,
                salesOrderLine.getDiscountAmount(),
                salesOrderLine.getDeliveryDate(),
                salesOrderLine.getProduct(),
                salesCalculation.getSalesAmount(),
                salesCalculation.getConsumptionTaxAmount()
        );
    }

    public static SalesOrderLine of(SalesOrderLine salesOrderLine, Product product) {
        SalesCalculation salesCalculation = SalesCalculation.of(salesOrderLine.getSalesUnitPrice(), salesOrderLine.getOrderQuantity(), product, salesOrderLine.getTaxRate());

        return new SalesOrderLine(
                salesOrderLine.getOrderNumber(),
                salesOrderLine.getOrderLineNumber(),
                salesOrderLine.getProductCode(),
                salesOrderLine.getProductName(),
                salesOrderLine.getSalesUnitPrice(),
                salesOrderLine.getOrderQuantity(),
                salesOrderLine.getTaxRate(),
                salesOrderLine.getAllocationQuantity(),
                salesOrderLine.getShipmentInstructionQuantity(),
                salesOrderLine.getShippedQuantity(),
                salesOrderLine.getCompletionFlag(),
                salesOrderLine.getDiscountAmount(),
                salesOrderLine.getDeliveryDate(),
                product,
                salesCalculation.getSalesAmount(),
                salesCalculation.getConsumptionTaxAmount()
        );
    }

    public Money calcSalesAmount() {
        return Objects.requireNonNull(salesAmount).getValue();
    }

    public Money calcConsumptionTaxAmount() {
        return Objects.requireNonNull(consumptionTaxAmount).getValue();
    }
}