package com.example.sms.presentation.api.procurement.receipt;

import com.example.sms.domain.model.procurement.receipt.PurchaseLine;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Schema(description = "仕入明細情報")
public class PurchaseLineResource {
    @Schema(description = "仕入番号")
    String purchaseNumber;

    @Schema(description = "仕入行番号")
    Integer purchaseLineNumber;

    @Schema(description = "仕入行表示番号")
    Integer purchaseLineDisplayNumber;

    @Schema(description = "発注行番号")
    Integer purchaseOrderLineNumber;

    @Schema(description = "商品コード")
    String productCode;

    @Schema(description = "倉庫コード")
    String warehouseCode;

    @Schema(description = "商品名")
    String productName;

    @Schema(description = "仕入単価")
    Integer purchaseUnitPrice;

    @Schema(description = "仕入数量")
    Integer purchaseQuantity;

    public static PurchaseLineResource from(PurchaseLine line) {
        PurchaseLineResource resource = new PurchaseLineResource();
        resource.setPurchaseNumber(line.getPurchaseNumber().getValue());
        resource.setPurchaseLineNumber(line.getPurchaseLineNumber());
        resource.setPurchaseLineDisplayNumber(line.getPurchaseLineDisplayNumber());
        resource.setPurchaseOrderLineNumber(line.getPurchaseOrderLineNumber());
        resource.setProductCode(line.getProductCode().getValue());
        resource.setWarehouseCode(line.getWarehouseCode().getValue());
        resource.setProductName(line.getProductName());
        resource.setPurchaseUnitPrice(line.getPurchaseUnitPrice().getAmount());
        resource.setPurchaseQuantity(line.getPurchaseQuantity().getAmount());
        return resource;
    }

    public static List<PurchaseLineResource> from(List<PurchaseLine> lines) {
        return lines.stream()
                .map(PurchaseLineResource::from)
                .collect(Collectors.toList());
    }
}
