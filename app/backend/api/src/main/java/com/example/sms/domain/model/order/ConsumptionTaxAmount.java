package com.example.sms.domain.model.order;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.TaxType;
import com.example.sms.domain.type.money.Money;
import lombok.Value;

import java.util.function.Supplier;

/**
 * 消費税額
 */
@Value
public class ConsumptionTaxAmount {
    Money value;
    SalesAmount salesAmount; // 販売価格
    Double taxRate; // 消費税率
    TaxType taxType; // 税区分

    public ConsumptionTaxAmount(SalesAmount salesAmount, TaxRateType taxRate, Product product) {
        this.salesAmount = salesAmount;
        this.taxRate = taxRate.getRate().doubleValue();
        this.taxType = product != null ? product.getTaxType() : TaxType.外税;

        Supplier<Money> calcValue = () ->
            switch (this.taxType) {
                case 外税 -> {
                    double taxRateValue = getSalesAmount().getValue().getAmount() * (this.taxRate / 100);
                    int truncatedTaxRateValue = (int) Math.floor(taxRateValue);
                    yield Money.of(truncatedTaxRateValue);
                }
                case 内税 -> {
                    double taxRateValue = getSalesAmount().getValue().getAmount() * (this.taxRate / 100) * (1 / (1 + this.taxRate / 100));
                    int truncatedTaxRateValue = (int) Math.floor(taxRateValue);
                    yield Money.of(truncatedTaxRateValue);
                }
                case 非課税 -> Money.of(0);
                default -> Money.of(0);
            };

        this.value = calcValue.get();
    }

    public static ConsumptionTaxAmount of(SalesAmount salesAmount, TaxRateType taxRateType) {
        return new ConsumptionTaxAmount(salesAmount, taxRateType, null);
    }

    public static ConsumptionTaxAmount of(SalesAmount salesAmount, TaxRateType taxRateType, Product product) {
        return new ConsumptionTaxAmount(salesAmount, taxRateType, product);
    }
}
