package com.example.sms.presentation.api.sales.payment.incoming;

import com.example.sms.domain.model.sales.payment.incoming.Payment;
import com.example.sms.domain.model.sales.payment.incoming.PaymentMethodType;
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
public class PaymentResource {
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
    private String paymentMethodType;

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

    public static PaymentResource from(Payment payment) {
        return PaymentResource.builder()
                .paymentNumber(payment.getPaymentNumber().getValue())
                .paymentDate(payment.getPaymentDate())
                .departmentCode(payment.getDepartmentId().getDeptCode().getValue())
                .departmentStartDate(payment.getDepartmentId().getDepartmentStartDate().getValue())
                .customerCode(payment.getCustomerCode().getCode().getValue())
                .customerBranchNumber(payment.getCustomerCode().getBranchNumber())
                .paymentMethodType(payment.getPaymentMethodType().getCode().toString())
                .paymentAccountCode(payment.getPaymentAccountCode())
                .paymentAmount(payment.getPaymentAmount().getAmount())
                .offsetAmount(payment.getOffsetAmount().getAmount())
                .customerName(payment.getCustomer() != null ? payment.getCustomer().getCustomerName().getValue().getName() : null)
                .paymentAccountName(payment.getPaymentAccount() != null ? payment.getPaymentAccount().getAccountName() : null)
                .build();
    }
}
