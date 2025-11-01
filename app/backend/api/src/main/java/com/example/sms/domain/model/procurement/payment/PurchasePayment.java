package com.example.sms.domain.model.procurement.payment;

import com.example.sms.domain.model.master.department.DepartmentCode;
import com.example.sms.domain.model.master.partner.supplier.SupplierCode;
import com.example.sms.domain.model.procurement.purchase.Purchase;
import com.example.sms.domain.type.money.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 支払が完了しているかどうかを判定する
     *
     * @return 支払完了している場合はtrue
     */
    public boolean isCompleted() {
        return Boolean.TRUE.equals(paymentCompletedFlag);
    }

    /**
     * 仕入データから支払データを集計する
     *
     * @param purchases 仕入データのリスト
     * @param paymentNumber 支払番号
     * @param paymentDate 支払日
     * @param paymentMethodType 支払方法区分
     * @return 集計された支払データ
     */
    public static PurchasePayment aggregateFromPurchases(
            List<Purchase> purchases,
            PurchasePaymentNumber paymentNumber,
            PurchasePaymentDate paymentDate,
            PurchasePaymentMethodType paymentMethodType
    ) {
        if (purchases == null || purchases.isEmpty()) {
            throw new IllegalArgumentException("仕入データが存在しません");
        }

        // 最初の仕入データから共通情報を取得
        Purchase firstPurchase = purchases.get(0);

        // 仕入金額と消費税を合計
        Money totalAmount = purchases.stream()
                .map(Purchase::getTotalPurchaseAmount)
                .reduce(Money.of(0), Money::plusMoney);

        Money totalTax = purchases.stream()
                .map(Purchase::getTotalConsumptionTax)
                .reduce(Money.of(0), Money::plusMoney);

        // 支払データを作成
        return PurchasePayment.builder()
                .paymentNumber(paymentNumber)
                .paymentDate(paymentDate)
                .departmentCode(firstPurchase.getDepartmentCode())
                .departmentStartDate(firstPurchase.getStartDate())
                .supplierCode(firstPurchase.getSupplierCode())
                .paymentMethodType(paymentMethodType)
                .paymentAmount(totalAmount)
                .totalConsumptionTax(totalTax)
                .paymentCompletedFlag(false)
                .build();
    }
}
