package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "顧客検索条件")
public class CustomerCriteriaResource {
    @Schema(description = "顧客コード")
    String customerCode;
    @Schema(description = "顧客名")
    String customerName;
    @Schema(description = "顧客名カナ")
    String customerNameKana;
    @Schema(description = "顧客区分")
    String customerType;
    @Schema(description = "請求先コード")
    String billingCode;
    @Schema(description = "回収先コード")
    String collectionCode;
    @Schema(description = "自社担当者コード")
    String companyRepresentativeCode;
    @Schema(description = "顧客担当者名")
    String customerRepresentativeName;
    @Schema(description = "顧客部門名")
    String customerDepartmentName;
    @Schema(description = "郵便番号")
    String postalCode;
    @Schema(description = "都道府県")
    String prefecture;
    @Schema(description = "住所1")
    String address1;
    @Schema(description = "住所2")
    String address2;
    @Schema(description = "顧客電話番号")
    String customerPhoneNumber;
    @Schema(description = "顧客ｆａｘ番号")
    String customerFaxNumber;
    @Schema(description = "顧客メールアドレス")
    String customerEmailAddress;
    @Schema(description = "顧客請求区分")
    String customerBillingCategory;
}
