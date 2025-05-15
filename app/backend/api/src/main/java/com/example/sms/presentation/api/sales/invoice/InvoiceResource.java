package com.example.sms.presentation.api.sales.invoice;

import com.example.sms.domain.model.sales.invoice.Invoice;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Schema(description = "請求情報")
public class InvoiceResource {
    @Schema(description = "請求番号")
    String invoiceNumber;

    @Schema(description = "請求日")
    LocalDateTime invoiceDate;

    @Schema(description = "取引先コード")
    String partnerCode;

    @Schema(description = "顧客コード")
    String customerCode;

    @Schema(description = "顧客枝番")
    Integer customerBranchNumber;

    @Schema(description = "前回入金額")
    Integer previousPaymentAmount;

    @Schema(description = "当月売上額")
    Integer currentMonthSalesAmount;

    @Schema(description = "当月入金額")
    Integer currentMonthPaymentAmount;

    @Schema(description = "当月請求額")
    Integer currentMonthInvoiceAmount;

    @Schema(description = "消費税金額")
    Integer consumptionTaxAmount;

    @Schema(description = "請求消込金額")
    Integer invoiceReconciliationAmount;

    @Schema(description = "請求明細")
    List<InvoiceLineResource> invoiceLines;

    /**
     * Invoice エンティティをリソースにマッピングするメソッド
     */
    public static InvoiceResource from(Invoice invoice) {
        InvoiceResource resource = new InvoiceResource();
        resource.setInvoiceNumber(invoice.getInvoiceNumber().getValue());
        resource.setInvoiceDate(invoice.getInvoiceDate());
        resource.setPartnerCode(invoice.getPartnerCode());
        resource.setCustomerCode(invoice.getCustomerCode().getCode().getValue());
        resource.setCustomerBranchNumber(invoice.getCustomerCode().getBranchNumber());
        resource.setPreviousPaymentAmount(invoice.getPreviousPaymentAmount().getAmount());
        resource.setCurrentMonthSalesAmount(invoice.getCurrentMonthSalesAmount().getAmount());
        resource.setCurrentMonthPaymentAmount(invoice.getCurrentMonthPaymentAmount().getAmount());
        resource.setCurrentMonthInvoiceAmount(invoice.getCurrentMonthInvoiceAmount().getAmount());
        resource.setConsumptionTaxAmount(invoice.getConsumptionTaxAmount().getAmount());
        resource.setInvoiceReconciliationAmount(invoice.getInvoiceReconciliationAmount().getAmount());
        resource.setInvoiceLines(InvoiceLineResource.from(invoice.getInvoiceLines()));
        return resource;
    }
}