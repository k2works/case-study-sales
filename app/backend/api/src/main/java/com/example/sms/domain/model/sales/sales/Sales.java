package com.example.sms.domain.model.sales.sales;

import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.PartnerCode;
import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.sales.order.OrderNumber;
import com.example.sms.domain.type.money.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 売上
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class Sales {
    SalesNumber salesNumber; // 売上番号
    OrderNumber orderNumber; // 受注番号
    SalesDate salesDate; // 売上日
    SalesType salesType; // 売上区分
    DepartmentId departmentId; // 部門ID
    PartnerCode partnerCode; // 取引先コード
    CustomerCode customerCode; // 顧客コード
    EmployeeCode employeeCode; // 社員コード
    Money totalSalesAmount; // 売上金額合計
    Money totalConsumptionTax; // 消費税合計
    String remarks; // 備考
    Integer voucherNumber; // 赤黒伝票番号
    String originalVoucherNumber; // 元伝票番号

    List<SalesLine> salesLines; // 売上明細

    Customer customer;
    Employee employee;

    /**
     * ファクトリーメソッド
     */
    public static Sales of(String salesNumber, String orderNumber, LocalDateTime salesDate, Integer salesType, String departmentCode, LocalDateTime departmentStartDate,
                           String customerCode, Integer customerBranchNumber, String employeeCode, Integer voucherNumber, String originalVoucherNumber,
                           String remarks, List<SalesLine> salesLines) {
        Money calcTotalSalesAmount = salesLines.stream()
                .map(SalesLine::calcSalesAmount)
                .reduce(Money.of(0), Money::plusMoney);
        Money calcTotalConsumptionTax = salesLines.stream()
                .map(SalesLine::calcConsumptionTaxAmount)
                .reduce(Money.of(0), Money::plusMoney);

        SalesNumber salesNumberValueObject = salesNumber == null ? null : SalesNumber.of(salesNumber);

        SalesType salesTypeValueObject = salesType == null ? SalesType.現金 : SalesType.fromCode(salesType);

        return new Sales(
                salesNumberValueObject,
                OrderNumber.of(orderNumber),
                SalesDate.of(salesDate),
                salesTypeValueObject,
                DepartmentId.of(departmentCode, departmentStartDate),
                PartnerCode.of(customerCode),
                CustomerCode.of(customerCode, customerBranchNumber),
                EmployeeCode.of(employeeCode),
                calcTotalSalesAmount,
                calcTotalConsumptionTax,
                remarks,
                voucherNumber,
                originalVoucherNumber,
                salesLines,
                null,
                null
        );
    }

    public static Sales of(Sales sales, Customer customer, Employee employee) {
        return new Sales(
                sales.getSalesNumber(),
                sales.getOrderNumber(),
                sales.getSalesDate(),
                sales.getSalesType(),
                sales.getDepartmentId(),
                sales.getPartnerCode(),
                sales.getCustomerCode(),
                sales.getEmployeeCode(),
                sales.getTotalSalesAmount(),
                sales.getTotalConsumptionTax(),
                sales.getRemarks(),
                sales.getVoucherNumber(),
                sales.getOriginalVoucherNumber(),
                sales.getSalesLines(),
                customer,
                employee
        );
    }
}
