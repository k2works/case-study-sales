package com.example.sms.domain.model.sales;

import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.PartnerCode;
import com.example.sms.domain.model.order.OrderNumber;
import com.example.sms.domain.type.money.Money;
import lombok.AllArgsConstructor;
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
public class Sales {
    SalesNumber salesNumber; // 売上番号
    OrderNumber orderNumber; // 受注番号
    SalesDate salesDate; // 売上日
    SalesType salesType; // 売上区分
    DepartmentId departmentId; // 部門ID
    PartnerCode customerCode; // 取引先コード
    EmployeeCode employeeCode; // 社員コード
    Money totalSalesAmount; // 売上金額合計
    Money totalConsumptionTax; // 消費税合計
    String remarks; // 備考
    Integer voucherNumber; // 赤黒伝票番号
    String originalVoucherNumber; // 元伝票番号

    List<SalesLine> salesLines; // 売上明細

    /**
     * ファクトリーメソッド
     */
    public static Sales of(String salesNumber, String orderNumber, LocalDateTime salesDate, Integer salesType, String departmentCode, LocalDateTime departmentStartDate,
                           String customerCode, String employeeCode, Integer voucherNumber, String originalVoucherNumber,
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
                EmployeeCode.of(employeeCode),
                calcTotalSalesAmount,
                calcTotalConsumptionTax,
                remarks,
                voucherNumber,
                originalVoucherNumber,
                salesLines
        );
    }
}
