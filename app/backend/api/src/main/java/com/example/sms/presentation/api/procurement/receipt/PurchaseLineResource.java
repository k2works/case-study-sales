package com.example.sms.presentation.api.procurement.receipt;

import com.example.sms.domain.model.sales.order.CompletionFlag;
import com.example.sms.domain.model.procurement.order.PurchaseOrderLine;
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

    @Schema(description = "仕入明細番号")
    Integer purchaseLineNumber;

    @Schema(description = "仕入明細表示番号")
    Integer purchaseLineDisplayNumber;

    @Schema(description = "受注番号")
    String salesOrderNumber;

    @Schema(description = "受注明細番号")
    Integer salesOrderLineNumber;

    @Schema(description = "商品コード")
    String productCode;

    @Schema(description = "商品名")
    String productName;

    @Schema(description = "仕入単価")
    Integer purchaseUnitPrice;

    @Schema(description = "仕入数量")
    Integer purchaseQuantity;

    @Schema(description = "入荷数量")
    Integer receivedQuantity;

    @Schema(description = "完了フラグ")
    CompletionFlag completionFlag;

    public static PurchaseLineResource from(PurchaseOrderLine line) {
        PurchaseLineResource resource = new PurchaseLineResource();
        resource.setPurchaseNumber(line.getPurchaseOrderNumber().getValue());
        resource.setPurchaseLineNumber(line.getPurchaseOrderLineNumber());
        resource.setPurchaseLineDisplayNumber(line.getPurchaseOrderLineDisplayNumber());
        resource.setSalesOrderNumber(line.getSalesOrderNumber().getValue());
        resource.setSalesOrderLineNumber(line.getSalesOrderLineNumber());
        resource.setProductCode(line.getProductCode().getValue());
        resource.setProductName(line.getProductName());
        resource.setPurchaseUnitPrice(line.getPurchaseUnitPrice().getAmount());
        resource.setPurchaseQuantity(line.getPurchaseOrderQuantity().getAmount());
        resource.setReceivedQuantity(line.getReceivedQuantity().getAmount());
        resource.setCompletionFlag(line.getCompletionFlag());
        return resource;
    }

    public static List<PurchaseLineResource> from(List<PurchaseOrderLine> lines) {
        return lines.stream()
                .map(PurchaseLineResource::from)
                .collect(Collectors.toList());
    }
}
