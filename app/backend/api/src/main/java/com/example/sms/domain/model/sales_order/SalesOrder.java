package com.example.sms.domain.model.sales_order;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 受注
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SalesOrder {
    String orderNumber; // 受注番号
    LocalDateTime orderDate; // 受注日
    String departmentCode; // 部門コード
    LocalDateTime departmentStartDate; // 部門開始日
    String customerCode; // 顧客コード
    Integer customerBranchNumber; // 顧客枝番
    String employeeCode; // 社員コード
    LocalDateTime desiredDeliveryDate; // 希望納期
    String customerOrderNumber; // 客先注文番号
    String warehouseCode; // 倉庫コード
    Integer totalOrderAmount; // 受注金額合計
    Integer totalConsumptionTax; // 消費税合計
    String remarks; // 備考
    List<SalesOrderLine> salesOrderLines; // 受注明細

    public static SalesOrder of(String orderNumber, LocalDateTime orderDate, String departmentCode, LocalDateTime departmentStartDate, String customerCode, Integer customerBranchNumber, String employeeCode, LocalDateTime desiredDeliveryDate, String customerOrderNumber, String warehouseCode, Integer totalOrderAmount, Integer totalConsumptionTax, String remarks, List<SalesOrderLine> salesOrderLines) {
        return new SalesOrder(orderNumber, orderDate, departmentCode, departmentStartDate, customerCode, customerBranchNumber, employeeCode, desiredDeliveryDate, customerOrderNumber, warehouseCode, totalOrderAmount, totalConsumptionTax, remarks, salesOrderLines);
    }

    public static SalesOrder of(SalesOrder order, List<SalesOrderLine> line) {
        return new SalesOrder(order.getOrderNumber(), order.getOrderDate(), order.getDepartmentCode(), order.getDepartmentStartDate(), order.getCustomerCode(), order.getCustomerBranchNumber(), order.getEmployeeCode(), order.getDesiredDeliveryDate(), order.getCustomerOrderNumber(), order.getWarehouseCode(), order.getTotalOrderAmount(), order.getTotalConsumptionTax(), order.getRemarks(), line);
    }
}