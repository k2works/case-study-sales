package com.example.sms.presentation.api.procurement.purchase.order;

import com.example.sms.domain.model.procurement.purchase.order.PurchaseOrder;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Schema(description = "発注情報")
public class PurchaseOrderResource {
    @Schema(description = "発注番号")
    String purchaseOrderNumber;

    @Schema(description = "発注日")
    LocalDateTime purchaseOrderDate;

    @Schema(description = "売上番号")
    String salesOrderNumber;

    @Schema(description = "仕入先コード")
    String supplierCode;

    @Schema(description = "仕入先枝番")
    Integer supplierBranchNumber;

    @Schema(description = "発注担当者コード")
    String purchaseManagerCode;

    @Schema(description = "指定納期")
    LocalDateTime designatedDeliveryDate;

    @Schema(description = "倉庫コード")
    String warehouseCode;

    @Schema(description = "発注金額合計")
    Integer totalPurchaseAmount;

    @Schema(description = "消費税合計")
    Integer totalConsumptionTax;

    @Schema(description = "備考")
    String remarks;

    @Schema(description = "発注明細")
    List<PurchaseOrderLineResource> purchaseOrderLines;

    public static PurchaseOrderResource from(PurchaseOrder purchaseOrder) {
        PurchaseOrderResource resource = new PurchaseOrderResource();
        resource.setPurchaseOrderNumber(purchaseOrder.getPurchaseOrderNumber() != null ? 
            purchaseOrder.getPurchaseOrderNumber().getValue() : null);
        resource.setPurchaseOrderDate(purchaseOrder.getPurchaseOrderDate().getValue());
        resource.setSalesOrderNumber(purchaseOrder.getSalesOrderNumber());
        resource.setSupplierCode(purchaseOrder.getSupplierCode().getValue());
        resource.setSupplierBranchNumber(purchaseOrder.getSupplierBranchNumber());
        resource.setPurchaseManagerCode(purchaseOrder.getPurchaseManagerCode().getValue());
        resource.setDesignatedDeliveryDate(purchaseOrder.getDesignatedDeliveryDate().getValue());
        resource.setWarehouseCode(purchaseOrder.getWarehouseCode());
        resource.setTotalPurchaseAmount(purchaseOrder.getTotalPurchaseAmount().getAmount());
        resource.setTotalConsumptionTax(purchaseOrder.getTotalConsumptionTax().getAmount());
        resource.setRemarks(purchaseOrder.getRemarks());
        resource.setPurchaseOrderLines(PurchaseOrderLineResource.from(purchaseOrder.getPurchaseOrderLines()));
        return resource;
    }
}