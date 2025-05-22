package com.example.sms.presentation.api.sales.invoice;

import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceLine;
import com.example.sms.service.sales.invoice.InvoiceCriteria;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class InvoiceResourceDTOMapper {

    /**
     * InvoiceResource を Invoice エンティティに変換
     */
    public static Invoice convertToEntity(InvoiceResource resource) {
        if (resource == null) {
            return null;
        }

        List<InvoiceLine> invoiceLines = resource.getInvoiceLines() != null
                ? resource.getInvoiceLines().stream()
                .map(line -> line != null
                        ? InvoiceLine.of(
                        line.getInvoiceNumber(),
                        line.getSalesNumber(),
                        line.getSalesLineNumber()
                )
                        : null
                )
                .filter(Objects::nonNull)
                .toList()
                : null;

        return Invoice.of(
                Objects.equals(resource.getInvoiceNumber(), "") ? null : resource.getInvoiceNumber(),
                resource.getInvoiceDate(),
                resource.getPartnerCode(),
                resource.getCustomerBranchNumber(),
                Optional.ofNullable(resource.getPreviousPaymentAmount()).orElse(0),
                Optional.ofNullable(resource.getCurrentMonthSalesAmount()).orElse(0),
                Optional.ofNullable(resource.getCurrentMonthPaymentAmount()).orElse(0),
                Optional.ofNullable(resource.getCurrentMonthInvoiceAmount()).orElse(0),
                Optional.ofNullable(resource.getConsumptionTaxAmount()).orElse(0),
                Optional.ofNullable(resource.getInvoiceReconciliationAmount()).orElse(0),
                invoiceLines
        );
    }

    /**
     * Invoice エンティティを InvoiceResource に変換
     */
    public static InvoiceResource convertToResource(Invoice invoice) {
        if (invoice == null) {
            return null;
        }
        
        return InvoiceResource.from(invoice);
    }

    /**
     * InvoiceCriteriaResource を InvoiceCriteria に変換
     */
    public static InvoiceCriteria convertToCriteria(InvoiceCriteriaResource resource) {
        if (resource == null) {
            return null;
        }
        
        LocalDate invoiceDate = null;
        if (resource.getInvoiceDate() != null && !resource.getInvoiceDate().isEmpty()) {
            try {
                invoiceDate = LocalDate.parse(resource.getInvoiceDate());
            } catch (Exception e) {
                // 日付のパースに失敗した場合は null のままにする
            }
        }

        return InvoiceCriteria.builder()
                .invoiceNumber(resource.getInvoiceNumber())
                .invoiceDate(invoiceDate != null ? invoiceDate.toString() : null)
                .partnerCode(resource.getPartnerCode())
                .customerCode(resource.getCustomerCode())
                .build();
    }
}