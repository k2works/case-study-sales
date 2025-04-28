package com.example.sms.domain.model.sales;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.model.order.ConsumptionTaxAmount;
import com.example.sms.domain.model.order.SalesAmount;
import com.example.sms.domain.model.order.SalesCalculation;
import com.example.sms.domain.model.order.TaxRateType;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 売上明細
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SalesLine {
    SalesNumber salesNumber; // 売上番号
    Integer salesLineNumber; // 売上行番号
    ProductCode productCode; // 商品コード
    String productName; // 商品名
    Money salesUnitPrice; // 販売単価
    Quantity salesQuantity; // 売上数量
    Quantity shippedQuantity; // 出荷数量
    Money discountAmount; // 値引金額
    BillingDate billingDate; // 請求日
    BillingNumber billingNumber; // 請求番号
    BillingDelayType billingDelayType; // 請求遅延区分
    AutoJournalDate autoJournalDate; // 自動仕訳日
    /** 明細金額 (売上金額) */
    SalesAmount salesAmount;
    /** 消費税額 */
    ConsumptionTaxAmount consumptionTaxAmount;
    Product product; // 商品マスタ情報
    TaxRateType taxRate; // 消費税率

    public static SalesLine of(String salesNumber, Integer salesLineNumber, String productCode, String productName,
                               Integer salesUnitPrice, Integer salesQuantity, Integer shippedQuantity,
                               Integer discountAmount, LocalDateTime billingDate, String billingNumber,
                               Integer billingDelayCategory, LocalDateTime autoJournalDate, Product product, TaxRateType taxRate) {
        SalesCalculation salesCalculation = SalesCalculation.of(Money.of(salesUnitPrice), Quantity.of(salesQuantity), product, taxRate);

        SalesNumber salesNumberValueObject = salesNumber == null ? null : SalesNumber.of(salesNumber);

        return new SalesLine(
                salesNumberValueObject,
                salesLineNumber,
                ProductCode.of(productCode),
                productName,
                Money.of(salesUnitPrice),
                Quantity.of(salesQuantity),
                Quantity.of(shippedQuantity),
                Money.of(discountAmount),
                billingDate == null ? null : BillingDate.of(billingDate),
                billingNumber == null ? null : BillingNumber.of(billingNumber),
                billingDelayCategory == null ? null : BillingDelayType.fromCode(billingDelayCategory),
                autoJournalDate == null ? null : AutoJournalDate.of(autoJournalDate),
                salesCalculation.getSalesAmount(),
                salesCalculation.getConsumptionTaxAmount(),
                product,
                taxRate
        );
    }

    public static SalesLine of(SalesLine salesLine) {
        SalesCalculation salesCalculation = SalesCalculation.of(salesLine.getSalesUnitPrice(), salesLine.getSalesQuantity(), salesLine.getProduct(), salesLine.getTaxRate());

        return new SalesLine(
                salesLine.getSalesNumber(),
                salesLine.getSalesLineNumber(),
                salesLine.getProductCode(),
                salesLine.getProductName(),
                salesLine.getSalesUnitPrice(),
                salesLine.getSalesQuantity(),
                salesLine.getShippedQuantity(),
                salesLine.getDiscountAmount(),
                salesLine.getBillingDate(),
                salesLine.getBillingNumber(),
                salesLine.getBillingDelayType(),
                salesLine.getAutoJournalDate(),
                salesCalculation.getSalesAmount(),
                salesCalculation.getConsumptionTaxAmount(),
                salesLine.getProduct(),
                salesLine.getTaxRate()
        );
    }

    // 明細の計算メソッド
    public Money calcSalesAmount() {
        return Objects.requireNonNull(salesAmount).getValue();
    }

    public Money calcConsumptionTaxAmount() {
        return Objects.requireNonNull(consumptionTaxAmount).getValue();
    }

    // 完了済み明細の生成
    public static SalesLine complete(SalesLine salesLine) {
        SalesCalculation salesCalculation = SalesCalculation.of(salesLine.getSalesUnitPrice(), salesLine.getSalesQuantity(), salesLine.getProduct(), salesLine.getTaxRate());

        return new SalesLine(
                salesLine.getSalesNumber(),
                salesLine.getSalesLineNumber(),
                salesLine.getProductCode(),
                salesLine.getProductName(),
                salesLine.getSalesUnitPrice(),
                salesLine.getSalesQuantity(),
                salesLine.getShippedQuantity(),
                salesLine.getDiscountAmount(),
                salesLine.getBillingDate(),
                salesLine.getBillingNumber(),
                salesLine.getBillingDelayType(),
                salesLine.getAutoJournalDate(),
                salesCalculation.getSalesAmount(),
                salesCalculation.getConsumptionTaxAmount(),
                salesLine.getProduct(),
                salesLine.getTaxRate()
        );
    }
}
