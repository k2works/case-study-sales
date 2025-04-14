package com.example.sms.presentation.api.shipping;

import com.example.sms.domain.model.master.department.DepartmentCode;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.model.sales_order.*;
import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import com.example.sms.service.shipping.ShippingCriteria;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class ShippingResourceDTOMapper {

    /**
     * ShippingResource を Shipping エンティティに変換
     */
    public static Shipping convertToEntity(ShippingResource resource) {
        if (resource == null) {
            return null; // resourceがnullの場合はnullを返す
        }

        return Shipping.of(
                OrderNumber.of(resource.getOrderNumber()),
                OrderDate.of(resource.getOrderDate()),
                DepartmentCode.of(resource.getDepartmentCode()),
                resource.getDepartmentStartDate(),
                CustomerCode.of(resource.getCustomerCode(), Optional.ofNullable(resource.getCustomerBranchNumber()).orElse(0)),
                EmployeeCode.of(resource.getEmployeeCode()),
                DesiredDeliveryDate.of(resource.getDesiredDeliveryDate()),
                resource.getCustomerOrderNumber(),
                resource.getWarehouseCode(),
                Money.of(Optional.ofNullable(resource.getTotalOrderAmount()).orElse(0)),
                Money.of(Optional.ofNullable(resource.getTotalConsumptionTax()).orElse(0)),
                resource.getRemarks(),
                resource.getOrderLineNumber(),
                ProductCode.of(resource.getProductCode()),
                resource.getProductName(),
                Money.of(Optional.ofNullable(resource.getSalesUnitPrice()).orElse(0)),
                Quantity.of(Optional.ofNullable(resource.getOrderQuantity()).orElse(0)),
                TaxRateType.of(Optional.ofNullable(resource.getTaxRate()).orElse(10)),
                Quantity.of(Optional.ofNullable(resource.getAllocationQuantity()).orElse(0)),
                Quantity.of(Optional.ofNullable(resource.getShipmentInstructionQuantity()).orElse(0)),
                Quantity.of(Optional.ofNullable(resource.getShippedQuantity()).orElse(0)),
                Money.of(Optional.ofNullable(resource.getDiscountAmount()).orElse(0)),
                DeliveryDate.of(resource.getDeliveryDate()),
                null, // product
                null, // salesAmount
                null, // consumptionTaxAmount
                null, // department
                null, // customer
                null  // employee
        );
    }

    /**
     * ShippingCriteriaResource を ShippingCriteria に変換
     */
    public static ShippingCriteria convertToCriteria(ShippingCriteriaResource resource) {
        if (resource == null) {
            return null;
        }

        LocalDateTime orderDate = parseDateTime(resource.getOrderDate());
        LocalDateTime departmentStartDate = parseDateTime(resource.getDepartmentStartDate());
        LocalDateTime desiredDeliveryDate = parseDateTime(resource.getDesiredDeliveryDate());
        LocalDateTime deliveryDate = parseDateTime(resource.getDeliveryDate());

        return ShippingCriteria.builder()
                .orderNumber(resource.getOrderNumber())
                .orderDate(orderDate)
                .departmentCode(resource.getDepartmentCode())
                .departmentStartDate(departmentStartDate)
                .customerCode(resource.getCustomerCode())
                .employeeCode(resource.getEmployeeCode())
                .desiredDeliveryDate(desiredDeliveryDate)
                .customerOrderNumber(resource.getCustomerOrderNumber())
                .warehouseCode(resource.getWarehouseCode())
                .remarks(resource.getRemarks())
                .orderLineNumber(resource.getOrderLineNumber())
                .productCode(resource.getProductCode())
                .productName(resource.getProductName())
                .deliveryDate(deliveryDate)
                .completionFlag(resource.getCompletionFlag())
                .build();
    }

    private static LocalDateTime parseDateTime(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return date.atStartOfDay();
        } catch (Exception e) {
            return null;
        }
    }
}
