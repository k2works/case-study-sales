package com.example.sms.presentation.api.sales.payment;

import com.example.sms.domain.model.sales.payment.PaymentReceived;
import com.example.sms.domain.model.sales.payment.PaymentMethodType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Schema(description = "入金データ")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReceivedResource {
    @NotNull
    @Schema(description = "入金番号")
    private String paymentNumber;

    @NotNull
    @Schema(description = "入金日")
    private LocalDateTime paymentDate;

    @NotNull
    @Schema(description = "部門ID")
    private String departmentCode;

    @NotNull
    @Schema(description = "部門開始日")
    private LocalDateTime departmentStartDate;

    @NotNull
    @Schema(description = "顧客コード")
    private String customerCode;

    @NotNull
    @Schema(description = "顧客枝番")
    private Integer customerBranchNumber;

    @NotNull
    @Schema(description = "支払方法区分")
    private PaymentMethodType paymentMethodType;

    @NotNull
    @Schema(description = "入金口座コード")
    private String paymentAccountCode;

    @NotNull
    @Schema(description = "入金金額")
    private Integer paymentAmount;

    @NotNull
    @Schema(description = "消込金額")
    private Integer offsetAmount;

    @Schema(description = "顧客名")
    private String customerName;

    @Schema(description = "入金口座名")
    private String paymentAccountName;

    public static PaymentReceivedResource from(PaymentReceived paymentReceived) {
        return PaymentReceivedResource.builder()
                .paymentNumber(paymentReceived.getPaymentNumber().getValue())
                .paymentDate(paymentReceived.getPaymentDate())
                .departmentCode(paymentReceived.getDepartmentId().getDeptCode().getValue())
                .departmentStartDate(paymentReceived.getDepartmentId().getDepartmentStartDate().getValue())
                .customerCode(paymentReceived.getCustomerCode().getCode().getValue())
                .customerBranchNumber(paymentReceived.getCustomerCode().getBranchNumber())
                .paymentMethodType(paymentReceived.getPaymentMethodType())
                .paymentAccountCode(paymentReceived.getPaymentAccountCode())
                .paymentAmount(paymentReceived.getPaymentAmount().getAmount())
                .offsetAmount(paymentReceived.getOffsetAmount().getAmount())
                .customerName(paymentReceived.getCustomer() != null ? paymentReceived.getCustomer().getCustomerName().getValue().getName() : null)
                .paymentAccountName(paymentReceived.getPaymentAccount() != null ? paymentReceived.getPaymentAccount().getAccountName() : null)
                .build();
    }
}
