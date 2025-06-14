package com.example.sms.domain.model.master.partner.vendor;

import com.example.sms.domain.model.master.partner.billing.ClosingBilling;
import com.example.sms.domain.type.mail.EmailAddress;
import com.example.sms.domain.type.phone.FaxNumber;
import com.example.sms.domain.type.phone.PhoneNumber;
import com.example.sms.domain.type.address.Address;
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
    PhoneNumber vendorPhoneNumber; // 仕入先電話番号
    FaxNumber vendorFaxNumber; // 仕入先ｆａｘ番号
    EmailAddress vendorEmailAddress; // 仕入先メールアドレス
    ClosingBilling vendorClosingBilling; // 仕入先締請求

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
                PhoneNumber.of(vendorPhoneNumber),
                FaxNumber.of(vendorFaxNumber),
                EmailAddress.of(vendorEmailAddress),
                ClosingBilling.of(vendorClosingDate, vendorPaymentMonth, vendorPaymentDate, vendorPaymentMethod)
        );
    }

    public static Vendor of(
            VendorCode vendorCode,
            VendorName vendorName,
            String vendorContactName,
            String vendorDepartmentName,
            Address vendorAddress,
            PhoneNumber vendorPhoneNumber,
            FaxNumber vendorFaxNumber,
            EmailAddress vendorEmailAddress,
            ClosingBilling vendorClosingBilling
    ) {
        return new Vendor(
                vendorCode,
                vendorName,
                vendorContactName,
                vendorDepartmentName,
                vendorAddress,
                vendorPhoneNumber,
                vendorFaxNumber,
                vendorEmailAddress,
                vendorClosingBilling
        );
    }
}
