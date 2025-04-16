package com.example.sms.presentation.api.sales;

import com.example.sms.domain.model.sales.SalesLine;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * SalesLine エンティティを SalesLineResource に変換するメソッド
     */
    public static SalesLineResource from(SalesLine salesLine) {

        return SalesLineResource.builder()
                .salesNumber(salesLine.getSalesNumber().getValue())
                .salesLineNumber(salesLine.getSalesLineNumber())
                .productCode(salesLine.getProductCode().getValue())
                .productName(salesLine.getProductName())
                .salesUnitPrice(salesLine.getSalesUnitPrice().getAmount())
                .salesQuantity(salesLine.getSalesQuantity().getAmount())
                .shippedQuantity(salesLine.getShippedQuantity().getAmount())
                .discountAmount(salesLine.getDiscountAmount().getAmount())
                .billingDate(salesLine.getBillingDate())
                .billingNumber(salesLine.getBillingNumber())
                .billingDelayCategory(salesLine.getBillingDelayCategory())
                .autoJournalDate(salesLine.getAutoJournalDate())
                .build();
    }

    /**
     * SalesLine のリストを SalesLineResource のリストに変換するメソッド
     */
    public static List<SalesLineResource> from(List<SalesLine> salesLines) {
        return salesLines.stream().map(SalesLineResource::from).toList();
    }
}