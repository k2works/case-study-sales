package com.example.sms.presentation.api.sales.purchase.order;

import com.example.sms.domain.model.sales.purchase.order.PurchaseOrderLine;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Schema(description = "発注明細情報")
public class PurchaseOrderLineResource {
    @Schema(description = "発注番号")
    String purchaseOrderNumber;

    @Schema(description = "発注明細番号")
    Integer purchaseOrderLineNumber;

    @Schema(description = "発注明細表示番号")
    Integer purchaseOrderLineDisplayNumber;

    @Schema(description = "売上番号")
    String salesOrderNumber;

    @Schema(description = "受注明細番号")
    Integer salesOrderLineNumber;

    @Schema(description = "商品コード")
    String productCode;

    @Schema(description = "商品名")
    String productName;

    @Schema(description = "発注単価")
    Integer purchaseUnitPrice;

    @Schema(description = "発注数量")
    Integer purchaseOrderQuantity;

    @Schema(description = "入荷数量")
    Integer receivedQuantity;

    @Schema(description = "完了フラグ")
    Integer completionFlag;

    public static PurchaseOrderLineResource from(PurchaseOrderLine line) {
        PurchaseOrderLineResource resource = new PurchaseOrderLineResource();
        resource.setPurchaseOrderNumber(line.getPurchaseOrderNumber().getValue());
        resource.setPurchaseOrderLineNumber(line.getPurchaseOrderLineNumber());
        resource.setPurchaseOrderLineDisplayNumber(line.getPurchaseOrderLineDisplayNumber());
        resource.setSalesOrderNumber(line.getSalesOrderNumber());
        resource.setSalesOrderLineNumber(line.getSalesOrderLineNumber());
        resource.setProductCode(line.getProductCode().getValue());
        resource.setProductName(line.getProductName());
        resource.setPurchaseUnitPrice(line.getPurchaseUnitPrice().getAmount());
        resource.setPurchaseOrderQuantity(line.getPurchaseOrderQuantity().getAmount());
        resource.setReceivedQuantity(line.getReceivedQuantity().getAmount());
        resource.setCompletionFlag(line.getCompletionFlag() != null ? line.getCompletionFlag().getValue() : null);
        return resource;
    }

    public static List<PurchaseOrderLineResource> from(List<PurchaseOrderLine> lines) {
        return lines.stream()
                .map(PurchaseOrderLineResource::from)
                .collect(Collectors.toList());
    }
}