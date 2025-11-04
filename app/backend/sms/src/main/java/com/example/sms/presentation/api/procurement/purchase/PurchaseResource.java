package com.example.sms.presentation.api.procurement.purchase;

import com.example.sms.domain.model.procurement.purchase.Purchase;
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

    @Schema(description = "仕入先コード")
    String supplierCode;

    @Schema(description = "仕入先枝番")
    Integer supplierBranchNumber;

    @Schema(description = "仕入担当者コード")
    String purchaseManagerCode;

    @Schema(description = "開始日")
    LocalDateTime startDate;

    @Schema(description = "発注番号")
    String purchaseOrderNumber;

    @Schema(description = "部門コード")
    String departmentCode;

    @Schema(description = "仕入金額合計")
    Integer totalPurchaseAmount;

    @Schema(description = "消費税合計")
    Integer totalConsumptionTax;

    @Schema(description = "備考")
    String remarks;

    @Schema(description = "仕入明細")
    List<PurchaseLineResource> purchaseLines;

    public static PurchaseResource from(Purchase purchase) {
        PurchaseResource resource = new PurchaseResource();
        resource.setPurchaseNumber(purchase.getPurchaseNumber() != null ?
            purchase.getPurchaseNumber().getValue() : null);
        resource.setPurchaseDate(purchase.getPurchaseDate().getValue());
        resource.setSupplierCode(purchase.getSupplierCode().getCode().getValue());
        resource.setSupplierBranchNumber(purchase.getSupplierCode().getBranchNumber());
        resource.setPurchaseManagerCode(purchase.getPurchaseManagerCode().getValue());
        resource.setStartDate(purchase.getStartDate());
        resource.setPurchaseOrderNumber(purchase.getPurchaseOrderNumber() != null ?
            purchase.getPurchaseOrderNumber().getValue() : null);
        resource.setDepartmentCode(purchase.getDepartmentCode().getValue());
        resource.setTotalPurchaseAmount(purchase.getTotalPurchaseAmount().getAmount());
        resource.setTotalConsumptionTax(purchase.getTotalConsumptionTax().getAmount());
        resource.setRemarks(purchase.getRemarks());
        resource.setPurchaseLines(PurchaseLineResource.from(purchase.getPurchaseLines()));
        return resource;
    }
}
