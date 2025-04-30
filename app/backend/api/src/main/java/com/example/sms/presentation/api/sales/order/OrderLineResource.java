package com.example.sms.presentation.api.sales.order;

import com.example.sms.domain.model.sales.order.CompletionFlag;
import com.example.sms.domain.model.sales.order.OrderLine;
import com.example.sms.domain.model.sales.order.TaxRateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "受注明細情報")
public class OrderLineResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "受注番号")
    String orderNumber;

    @Schema(description = "受注行番号")
    Integer orderLineNumber;

    @Schema(description = "商品コード")
    String productCode;

    @Schema(description = "商品名")
    String productName;

    @Schema(description = "販売単価")
    Integer salesUnitPrice;

    @Schema(description = "受注数量")
    Integer orderQuantity;

    @Schema(description = "消費税率")
    TaxRateType taxRate;

    @Schema(description = "引当数量")
    Integer allocationQuantity;

    @Schema(description = "出荷指示数量")
    Integer shipmentInstructionQuantity;

    @Schema(description = "出荷済数量")
    Integer shippedQuantity;

    @Schema(description = "完了フラグ")
    CompletionFlag completionFlag;

    @Schema(description = "値引金額")
    Integer discountAmount;

    @Schema(description = "納期")
    LocalDateTime deliveryDate;

    public static List<OrderLineResource> from(List<OrderLine> orderLines) {
        return orderLines.stream()
                .map(salesOrderLine -> OrderLineResource.builder()
                        .orderNumber(salesOrderLine.getOrderNumber().getValue())
                        .orderLineNumber(salesOrderLine.getOrderLineNumber())
                        .productCode(salesOrderLine.getProductCode().getValue())
                        .productName(salesOrderLine.getProductName())
                        .salesUnitPrice(salesOrderLine.getSalesUnitPrice().getAmount())
                        .orderQuantity(salesOrderLine.getOrderQuantity().getAmount())
                        .taxRate(salesOrderLine.getTaxRate())
                        .allocationQuantity(salesOrderLine.getAllocationQuantity().getAmount())
                        .shipmentInstructionQuantity(salesOrderLine.getShipmentInstructionQuantity().getAmount())
                        .shippedQuantity(salesOrderLine.getShippedQuantity().getAmount())
                        .completionFlag(salesOrderLine.getCompletionFlag())
                        .discountAmount(salesOrderLine.getDiscountAmount().getAmount())
                        .deliveryDate(salesOrderLine.getDeliveryDate().getValue())
                        .build())
                .toList();
    }
}