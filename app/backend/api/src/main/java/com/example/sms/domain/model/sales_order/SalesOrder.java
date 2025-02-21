package com.example.sms.domain.model.sales_order;

import com.example.sms.domain.model.master.department.DepartmentCode;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.type.money.Money;
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
    OrderNumber orderNumber; // 受注番号
    OrderDate orderDate; // 受注日
    DepartmentCode departmentCode; // 部門コード
    LocalDateTime departmentStartDate; // 部門開始日
    CustomerCode customerCode; // 顧客コード
    String employeeCode; // 社員コード
    DesiredDeliveryDate desiredDeliveryDate; // 希望納期
    String customerOrderNumber; // 客先注文番号
    String warehouseCode; // 倉庫コード
    Money totalOrderAmount; // 受注金額合計
    Money totalConsumptionTax; // 消費税合計
    String remarks; // 備考
    List<SalesOrderLine> salesOrderLines; // 受注明細

    public static SalesOrder of(String orderNumber, LocalDateTime orderDate, String departmentCode, LocalDateTime departmentStartDate, String customerCode, Integer customerBranchNumber, String employeeCode, LocalDateTime desiredDeliveryDate, String customerOrderNumber, String warehouseCode, Integer totalOrderAmount, Integer totalConsumptionTax, String remarks, List<SalesOrderLine> salesOrderLines) {
        return new SalesOrder(OrderNumber.of(orderNumber), OrderDate.of(orderDate), DepartmentCode.of(departmentCode), departmentStartDate, CustomerCode.of(customerCode, customerBranchNumber), employeeCode, DesiredDeliveryDate.of(desiredDeliveryDate), customerOrderNumber, warehouseCode, Money.of(totalOrderAmount), Money.of(totalConsumptionTax), remarks, salesOrderLines);
    }

    public static SalesOrder of(SalesOrder order, List<SalesOrderLine> line) {
        return new SalesOrder(order.getOrderNumber(), order.getOrderDate(), order.getDepartmentCode(), order.getDepartmentStartDate(), order.getCustomerCode(), order.getEmployeeCode(), order.getDesiredDeliveryDate(), order.getCustomerOrderNumber(), order.getWarehouseCode(), order.getTotalOrderAmount(), order.getTotalConsumptionTax(), order.getRemarks(), line);
    }
}