package com.example.sms.presentation.api.procurement.receipt;

import com.example.sms.domain.model.procurement.order.PurchaseOrder;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Schema(description = "仕入情報")
public class PurchaseResource {
    @Schema(description = "仕入番号")
    String purchaseNumber;

    @Schema(description = "仕入日")
    LocalDateTime purchaseDate;

    @Schema(description = "受注番号")
    String salesOrderNumber;

    @Schema(description = "仕入先コード")
    String supplierCode;

    @Schema(description = "仕入先枝番")
    Integer supplierBranchNumber;

    @Schema(description = "仕入担当者コード")
    String purchaseManagerCode;

    @Schema(description = "納期")
    LocalDateTime deliveryDate;

    @Schema(description = "倉庫コード")
    String warehouseCode;

    @Schema(description = "仕入金額合計")
    Integer totalPurchaseAmount;

    @Schema(description = "消費税合計")
    Integer totalConsumptionTax;

    @Schema(description = "備考")
    String remarks;

    @Schema(description = "仕入明細")
    List<PurchaseLineResource> purchaseLines;

    public static PurchaseResource from(PurchaseOrder purchaseOrder) {
        PurchaseResource resource = new PurchaseResource();
        resource.setPurchaseNumber(purchaseOrder.getPurchaseOrderNumber() != null ?
            purchaseOrder.getPurchaseOrderNumber().getValue() : null);
        resource.setPurchaseDate(purchaseOrder.getPurchaseOrderDate().getValue());
        resource.setSalesOrderNumber(purchaseOrder.getSalesOrderNumber().getValue());
        resource.setSupplierCode(purchaseOrder.getSupplierCode().getValue());
        resource.setSupplierBranchNumber(purchaseOrder.getSupplierCode().getBranchNumber());
        resource.setPurchaseManagerCode(purchaseOrder.getPurchaseManagerCode().getValue());
        resource.setDeliveryDate(purchaseOrder.getDesignatedDeliveryDate().getValue());
        resource.setWarehouseCode(purchaseOrder.getWarehouseCode().getValue());
        resource.setTotalPurchaseAmount(purchaseOrder.getTotalPurchaseAmount().getAmount());
        resource.setTotalConsumptionTax(purchaseOrder.getTotalConsumptionTax().getAmount());
        resource.setRemarks(purchaseOrder.getRemarks());
        resource.setPurchaseLines(PurchaseLineResource.from(purchaseOrder.getPurchaseOrderLines()));
        return resource;
    }
}
