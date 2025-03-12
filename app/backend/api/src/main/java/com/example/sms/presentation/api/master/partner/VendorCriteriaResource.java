package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "仕入先")
public class VendorCriteriaResource {
    @Schema(description = "仕入先コード")
    String vendorCode;
    @Schema(description = "仕入先名")
    String vendorName;
    @Schema(description = "仕入先担当者名")
    String vendorContactName;
    @Schema(description = "仕入先部門名")
    String vendorDepartmentName;
    @Schema(description = "郵便番号")
    String postalCode;
    @Schema(description = "都道府県")
    String prefecture;
    @Schema(description = "住所1")
    String address1;
    @Schema(description = "住所2")
    String address2;
    @Schema(description = "仕入先電話番号")
    String vendorPhoneNumber;
    @Schema(description = "仕入先ｆａｘ番号")
    String vendorFaxNumber;
    @Schema(description = "仕入先メールアドレス")
    String vendorEmailAddress;
}
