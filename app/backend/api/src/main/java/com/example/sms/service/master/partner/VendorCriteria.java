package com.example.sms.service.master.partner;

import lombok.Builder;
import lombok.Value;

/**
 * 仕入先検索条件
 */
@Value
@Builder
public class VendorCriteria {
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
