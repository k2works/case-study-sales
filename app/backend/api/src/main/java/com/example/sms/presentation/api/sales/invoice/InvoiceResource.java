package com.example.sms.presentation.api.sales.invoice;

import com.example.sms.domain.model.master.partner.PartnerCode;
import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceNumber;
import com.example.sms.domain.type.money.Money;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        return Optional.ofNullable(invoice)
                .map(inv -> {
                    InvoiceResource resource = new InvoiceResource();

                    Optional.ofNullable(inv.getInvoiceNumber())
                            .map(InvoiceNumber::getValue)
                            .ifPresent(resource::setInvoiceNumber);

                    resource.setInvoiceDate(inv.getInvoiceDate().getValue());
                    resource.setPartnerCode(inv.getPartnerCode().getValue());

                    Optional.ofNullable(inv.getCustomerCode())
                            .ifPresent(customerCode -> {
                                Optional.ofNullable(customerCode.getCode())
                                        .map(PartnerCode::getValue)
                                        .ifPresent(resource::setCustomerCode);
                                resource.setCustomerBranchNumber(customerCode.getBranchNumber());
                            });

                    Optional.ofNullable(inv.getPreviousPaymentAmount())
                            .map(Money::getAmount)
                            .ifPresent(resource::setPreviousPaymentAmount);

                    Optional.ofNullable(inv.getCurrentMonthSalesAmount())
                            .map(Money::getAmount)
                            .ifPresent(resource::setCurrentMonthSalesAmount);

                    Optional.ofNullable(inv.getCurrentMonthPaymentAmount())
                            .map(Money::getAmount)
                            .ifPresent(resource::setCurrentMonthPaymentAmount);

                    Optional.ofNullable(inv.getCurrentMonthInvoiceAmount())
                            .map(Money::getAmount)
                            .ifPresent(resource::setCurrentMonthInvoiceAmount);

                    Optional.ofNullable(inv.getConsumptionTaxAmount())
                            .map(Money::getAmount)
                            .ifPresent(resource::setConsumptionTaxAmount);

                    Optional.ofNullable(inv.getInvoiceReconciliationAmount())
                            .map(Money::getAmount)
                            .ifPresent(resource::setInvoiceReconciliationAmount);

                    Optional.ofNullable(inv.getInvoiceLines())
                            .map(InvoiceLineResource::from)
                            .ifPresent(resource::setInvoiceLines);

                    return resource;
                })
                .orElse(null);
    }
}