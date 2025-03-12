package com.example.sms.presentation.api.sales_order;

import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;
import com.example.sms.service.sales_order.SalesOrderCriteria;

import java.time.LocalDateTime;
import java.util.List;

public class SalesOrderResourceDTOMapper {

    public static SalesOrder convertToEntity(SalesOrderResource resource) {
        List<SalesOrderLine> salesOrderLines = resource.getSalesOrderLines() != null
                ? resource.getSalesOrderLines().stream().map(line -> SalesOrderLine.of(
                        line.getOrderNumber(),
                        line.getOrderLineNumber(),
                        line.getProductCode(),
                        line.getProductName(),
                        line.getSalesUnitPrice(),
                        line.getOrderQuantity(),
                        line.getTaxRate().getRate(),
                        line.getAllocationQuantity(),
                        line.getShipmentInstructionQuantity(),
                        line.getShippedQuantity(),
                        line.getCompletionFlag().getValue(),
                        line.getDiscountAmount(),
                        line.getDeliveryDate()
                )).toList()
                : null;

        return SalesOrder.of(
                resource.getOrderNumber(),
                resource.getOrderDate(),
                resource.getDepartmentCode(),
                resource.getDepartmentStartDate(),
                resource.getCustomerCode(),
                resource.getCustomerBranchNumber(),
                resource.getEmployeeCode(),
                resource.getDesiredDeliveryDate(),
                resource.getCustomerOrderNumber(),
                resource.getWarehouseCode(),
                resource.getTotalOrderAmount(),
                resource.getTotalConsumptionTax(),
                resource.getRemarks(),
                salesOrderLines
        );
    }

    public static SalesOrderCriteria convertToCriteria(SalesOrderCriteriaResource resource) {
        LocalDateTime orderDate = resource.getOrderDate() != null ? resource.getOrderDate() : null;
        LocalDateTime departmentStartDate = resource.getDepartmentStartDate() != null ? resource.getDepartmentStartDate() : null;
        LocalDateTime desiredDeliveryDate = resource.getDesiredDeliveryDate() != null ? resource.getDesiredDeliveryDate() : null;

        return SalesOrderCriteria.builder()
                .orderNumber(resource.getOrderNumber())
                .orderDate(orderDate)
                .departmentCode(resource.getDepartmentCode())
                .departmentStartDate(departmentStartDate)
                .customerCode(resource.getCustomerCode())
                .customerBranchNumber(resource.getCustomerBranchNumber())
                .employeeCode(resource.getEmployeeCode())
                .desiredDeliveryDate(desiredDeliveryDate)
                .customerOrderNumber(resource.getCustomerOrderNumber())
                .warehouseCode(resource.getWarehouseCode())
                .remarks(resource.getRemarks())
                .build();
    }
}