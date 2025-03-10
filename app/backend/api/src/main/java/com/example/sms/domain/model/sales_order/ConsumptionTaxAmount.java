package com.example.sms.domain.model.sales_order;

import com.example.sms.domain.type.money.Money;
import lombok.Value;

/**
 * 消費税額
 */
@Value
public class ConsumptionTaxAmount {
    Money value;
    SalesAmount salesAmount; // 販売価格
    Double taxRate; // 消費税率

    public ConsumptionTaxAmount(SalesAmount salesAmount, TaxRateType taxRate) {
        this.salesAmount = salesAmount;
        this.taxRate = taxRate.getRate().doubleValue();

        double taxRateValue = getSalesAmount().getValue().getAmount() * (this.taxRate / 100);
        int truncatedTaxRateValue = (int) Math.floor(taxRateValue);
        this.value = Money.of(truncatedTaxRateValue);
    }

    public static ConsumptionTaxAmount of(SalesAmount salesAmount, TaxRateType taxRateType) {
        return new ConsumptionTaxAmount(salesAmount, taxRateType);
    }
}
