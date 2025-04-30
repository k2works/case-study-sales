package com.example.sms.domain.model.sales.shipping;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentCode;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.model.sales.order.*;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * 出荷
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Shipping {
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
    Integer orderLineNumber; // 受注行番号
    ProductCode productCode; // 商品コード
    String productName; // 商品名
    Money salesUnitPrice; // 販売単価
    Quantity orderQuantity; // 受注数量
    TaxRateType taxRate; // 消費税率
    Quantity allocationQuantity; // 引当数量
    Quantity shipmentInstructionQuantity; // 出荷指示数量
    Quantity shippedQuantity; // 出荷済数量
    CompletionFlag completionFlag; // 完了フラグ
    Money discountAmount; // 値引金額
    DeliveryDate deliveryDate; // 納期
    Product product; // 商品
    SalesAmount salesAmount; // 販売価格
    ConsumptionTaxAmount consumptionTaxAmount; // 消費税額
    Department department; // 部門
    Customer customer; // 顧客
    Employee employee; // 社員

    /**
     * 全ての属性を指定してShippingを生成するファクトリメソッド。
     */
    public static Shipping of(
            OrderNumber orderNumber,
            OrderDate orderDate,
            DepartmentCode departmentCode,
            LocalDateTime departmentStartDate,
            CustomerCode customerCode,
            EmployeeCode employeeCode,
            DesiredDeliveryDate desiredDeliveryDate,
            String customerOrderNumber,
            String warehouseCode,
            Money totalOrderAmount,
            Money totalConsumptionTax,
            String remarks,
            Integer orderLineNumber,
            ProductCode productCode,
            String productName,
            Money salesUnitPrice,
            Quantity orderQuantity,
            TaxRateType taxRate,
            Quantity allocationQuantity,
            Quantity shipmentInstructionQuantity,
            Quantity shippedQuantity,
            Money discountAmount,
            DeliveryDate deliveryDate,
            Product product,
            SalesAmount salesAmount,
            ConsumptionTaxAmount consumptionTaxAmount,
            Department department,
            Customer customer,
            Employee employee) {

        isTrue(orderQuantity.getAmount() >= shipmentInstructionQuantity.getAmount(), "受注数量より出荷指示数量が多いです。");
        isTrue(orderQuantity.getAmount() >= shippedQuantity.getAmount(), "受注数量より出荷済数量が多いです。");
        isTrue(shipmentInstructionQuantity.getAmount() >= shippedQuantity.getAmount(), "受注指示数量より出荷済数量が多いです。");

        CompletionFlag status = orderQuantity.getAmount() == shippedQuantity.getAmount() ? CompletionFlag.完了 : CompletionFlag.未完了;

        return new Shipping(
                orderNumber,
                orderDate,
                departmentCode,
                departmentStartDate,
                customerCode,
                employeeCode,
                desiredDeliveryDate,
                customerOrderNumber,
                warehouseCode,
                totalOrderAmount,
                totalConsumptionTax,
                remarks,
                orderLineNumber,
                productCode,
                productName,
                salesUnitPrice,
                orderQuantity,
                taxRate,
                allocationQuantity,
                shipmentInstructionQuantity,
                shippedQuantity,
                status,
                discountAmount,
                deliveryDate,
                product,
                salesAmount,
                consumptionTaxAmount,
                department,
                customer,
                employee
        );
    }
}