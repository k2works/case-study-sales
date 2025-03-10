package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "取引先分類種別")
public class PartnerCategoryTypeResource {
    @NotNull
    @Schema(description = "取引先分類種別コード")
    private String partnerCategoryTypeCode;

    @Schema(description = "取引先分類種別名")
    private String partnerCategoryTypeName;

    @Schema(description = "取引先分類項目リスト")
    private List<PartnerCategoryItemResource> partnerCategoryItems;

    @Getter
    @Setter
    @Schema(description = "取引先分類項目")
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
