package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.PartnerCategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Schema(description = "取引先分類種別")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerCategoryTypeResource {
    @NotNull
    @Schema(description = "取引先分類種別コード")
    private String partnerCategoryTypeCode;

    @Schema(description = "取引先分類種別名")
    private String partnerCategoryTypeName;

    @Schema(description = "取引先分類項目リスト")
    private List<PartnerCategoryItemResource> partnerCategoryItems;

    public static PartnerCategoryTypeResource from(PartnerCategoryType partnerCategoryType) {
        return PartnerCategoryTypeResource.builder()
                .partnerCategoryTypeCode(partnerCategoryType.getPartnerCategoryTypeCode())
                .partnerCategoryTypeName(partnerCategoryType.getPartnerCategoryTypeName())
                .partnerCategoryItems(partnerCategoryType.getPartnerCategoryItems().stream()
                        .map(partnerCategoryItem -> PartnerCategoryItemResource.builder()
                                .partnerCategoryItemCode(partnerCategoryItem.getPartnerCategoryItemCode())
                                .partnerCategoryItemName(partnerCategoryItem.getPartnerCategoryItemName())
                                .partnerCategoryAffiliations(partnerCategoryItem.getPartnerCategoryAffiliations().stream()
                                        .map(partnerCategoryAffiliation -> {
                                            PartnerCategoryItemResource.PartnerCategoryAffiliationResource partnerCategoryAffiliationResource = new PartnerCategoryItemResource.PartnerCategoryAffiliationResource();
                                            partnerCategoryAffiliationResource.setPartnerCategoryTypeCode(partnerCategoryAffiliation.getPartnerCategoryTypeCode());
                                            partnerCategoryAffiliationResource.setPartnerCode(partnerCategoryAffiliation.getPartnerCode().getValue());
                                            partnerCategoryAffiliationResource.setPartnerCategoryItemCode(partnerCategoryAffiliation.getPartnerCategoryItemCode());
                                            return partnerCategoryAffiliationResource;
                                        })
                                        .toList())
                                .build())
                        .toList())
                .build();
    }

    @Getter
    @Setter
    @Schema(description = "取引先分類項目")
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PartnerCategoryItemResource {
        @Schema(description = "取引先分類コード")
        private String partnerCategoryItemCode;

        @Schema(description = "取引先分類名")
        private String partnerCategoryItemName;

        @Schema(description = "取引先分類所属リスト")
        private List<PartnerCategoryAffiliationResource> partnerCategoryAffiliations;

        @Getter
        @Setter
        @Schema(description = "取引先分類所属")
        public static class PartnerCategoryAffiliationResource {
            @Schema(description = "取引先分類種別コード")
            private String partnerCategoryTypeCode;

            @Schema(description = "取引先コード")
            private String partnerCode;

            @Schema(description = "取引先分類コード")
            private String partnerCategoryItemCode;
        }
    }
}
