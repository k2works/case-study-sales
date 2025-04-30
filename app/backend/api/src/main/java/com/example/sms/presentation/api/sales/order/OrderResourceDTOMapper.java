package com.example.sms.presentation.api.sales.order;

import com.example.sms.domain.model.sales.order.Order;
import com.example.sms.domain.model.sales.order.OrderLine;
import com.example.sms.service.sales.order.SalesOrderCriteria;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResourceDTOMapper {

    public static Order convertToEntity(OrderResource resource) {
        List<OrderLine> orderLines = resource.getSalesOrderLines() != null
                ? resource.getSalesOrderLines().stream().map(line -> OrderLine.of(
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

        return Order.of(
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
                orderLines
        );
    }

    public static SalesOrderCriteria convertToCriteria(OrderCriteriaResource resource) {
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