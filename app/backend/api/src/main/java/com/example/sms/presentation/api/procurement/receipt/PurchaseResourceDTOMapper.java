package com.example.sms.presentation.api.procurement.receipt;

import com.example.sms.domain.model.master.department.DepartmentCode;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.supplier.SupplierCode;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.model.procurement.order.PurchaseOrderNumber;
import com.example.sms.domain.model.procurement.receipt.*;
import com.example.sms.domain.type.money.Money;
import com.example.sms.service.procurement.receipt.PurchaseCriteria;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseResourceDTOMapper {

    public static Purchase convertToEntity(PurchaseResource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("PurchaseResource cannot be null");
        }

        List<PurchaseLine> purchaseLines = resource.getPurchaseLines() != null
                ? resource.getPurchaseLines().stream().map(line -> PurchaseLine.of(
                line.getPurchaseNumber(),
                line.getPurchaseLineNumber(),
                line.getPurchaseLineDisplayNumber(),
                line.getPurchaseOrderLineNumber(),
                line.getProductCode(),
                line.getWarehouseCode(),
                line.getProductName(),
                line.getPurchaseUnitPrice(),
                line.getPurchaseQuantity()
        )).toList()
                : List.of();

        // 仕入番号がnullまたは空の場合はbuilderを使用
        if (resource.getPurchaseNumber() == null || resource.getPurchaseNumber().isEmpty()) {
            return Purchase.builder()
                    .purchaseNumber(null)
                    .purchaseDate(PurchaseDate.of(resource.getPurchaseDate()))
                    .supplierCode(SupplierCode.of(resource.getSupplierCode(), resource.getSupplierBranchNumber()))
                    .purchaseManagerCode(EmployeeCode.of(resource.getPurchaseManagerCode()))
                    .startDate(resource.getStartDate())
                    .purchaseOrderNumber(resource.getPurchaseOrderNumber() != null ?
                        PurchaseOrderNumber.of(resource.getPurchaseOrderNumber()) : null)
                    .departmentCode(DepartmentCode.of(resource.getDepartmentCode()))
                    .totalPurchaseAmount(Money.of(resource.getTotalPurchaseAmount()))
                    .totalConsumptionTax(Money.of(resource.getTotalConsumptionTax()))
                    .remarks(resource.getRemarks())
                    .purchaseLines(purchaseLines)
                    .build();
        }

        return Purchase.of(
                resource.getPurchaseNumber(),
                resource.getPurchaseDate(),
                resource.getSupplierCode(),
                resource.getSupplierBranchNumber(),
                resource.getPurchaseManagerCode(),
                resource.getStartDate(),
                resource.getPurchaseOrderNumber(),
                resource.getDepartmentCode(),
                resource.getTotalPurchaseAmount(),
                resource.getTotalConsumptionTax(),
                resource.getRemarks(),
                purchaseLines
        );
    }

    public static PurchaseCriteria convertToCriteria(PurchaseCriteriaResource resource) {
        LocalDateTime purchaseDate = resource.getPurchaseDate() != null ? resource.getPurchaseDate() : null;

        return PurchaseCriteria.builder()
                .purchaseNumber(resource.getPurchaseNumber())
                .purchaseDate(purchaseDate)
                .purchaseOrderNumber(resource.getPurchaseOrderNumber())
                .supplierCode(resource.getSupplierCode())
                .supplierBranchNumber(resource.getSupplierBranchNumber())
                .purchaseManagerCode(resource.getPurchaseManagerCode())
                .departmentCode(resource.getDepartmentCode())
                .build();
    }
}
