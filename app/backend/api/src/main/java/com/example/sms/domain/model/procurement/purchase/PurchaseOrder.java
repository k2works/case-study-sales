package com.example.sms.domain.model.procurement.purchase;

import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.supplier.Supplier;
import com.example.sms.domain.model.master.partner.supplier.SupplierCode;
import com.example.sms.domain.model.sales.order.OrderNumber;
import com.example.sms.domain.type.money.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * 発注
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class PurchaseOrder {
    PurchaseOrderNumber purchaseOrderNumber; // 発注番号
    PurchaseOrderDate purchaseOrderDate; // 発注日
    OrderNumber salesOrderNumber; // 受注番号
    SupplierCode supplierCode; // 仕入先コード
    EmployeeCode purchaseManagerCode; // 発注担当者コード
    DesignatedDeliveryDate designatedDeliveryDate; // 指定納期
    String warehouseCode; // 倉庫コード
    Money totalPurchaseAmount; // 発注金額合計
    Money totalConsumptionTax; // 消費税合計
    String remarks; // 備考
    List<PurchaseOrderLine> purchaseOrderLines; // 発注明細
    Supplier supplier; // 仕入先
    Employee purchaseManager; // 発注担当者

    public static PurchaseOrder of(String purchaseOrderNumber, LocalDateTime purchaseOrderDate, String salesOrderNumber, String supplierCode, Integer supplierBranchNumber, String purchaseManagerCode, LocalDateTime designatedDeliveryDate, String warehouseCode, Integer totalPurchaseAmount, Integer totalConsumptionTax, String remarks, List<PurchaseOrderLine> purchaseOrderLines) {
        isTrue(!purchaseOrderDate.isAfter(designatedDeliveryDate), "発注日は指定納期より前に設定してください");

        Money calcTotalPurchaseAmount = purchaseOrderLines.stream()
                .map(PurchaseOrderLine::calcPurchaseAmount)
                .reduce(Money.of(0), Money::plusMoney);

        Money calcTotalConsumptionTax = purchaseOrderLines.stream()
                .map(PurchaseOrderLine::calcConsumptionTax)
                .reduce(Money.of(0), Money::plusMoney);

        return PurchaseOrder.builder()
                .purchaseOrderNumber(PurchaseOrderNumber.of(purchaseOrderNumber))
                .purchaseOrderDate(PurchaseOrderDate.of(purchaseOrderDate))
                .salesOrderNumber(OrderNumber.of(salesOrderNumber))
                .supplierCode(SupplierCode.of(supplierCode, supplierBranchNumber))
                .purchaseManagerCode(EmployeeCode.of(purchaseManagerCode))
                .designatedDeliveryDate(DesignatedDeliveryDate.of(designatedDeliveryDate))
                .warehouseCode(warehouseCode)
                .totalPurchaseAmount(calcTotalPurchaseAmount)
                .totalConsumptionTax(calcTotalConsumptionTax)
                .remarks(remarks)
                .purchaseOrderLines(purchaseOrderLines)
                .build();
    }

    /**
     * 発注金額合計計算
     */
    public Money calcTotalPurchaseAmount() {
        return purchaseOrderLines.stream()
                .map(PurchaseOrderLine::calcPurchaseAmount)
                .reduce(Money.of(0), Money::plusMoney);
    }

    /**
     * 消費税合計計算
     */
    public Money calcTotalConsumptionTax() {
        return purchaseOrderLines.stream()
                .map(PurchaseOrderLine::calcConsumptionTax)
                .reduce(Money.of(0), Money::plusMoney);
    }
}