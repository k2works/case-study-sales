package com.example.sms.domain.model.sales.order;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.TaxType;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import lombok.Value;

/**
 * 売上金額計算
 */
@Value
public class SalesCalculation {
    SalesAmount salesAmount;
    ConsumptionTaxAmount consumptionTaxAmount;

    public static SalesCalculation of(
            Money salesUnitPrice,
            Quantity salesQuantity,
            Product product,
            TaxRateType taxRateType) {

        SalesAmount calcSalesAmount = SalesAmount.of(salesUnitPrice, salesQuantity);
        ConsumptionTaxAmount calcConsumptionTaxAmount = ConsumptionTaxAmount.of(calcSalesAmount, taxRateType, product);

        if (calcConsumptionTaxAmount.getTaxType().equals(TaxType.内税)) {
            Money salesAmount = calcSalesAmount.getValue().subtract(calcConsumptionTaxAmount.getValue());
            Money salesAmountPerUnit = salesAmount.divide(calcSalesAmount.getOrderQuantity());
            calcSalesAmount = new SalesAmount(salesAmountPerUnit, calcSalesAmount.getOrderQuantity());
        }

        return new SalesCalculation(calcSalesAmount, calcConsumptionTaxAmount);
    }
}