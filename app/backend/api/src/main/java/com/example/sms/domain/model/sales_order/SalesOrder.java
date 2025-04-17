package com.example.sms.domain.model.sales_order;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentCode;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.type.money.Money;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;

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
    EmployeeCode employeeCode; // 社員コード
    DesiredDeliveryDate desiredDeliveryDate; // 希望納期
    String customerOrderNumber; // 客先注文番号
    String warehouseCode; // 倉庫コード
    Money totalOrderAmount; // 受注金額合計
    Money totalConsumptionTax; // 消費税合計
    String remarks; // 備考
    List<SalesOrderLine> salesOrderLines; // 受注明細
    Department department; // 部門
    Customer customer; // 顧客
    Employee employee; // 社員

    public static SalesOrder of(String orderNumber, LocalDateTime orderDate, String departmentCode, LocalDateTime departmentStartDate, String customerCode, Integer customerBranchNumber, String employeeCode, LocalDateTime desiredDeliveryDate, String customerOrderNumber, String warehouseCode, Integer totalOrderAmount, Integer totalConsumptionTax, String remarks, List<SalesOrderLine> salesOrderLines) {
        isTrue(!orderDate.isAfter(desiredDeliveryDate), "受注日は納品希望日より前に設定してください");

        Money calcTotalOrderAmount = salesOrderLines.stream()
                .map(SalesOrderLine::calcSalesAmount)
                .reduce(Money.of(0), Money::plusMoney);

        Money calcTotalConsumptionTax = salesOrderLines.stream()
                .map(SalesOrderLine::calcConsumptionTaxAmount)
                .reduce(Money.of(0), Money::plusMoney);

        OrderNumber orderNumberValueObject = orderNumber == null ? null : OrderNumber.of(orderNumber);

        return new SalesOrder(orderNumberValueObject, OrderDate.of(orderDate), DepartmentCode.of(departmentCode), departmentStartDate, CustomerCode.of(customerCode, customerBranchNumber), EmployeeCode.of(employeeCode), DesiredDeliveryDate.of(desiredDeliveryDate), customerOrderNumber, warehouseCode, calcTotalOrderAmount, calcTotalConsumptionTax, remarks, salesOrderLines, null, null, null);
    }

    public static SalesOrder of(SalesOrder order, List<SalesOrderLine> line) {
        return SalesOrder.of(
                order.getOrderNumber().getValue(),
                order.getOrderDate().getValue(),
                order.getDepartmentCode().getValue(),
                order.getDepartmentStartDate(),
                order.getCustomerCode().getCode().getValue(),
                order.getCustomerCode().getBranchNumber(),
                order.getEmployeeCode().getValue(),
                order.getDesiredDeliveryDate().getValue(),
                order.getCustomerOrderNumber(),
                order.getWarehouseCode(),
                order.getTotalOrderAmount().getAmount(),
                order.getTotalConsumptionTax().getAmount(),
                order.getRemarks(),
                line
        );
    }

    public static SalesOrder of(SalesOrder order, Department department, Customer customer, Employee employee) {
        return new SalesOrder(
                order.getOrderNumber(),
                order.getOrderDate(),
                order.getDepartmentCode(),
                order.getDepartmentStartDate(),
                order.getCustomerCode(),
                order.getEmployeeCode(),
                order.getDesiredDeliveryDate(),
                order.getCustomerOrderNumber(),
                order.getWarehouseCode(),
                order.getTotalOrderAmount(),
                order.getTotalConsumptionTax(),
                order.getRemarks(),
                order.getSalesOrderLines(),
                department,
                customer,
                employee
        );
    }
}