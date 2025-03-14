package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.SubstituteProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Schema(description = "代替商品")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubstituteProductResource {
    @Schema(description = "商品コード")
    String productCode;
    @Schema(description = "代替商品コード")
    String substituteProductCode;
    @Schema(description = "優先順位")
    Integer priority;

    public static SubstituteProductResource from(SubstituteProduct substituteProduct) {
        return SubstituteProductResource.builder()
                .productCode(substituteProduct.getProductCode().getValue())
                .substituteProductCode(substituteProduct.getSubstituteProductCode().getValue())
                .priority(substituteProduct.getPriority())
                .build();
    }

    public static List<SubstituteProductResource> from(List<SubstituteProduct> substituteProduct) {
        return substituteProduct.stream().map(SubstituteProductResource::from).toList();
    }
}
