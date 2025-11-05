package com.example.sms.infrastructure.datasource.sales.invoice;

import com.example.sms.domain.model.sales.invoice.*;
import com.example.sms.infrastructure.datasource.autogen.model.請求データ;
import com.example.sms.infrastructure.datasource.autogen.model.請求データ明細;
import com.example.sms.infrastructure.datasource.autogen.model.請求データ明細Key;
import com.example.sms.infrastructure.datasource.sales.invoice.invoice_line.InvoiceLineCustomEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * 請求データエンティティマッパー
 */
@Component
public class InvoiceEntityMapper {

    /**
     * ドメインモデルからエンティティへの変換
     */
    public 請求データ mapToEntity(Invoice invoice) {
        if (invoice == null) {
            return new 請求データ();
        }

        請求データ invoiceEntity = new 請求データ();
        invoiceEntity.set請求番号(invoice.getInvoiceNumber().getValue());
        invoiceEntity.set請求日(invoice.getInvoiceDate().getValue());
        invoiceEntity.set取引先コード(invoice.getPartnerCode().getValue());

        if (invoice.getCustomerCode() != null) {
            invoiceEntity.set取引先コード(invoice.getCustomerCode().getCode().getValue());
            invoiceEntity.set顧客枝番(invoice.getCustomerCode().getBranchNumber());
        }

        invoiceEntity.set前回入金額(invoice.getPreviousPaymentAmount().getAmount());
        invoiceEntity.set当月売上額(invoice.getCurrentMonthSalesAmount().getAmount());
        invoiceEntity.set当月入金額(invoice.getCurrentMonthPaymentAmount().getAmount());
        invoiceEntity.set当月請求額(invoice.getCurrentMonthInvoiceAmount().getAmount());
        invoiceEntity.set消費税金額(invoice.getConsumptionTaxAmount().getAmount());
        invoiceEntity.set請求消込金額(invoice.getInvoiceReconciliationAmount().getAmount());

        return invoiceEntity;
    }

    /**
     * ドメインモデルからエンティティへの変換（明細）
     */
    public 請求データ明細 mapToEntity(請求データ明細Key key) {
        請求データ明細 invoiceLineEntity = new 請求データ明細();
        invoiceLineEntity.set請求番号(key.get請求番号());
        invoiceLineEntity.set売上番号(key.get売上番号());
        invoiceLineEntity.set売上行番号(key.get売上行番号());
        return invoiceLineEntity;
    }

    /**
     * 明細キーの作成
     */
    public 請求データ明細Key mapToKey(InvoiceLine invoiceLine) {
        請求データ明細Key key = new 請求データ明細Key();
        key.set請求番号(invoiceLine.getInvoiceNumber().getValue());
        key.set売上番号(invoiceLine.getSalesNumber().getValue());
        key.set売上行番号(invoiceLine.getSalesLineNumber());
        return key;
    }

    /**
     * エンティティからドメインモデルへの変換
     */
    public Invoice mapToDomainModel(InvoiceCustomEntity invoiceData) {
        Function<InvoiceLineCustomEntity, InvoiceLine> invoiceLineMapper = e -> {
            if (Objects.isNull(e)) {
                return InvoiceLine.of(null, null, null);
            }

            return InvoiceLine.of(
                    e.get請求番号(),
                    e.get売上番号(),
                    e.get売上行番号()
            );
        };

        return Invoice.of(
                invoiceData.get請求番号(),
                invoiceData.get請求日(),
                invoiceData.get取引先コード(),
                invoiceData.get顧客枝番(),
                invoiceData.get前回入金額(),
                invoiceData.get当月売上額(),
                invoiceData.get当月入金額(),
                invoiceData.get当月請求額(),
                invoiceData.get消費税金額(),
                invoiceData.get請求消込金額(),
                Optional.ofNullable(invoiceData.get請求データ明細())
                        .map(lines -> lines.stream()
                                .map(invoiceLineMapper)
                                .toList())
                        .orElse(new ArrayList<>())
        );
    }
}
