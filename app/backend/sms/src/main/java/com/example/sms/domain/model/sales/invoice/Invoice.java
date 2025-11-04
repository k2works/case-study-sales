package com.example.sms.domain.model.sales.invoice;

import com.example.sms.domain.model.master.partner.PartnerCode;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.type.money.Money;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 請求
 */
@Value
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Invoice {
    final InvoiceNumber invoiceNumber; // 請求番号
    final InvoiceDate invoiceDate; // 請求日
    final PartnerCode partnerCode; // 取引先コード
    final CustomerCode customerCode; // 顧客コード
    final Money previousPaymentAmount; // 前回入金額
    final Money currentMonthSalesAmount; // 当月売上額
    final Money currentMonthPaymentAmount; // 当月入金額
    final Money currentMonthInvoiceAmount; // 当月請求額
    final Money consumptionTaxAmount; // 消費税金額
    final Money invoiceReconciliationAmount; // 請求消込金額
    final List<InvoiceLine> invoiceLines; // 請求明細

    /**
     * ファクトリメソッド
     */
    public static Invoice of(
            String invoiceNumber,
            LocalDateTime invoiceDate,
            String partnerCode,
            Integer customerBranchNumber,
            Integer previousPaymentAmount,
            Integer currentMonthSalesAmount,
            Integer currentMonthPaymentAmount,
            Integer currentMonthInvoiceAmount,
            Integer consumptionTaxAmount,
            Integer invoiceReconciliationAmount,
            List<InvoiceLine> invoiceLines
    ) {
        return new Invoice(
                new InvoiceNumber(invoiceNumber),
                InvoiceDate.of(invoiceDate),
                PartnerCode.of(partnerCode),
                CustomerCode.of(partnerCode,customerBranchNumber),
                Money.of(previousPaymentAmount),
                Money.of(currentMonthSalesAmount),
                Money.of(currentMonthPaymentAmount),
                Money.of(currentMonthInvoiceAmount),
                Money.of(consumptionTaxAmount),
                Money.of(invoiceReconciliationAmount),
                invoiceLines != null ? invoiceLines : new ArrayList<>()
        );
    }

    /**
     * 請求明細を追加
     */
    public Invoice addInvoiceLine(InvoiceLine invoiceLine) {
        List<InvoiceLine> newInvoiceLines = new ArrayList<>(this.invoiceLines);
        newInvoiceLines.add(invoiceLine);
        return new Invoice(
                this.invoiceNumber,
                this.invoiceDate,
                this.partnerCode,
                this.customerCode,
                this.previousPaymentAmount,
                this.currentMonthSalesAmount,
                this.currentMonthPaymentAmount,
                this.currentMonthInvoiceAmount,
                this.consumptionTaxAmount,
                this.invoiceReconciliationAmount,
                newInvoiceLines
        );
    }
}