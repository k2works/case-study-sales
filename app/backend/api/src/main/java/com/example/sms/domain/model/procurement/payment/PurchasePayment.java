package com.example.sms.domain.model.procurement.payment;

import com.example.sms.domain.model.master.department.DepartmentCode;
import com.example.sms.domain.model.master.partner.supplier.SupplierCode;
import com.example.sms.domain.type.money.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 支払
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class PurchasePayment {
    PurchasePaymentNumber paymentNumber; // 支払番号
    PurchasePaymentDate paymentDate; // 支払日
    DepartmentCode departmentCode; // 部門コード
    LocalDateTime departmentStartDate; // 部門開始日
    SupplierCode supplierCode; // 仕入先コード
    PurchasePaymentMethodType paymentMethodType; // 支払方法区分
    Money paymentAmount; // 支払金額
    Money totalConsumptionTax; // 消費税合計
    Boolean paymentCompletedFlag; // 支払完了フラグ
    LocalDateTime createdDateTime; // 作成日時
    String createdBy; // 作成者名
    LocalDateTime updatedDateTime; // 更新日時
    String updatedBy; // 更新者名

    public static PurchasePayment of(
            String paymentNumber,
            Integer paymentDate,
            String departmentCode,
            LocalDateTime departmentStartDate,
            String supplierCode,
            Integer supplierBranchNumber,
            Integer paymentMethodType,
            Integer paymentAmount,
            Integer totalConsumptionTax,
            Boolean paymentCompletedFlag
    ) {
        return PurchasePayment.builder()
                .paymentNumber(PurchasePaymentNumber.of(paymentNumber))
                .paymentDate(PurchasePaymentDate.of(paymentDate))
                .departmentCode(DepartmentCode.of(departmentCode))
                .departmentStartDate(departmentStartDate)
                .supplierCode(SupplierCode.of(supplierCode, supplierBranchNumber))
                .paymentMethodType(PurchasePaymentMethodType.fromCode(paymentMethodType))
                .paymentAmount(Money.of(paymentAmount))
                .totalConsumptionTax(Money.of(totalConsumptionTax))
                .paymentCompletedFlag(paymentCompletedFlag)
                .build();
    }
}
