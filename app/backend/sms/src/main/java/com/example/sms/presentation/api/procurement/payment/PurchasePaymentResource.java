package com.example.sms.presentation.api.procurement.payment;

import com.example.sms.domain.model.procurement.payment.PurchasePayment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Schema(description = "支払データ")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchasePaymentResource {
    @NotNull
    @Schema(description = "支払番号")
    private String paymentNumber;

    @NotNull
    @Schema(description = "支払日")
    private Integer paymentDate;

    @NotNull
    @Schema(description = "部門コード")
    private String departmentCode;

    @NotNull
    @Schema(description = "部門開始日")
    private LocalDateTime departmentStartDate;

    @NotNull
    @Schema(description = "仕入先コード")
    private String supplierCode;

    @NotNull
    @Schema(description = "仕入先枝番")
    private Integer supplierBranchNumber;

    @NotNull
    @Schema(description = "支払方法区分")
    private Integer paymentMethodType;

    @NotNull
    @Schema(description = "支払金額")
    private Integer paymentAmount;

    @NotNull
    @Schema(description = "消費税合計")
    private Integer totalConsumptionTax;

    @Schema(description = "支払完了フラグ")
    private Boolean paymentCompletedFlag;

    @Schema(description = "仕入先名")
    private String supplierName;

    public static PurchasePaymentResource from(PurchasePayment payment) {
        return PurchasePaymentResource.builder()
                .paymentNumber(payment.getPaymentNumber().getValue())
                .paymentDate(payment.getPaymentDate().getValue())
                .departmentCode(payment.getDepartmentCode().getValue())
                .departmentStartDate(payment.getDepartmentStartDate())
                .supplierCode(payment.getSupplierCode().getCode().getValue())
                .supplierBranchNumber(payment.getSupplierCode().getBranchNumber())
                .paymentMethodType(payment.getPaymentMethodType().getCode())
                .paymentAmount(payment.getPaymentAmount().getAmount())
                .totalConsumptionTax(payment.getTotalConsumptionTax().getAmount())
                .paymentCompletedFlag(payment.getPaymentCompletedFlag())
                .build();
    }
}
