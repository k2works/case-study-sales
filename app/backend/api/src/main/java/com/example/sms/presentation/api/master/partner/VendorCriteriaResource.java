package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "仕入先")
public class VendorCriteriaResource {
    String vendorCode; // 仕入先コード
    String vendorName; // 仕入先名
    String vendorContactName; // 仕入先担当者名
    String vendorDepartmentName; // 仕入先部門名
    String postalCode;
    String prefecture;
    String address1;
    String address2;
    String vendorPhoneNumber; // 仕入先電話番号
    String vendorFaxNumber; // 仕入先ｆａｘ番号
    String vendorEmailAddress; // 仕入先メールアドレス
}
