package com.example.sms.presentation.api.procurement.receipt;

import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.supplier.SupplierCode;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.model.procurement.purchase.*;
import com.example.sms.domain.model.sales.order.OrderNumber;
import com.example.sms.domain.type.money.Money;
import com.example.sms.service.procurement.purchase.PurchaseOrderCriteria;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseResourceDTOMapper {

    public static PurchaseOrder convertToEntity(PurchaseResource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("PurchaseResource cannot be null");
        }

        List<PurchaseOrderLine> purchaseOrderLines = resource.getPurchaseLines() != null
                ? resource.getPurchaseLines().stream().map(line -> PurchaseOrderLine.of(
                line.getPurchaseNumber(),
                line.getPurchaseLineNumber(),
                line.getPurchaseLineDisplayNumber(),
                line.getSalesOrderNumber(),
                line.getSalesOrderLineNumber(),
                line.getProductCode(),
                line.getProductName(),
                line.getPurchaseUnitPrice(),
                line.getPurchaseQuantity(),
                line.getReceivedQuantity(),
                line.getCompletionFlag().getValue()
        )).toList()
                : List.of();

        // 仕入番号がnullまたは空の場合はbuilderを使用
        if (resource.getPurchaseNumber() == null || resource.getPurchaseNumber().isEmpty()) {
            return PurchaseOrder.builder()
                    .purchaseOrderNumber(null)
                    .purchaseOrderDate(PurchaseOrderDate.of(resource.getPurchaseDate()))
                    .salesOrderNumber(OrderNumber.of(resource.getSalesOrderNumber()))
                    .supplierCode(SupplierCode.of(resource.getSupplierCode(), resource.getSupplierBranchNumber()))
                    .purchaseManagerCode(EmployeeCode.of(resource.getPurchaseManagerCode()))
                    .designatedDeliveryDate(DesignatedDeliveryDate.of(resource.getDeliveryDate()))
                    .warehouseCode(WarehouseCode.of(resource.getWarehouseCode()))
                    .totalPurchaseAmount(Money.of(resource.getTotalPurchaseAmount()))
                    .totalConsumptionTax(Money.of(resource.getTotalConsumptionTax()))
                    .remarks(resource.getRemarks())
                    .purchaseOrderLines(purchaseOrderLines)
                    .build();
        }

        return PurchaseOrder.of(
                resource.getPurchaseNumber(),
                resource.getPurchaseDate(),
                resource.getSalesOrderNumber(),
                resource.getSupplierCode(),
                resource.getSupplierBranchNumber(),
                resource.getPurchaseManagerCode(),
                resource.getDeliveryDate(),
                resource.getWarehouseCode(),
                resource.getTotalPurchaseAmount(),
                resource.getTotalConsumptionTax(),
                resource.getRemarks(),
                purchaseOrderLines
        );
    }

    public static PurchaseOrderCriteria convertToCriteria(PurchaseCriteriaResource resource) {
        LocalDateTime purchaseDate = resource.getPurchaseDate() != null ? resource.getPurchaseDate() : null;
        LocalDateTime deliveryDate = resource.getDeliveryDate() != null ? resource.getDeliveryDate() : null;

        return PurchaseOrderCriteria.builder()
                .purchaseOrderNumber(resource.getPurchaseNumber())
                .purchaseOrderDate(purchaseDate)
                .salesOrderNumber(resource.getSalesOrderNumber())
                .supplierCode(resource.getSupplierCode())
                .supplierBranchNumber(resource.getSupplierBranchNumber())
                .purchaseManagerCode(resource.getPurchaseManagerCode())
                .designatedDeliveryDate(deliveryDate)
                .warehouseCode(resource.getWarehouseCode())
                .remarks(resource.getRemarks())
                .build();
    }
}
