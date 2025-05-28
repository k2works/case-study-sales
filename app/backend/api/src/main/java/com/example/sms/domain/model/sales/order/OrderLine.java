package com.example.sms.domain.model.sales.order;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder(toBuilder = true)
public class OrderLine {
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
    ShippingDate shippingDate; // 出荷日
    Product product; // 商品
    SalesAmount salesAmount; // 販売価格
    ConsumptionTaxAmount consumptionTaxAmount; // 消費税額

    public static OrderLine of(String orderNumber, Integer orderLineNumber, String productCode, String productName, Integer salesUnitPrice, Integer salesQuantity, Integer taxRate, Integer allocationQuantity, Integer shipmentInstructionQuantity, Integer shippedQuantity, Integer completionFlag, Integer discountAmount, LocalDateTime deliveryDate, LocalDateTime shippingDate) {
        SalesCalculation salesCalculation = SalesCalculation.of(Money.of(salesUnitPrice), Quantity.of(salesQuantity), null, TaxRateType.of(taxRate));

        return new OrderLine(
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
                ShippingDate.of(shippingDate),
                null,
                salesCalculation.getSalesAmount(),
                salesCalculation.getConsumptionTaxAmount()
        );
    }

    public static OrderLine complete(OrderLine orderLine) {
        SalesCalculation salesCalculation = SalesCalculation.of(orderLine.getSalesUnitPrice(), orderLine.getOrderQuantity(), orderLine.getProduct(), orderLine.getTaxRate());

        return new OrderLine(
                orderLine.getOrderNumber(),
                orderLine.getOrderLineNumber(),
                orderLine.getProductCode(),
                orderLine.getProductName(),
                orderLine.getSalesUnitPrice(),
                orderLine.getOrderQuantity(),
                orderLine.getTaxRate(),
                orderLine.getAllocationQuantity(),
                orderLine.getShipmentInstructionQuantity(),
                orderLine.getShippedQuantity(),
                CompletionFlag.完了,
                orderLine.getDiscountAmount(),
                orderLine.getDeliveryDate(),
                orderLine.getShippingDate(),
                orderLine.getProduct(),
                salesCalculation.getSalesAmount(),
                salesCalculation.getConsumptionTaxAmount()
        );
    }

    public static OrderLine of(OrderLine orderLine, Product product) {
        SalesCalculation salesCalculation = SalesCalculation.of(orderLine.getSalesUnitPrice(), orderLine.getOrderQuantity(), product, orderLine.getTaxRate());

        return new OrderLine(
                orderLine.getOrderNumber(),
                orderLine.getOrderLineNumber(),
                orderLine.getProductCode(),
                orderLine.getProductName(),
                orderLine.getSalesUnitPrice(),
                orderLine.getOrderQuantity(),
                orderLine.getTaxRate(),
                orderLine.getAllocationQuantity(),
                orderLine.getShipmentInstructionQuantity(),
                orderLine.getShippedQuantity(),
                orderLine.getCompletionFlag(),
                orderLine.getDiscountAmount(),
                orderLine.getDeliveryDate(),
                orderLine.getShippingDate(),
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
