package com.example.sms.domain.model.master.partner;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 仕入先
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Vendor {
    VendorCode vendorCode; // 仕入先コード
    VendorName vendorName; // 仕入先名
    String vendorContactName; // 仕入先担当者名
    String vendorDepartmentName; // 仕入先部門名
    Address vendorAddress; // 仕入先住所
    String vendorPhoneNumber; // 仕入先電話番号
    String vendorFaxNumber; // 仕入先ｆａｘ番号
    String vendorEmailAddress; // 仕入先メールアドレス
    Integer vendorClosingDate; // 仕入先締日
    Integer vendorPaymentMonth; // 仕入先支払月
    Integer vendorPaymentDate; // 仕入先支払日
    Integer vendorPaymentMethod; // 仕入先支払方法

    public static Vendor of(
            String vendorCode,
            Integer vendorBranchCode,
            String vendorName,
            String vendorNameKana,
            String vendorContactName,
            String vendorDepartmentName,
            String vendorPostalCode,
            String vendorPrefecture,
            String vendorAddress1,
            String vendorAddress2,
            String vendorPhoneNumber,
            String vendorFaxNumber,
            String vendorEmailAddress,
            Integer vendorClosingDate,
            Integer vendorPaymentMonth,
            Integer vendorPaymentDate,
            Integer vendorPaymentMethod
    ) {
        return new Vendor(
                VendorCode.of(vendorCode, vendorBranchCode),
                VendorName.of(vendorName, vendorNameKana),
                vendorContactName,
                vendorDepartmentName,
                Address.of(vendorPostalCode, vendorPrefecture, vendorAddress1, vendorAddress2),
                vendorPhoneNumber,
                vendorFaxNumber,
                vendorEmailAddress,
                vendorClosingDate,
                vendorPaymentMonth,
                vendorPaymentDate,
                vendorPaymentMethod
        );
    }
}
