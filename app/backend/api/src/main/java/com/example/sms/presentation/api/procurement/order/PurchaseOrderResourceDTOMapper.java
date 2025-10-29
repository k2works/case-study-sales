package com.example.sms.presentation.api.procurement.order;

import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.supplier.SupplierCode;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.model.procurement.order.*;
import com.example.sms.domain.model.sales.order.OrderNumber;
import com.example.sms.domain.type.money.Money;
import com.example.sms.service.procurement.order.PurchaseOrderCriteria;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseOrderResourceDTOMapper {

    public static PurchaseOrder convertToEntity(PurchaseOrderResource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("PurchaseOrderResource cannot be null");
        }

        List<PurchaseOrderLine> purchaseOrderLines = resource.getPurchaseOrderLines() != null
                ? resource.getPurchaseOrderLines().stream().map(line -> PurchaseOrderLine.of(
                line.getPurchaseOrderNumber(),
                line.getPurchaseOrderLineNumber(),
                line.getPurchaseOrderLineDisplayNumber(),
                line.getSalesOrderNumber(),
                line.getSalesOrderLineNumber(),
                line.getProductCode(),
                line.getProductName(),
                line.getPurchaseUnitPrice(),
                line.getPurchaseOrderQuantity(),
                line.getReceivedQuantity(),
                line.getCompletionFlag().getValue()
        )).toList()
                : List.of();

        // 発注番号がnullまたは空の場合はbuilderを使用
        if (resource.getPurchaseOrderNumber() == null || resource.getPurchaseOrderNumber().isEmpty()) {
            return PurchaseOrder.builder()
                    .purchaseOrderNumber(null)
                    .purchaseOrderDate(PurchaseOrderDate.of(resource.getPurchaseOrderDate()))
                    .salesOrderNumber(OrderNumber.of(resource.getSalesOrderNumber()))
                    .supplierCode(SupplierCode.of(resource.getSupplierCode(), resource.getSupplierBranchNumber()))
                    .purchaseManagerCode(EmployeeCode.of(resource.getPurchaseManagerCode()))
                    .designatedDeliveryDate(DesignatedDeliveryDate.of(resource.getDesignatedDeliveryDate()))
                    .warehouseCode(WarehouseCode.of(resource.getWarehouseCode()))
                    .totalPurchaseAmount(Money.of(resource.getTotalPurchaseAmount()))
                    .totalConsumptionTax(Money.of(resource.getTotalConsumptionTax()))
                    .remarks(resource.getRemarks())
                    .purchaseOrderLines(purchaseOrderLines)
                    .build();
        }
        
        return PurchaseOrder.of(
                resource.getPurchaseOrderNumber(),
                resource.getPurchaseOrderDate(),
                resource.getSalesOrderNumber(),
                resource.getSupplierCode(),
                resource.getSupplierBranchNumber(),
                resource.getPurchaseManagerCode(),
                resource.getDesignatedDeliveryDate(),
                resource.getWarehouseCode(),
                resource.getTotalPurchaseAmount(),
                resource.getTotalConsumptionTax(),
                resource.getRemarks(),
                purchaseOrderLines
        );
    }

    public static PurchaseOrderCriteria convertToCriteria(PurchaseOrderCriteriaResource resource) {
        LocalDateTime purchaseOrderDate = resource.getPurchaseOrderDate() != null ? resource.getPurchaseOrderDate() : null;
        LocalDateTime designatedDeliveryDate = resource.getDesignatedDeliveryDate() != null ? resource.getDesignatedDeliveryDate() : null;

        return PurchaseOrderCriteria.builder()
                .purchaseOrderNumber(resource.getPurchaseOrderNumber())
                .purchaseOrderDate(purchaseOrderDate)
                .salesOrderNumber(resource.getSalesOrderNumber())
                .supplierCode(resource.getSupplierCode())
                .supplierBranchNumber(resource.getSupplierBranchNumber())
                .purchaseManagerCode(resource.getPurchaseManagerCode())
                .designatedDeliveryDate(designatedDeliveryDate)
                .warehouseCode(resource.getWarehouseCode())
                .remarks(resource.getRemarks())
                .build();
    }
}