package com.example.sms.domain.model.procurement.purchase;

import com.example.sms.domain.model.master.department.DepartmentCode;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.supplier.Supplier;
import com.example.sms.domain.model.master.partner.supplier.SupplierCode;
import com.example.sms.domain.model.procurement.order.PurchaseOrderNumber;
import com.example.sms.domain.type.money.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 仕入
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class Purchase {
    PurchaseNumber purchaseNumber; // 仕入番号
    PurchaseDate purchaseDate; // 仕入日
    SupplierCode supplierCode; // 仕入先コード
    EmployeeCode purchaseManagerCode; // 仕入担当者コード
    LocalDateTime startDate; // 開始日
    PurchaseOrderNumber purchaseOrderNumber; // 発注番号
    DepartmentCode departmentCode; // 部門コード
    Money totalPurchaseAmount; // 仕入金額合計
    Money totalConsumptionTax; // 消費税合計
    String remarks; // 備考
    List<PurchaseLine> purchaseLines; // 仕入明細
    Supplier supplier; // 仕入先
    Employee purchaseManager; // 仕入担当者

    public static Purchase of(String purchaseNumber, LocalDateTime purchaseDate, String supplierCode, Integer supplierBranchNumber, String purchaseManagerCode, LocalDateTime startDate, String purchaseOrderNumber, String departmentCode, Integer totalPurchaseAmount, Integer totalConsumptionTax, String remarks, List<PurchaseLine> purchaseLines) {
        Money calcTotalPurchaseAmount = purchaseLines.stream()
                .map(PurchaseLine::calcPurchaseAmount)
                .reduce(Money.of(0), Money::plusMoney);

        Money calcTotalConsumptionTax = purchaseLines.stream()
                .map(PurchaseLine::calcConsumptionTax)
                .reduce(Money.of(0), Money::plusMoney);

        return Purchase.builder()
                .purchaseNumber(PurchaseNumber.of(purchaseNumber))
                .purchaseDate(PurchaseDate.of(purchaseDate))
                .supplierCode(SupplierCode.of(supplierCode, supplierBranchNumber))
                .purchaseManagerCode(EmployeeCode.of(purchaseManagerCode))
                .startDate(startDate)
                .purchaseOrderNumber(purchaseOrderNumber != null ? PurchaseOrderNumber.of(purchaseOrderNumber) : null)
                .departmentCode(DepartmentCode.of(departmentCode))
                .totalPurchaseAmount(calcTotalPurchaseAmount)
                .totalConsumptionTax(calcTotalConsumptionTax)
                .remarks(remarks)
                .purchaseLines(purchaseLines)
                .build();
    }

    /**
     * 仕入金額合計計算
     */
    public Money calcTotalPurchaseAmount() {
        return purchaseLines.stream()
                .map(PurchaseLine::calcPurchaseAmount)
                .reduce(Money.of(0), Money::plusMoney);
    }

    /**
     * 消費税合計計算
     */
    public Money calcTotalConsumptionTax() {
        return purchaseLines.stream()
                .map(PurchaseLine::calcConsumptionTax)
                .reduce(Money.of(0), Money::plusMoney);
    }
}
