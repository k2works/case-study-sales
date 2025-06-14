package com.example.sms.presentation.api.sales.sales;

import com.example.sms.domain.model.sales.order.TaxRateType;
import com.example.sms.domain.model.sales.sales.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "売上明細情報")
public class SalesLineResource {

    @Schema(description = "売上番号")
    private String salesNumber;

    @Schema(description = "明細番号")
    private Integer salesLineNumber;

    @Schema(description = "受注番号")
    private String orderNumber;

    @Schema(description = "受注明細番号")
    private Integer orderLineNumber;

    @Schema(description = "製品コード")
    private String productCode;

    @Schema(description = "製品名")
    private String productName;

    @Schema(description = "売上単価")
    private Integer salesUnitPrice;

    @Schema(description = "売上数量")
    private Integer salesQuantity;

    @Schema(description = "出荷数量")
    private Integer shippedQuantity;

    @Schema(description = "割引額")
    private Integer discountAmount;

    @Schema(description = "請求日")
    private LocalDateTime billingDate;

    @Schema(description = "請求番号")
    private String billingNumber;

    @Schema(description = "請求遅延区分")
    private Integer billingDelayCategory;

    @Schema(description = "自動仕訳日")
    private LocalDateTime autoJournalDate;

    @Schema(description = "消費税率")
    private TaxRateType taxRate;

    /**
     * SalesLine エンティティを SalesLineResource に変換するメソッド
     */
    public static SalesLineResource from(SalesLine salesLine) {

        return SalesLineResource.builder()
                .salesNumber(salesLine.getSalesNumber().getValue())
                .salesLineNumber(salesLine.getSalesLineNumber())
                .orderNumber(salesLine.getOrderNumber().getValue())
                .orderLineNumber(salesLine.getOrderLineNumber())
                .productCode(salesLine.getProductCode().getValue())
                .productName(salesLine.getProductName())
                .salesUnitPrice(salesLine.getSalesUnitPrice().getAmount())
                .salesQuantity(salesLine.getSalesQuantity().getAmount())
                .shippedQuantity(salesLine.getShippedQuantity().getAmount())
                .discountAmount(salesLine.getDiscountAmount().getAmount())
                .billingDate(Optional.of(salesLine)
                        .map(SalesLine::getBillingDate)
                        .map(BillingDate::getValue)
                        .orElse(null))
                .billingNumber(Optional.of(salesLine)
                        .map(SalesLine::getBillingNumber)
                        .map(BillingNumber::getValue)
                        .orElse(null))
                .billingDelayCategory(Optional.of(salesLine)
                        .map(SalesLine::getBillingDelayType)
                        .map(BillingDelayType::getCode)
                        .orElse(null))
                .autoJournalDate(Optional.of(salesLine)
                        .map(SalesLine::getAutoJournalDate)
                        .map(AutoJournalDate::getValue)
                        .orElse(null))
                .taxRate(salesLine.getTaxRate())
                .build();
    }

    /**
     * SalesLine のリストを SalesLineResource のリストに変換するメソッド
     */
    public static List<SalesLineResource> from(List<SalesLine> salesLines) {
        return salesLines.stream().map(SalesLineResource::from).toList();
    }
}