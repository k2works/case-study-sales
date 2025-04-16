package com.example.sms.domain.model.sales;

import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.model.sales_order.ConsumptionTaxAmount;
import com.example.sms.domain.model.sales_order.SalesAmount;
import com.example.sms.domain.model.sales_order.TaxRateType;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

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
    Integer billingDelayCategory; // 請求遅延区分
    LocalDateTime autoJournalDate; // 自動仕訳日

    /** 明細金額 (売上金額) */
    SalesAmount salesAmount;

    /** 消費税額 */
    ConsumptionTaxAmount consumptionTaxAmount;

    // ファクトリーメソッド
    public static SalesLine of(String salesNumber, Integer salesLineNumber, String productCode, String productName,
                               Integer salesUnitPrice, Integer salesQuantity, Integer shippedQuantity,
                               Integer discountAmount, LocalDateTime billingDate, String billingNumber,
                               Integer billingDelayCategory, LocalDateTime autoJournalDate) {

        // 必要な計算ロジックを追加
        SalesAmount calcSalesAmount = SalesAmount.of(Money.of(salesUnitPrice), Quantity.of(salesQuantity));
        //TODO: 商品マスタから税率を取得するロジックを追加
        ConsumptionTaxAmount calcConsumptionTaxAmount = ConsumptionTaxAmount.of(calcSalesAmount, TaxRateType.of(10)); // 例: 10%固定税率

        return new SalesLine(
                SalesNumber.of(salesNumber),
                salesLineNumber,
                ProductCode.of(productCode),
                productName,
                Money.of(salesUnitPrice),
                Quantity.of(salesQuantity),
                Quantity.of(shippedQuantity),
                Money.of(discountAmount),
                billingDate == null ? null : BillingDate.of(billingDate),
                billingNumber == null ? null : BillingNumber.of(billingNumber),
                billingDelayCategory,
                autoJournalDate,
                calcSalesAmount,
                calcConsumptionTaxAmount
        );
    }

    public static SalesLine of(SalesLine salesLine) {
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
                salesLine.getBillingDelayCategory(),
                salesLine.getAutoJournalDate(),
                SalesAmount.of(salesLine.getSalesUnitPrice(), salesLine.getSalesQuantity()),
                //TODO: 商品マスタから税率を取得するロジックを追加
                ConsumptionTaxAmount.of(SalesAmount.of(salesLine.getSalesUnitPrice(), salesLine.getSalesQuantity()), TaxRateType.of(10))
        );
    }

    // 明細の計算メソッド
    public Money calcSalesAmount() {
        return salesAmount.getValue();
    }

    public Money calcConsumptionTaxAmount() {
        return consumptionTaxAmount.getValue();
    }

    // 完了済み明細の生成
    public static SalesLine complete(SalesLine salesLine) {
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
                salesLine.getBillingDelayCategory(),
                salesLine.getAutoJournalDate(),
                SalesAmount.of(salesLine.getSalesUnitPrice(), salesLine.getSalesQuantity()),
                ConsumptionTaxAmount.of(SalesAmount.of(salesLine.getSalesUnitPrice(), salesLine.getSalesQuantity()), TaxRateType.of(10)) // 固定税率例
        );
    }
}
