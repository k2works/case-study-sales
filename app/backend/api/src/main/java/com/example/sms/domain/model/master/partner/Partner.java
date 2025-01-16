package com.example.sms.domain.model.master.partner;

import com.example.sms.domain.model.common.address.Address;
import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.domain.type.partner.MiscellaneousType;
import com.example.sms.domain.type.partner.TradeProhibitedFlag;
import com.example.sms.domain.type.partner.VendorType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Collections;
import java.util.List;

/**
 * 取引先
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Partner {
    PartnerCode partnerCode;          // 取引先コード
    PartnerName partnerName;          // 取引先名
    VendorType vendorType;        // 仕入先区分
    Address address;             // 住所
    TradeProhibitedFlag tradeProhibitedFlag; // 取引禁止フラグ
    MiscellaneousType miscellaneousType;   // 雑区分
    PartnerGroupCode partnerGroupCode;     // 取引先グループコード
    Credit credit;               // 与信
    List<Customer> customers;   // 顧客
    List<Vendor> vendors;       // 仕入先

    public static Partner of(
            String partnerCode,
            String partnerName,
            String partnerNameKana,
            Integer vendorType,
            String postalCode,
            String prefecture,
            String address1,
            String address2,
            Integer tradeProhibitedFlag,
            Integer miscellaneousType,
            String partnerGroupCode,
            Integer creditLimit,
            Integer temporaryCreditIncrease
    ) {
        return new Partner(
                PartnerCode.of(partnerCode),
                PartnerName.of(partnerName, partnerNameKana),
                VendorType.fromCode(vendorType),
                Address.of(postalCode, prefecture, address1, address2),
                TradeProhibitedFlag.fromCode(tradeProhibitedFlag),
                MiscellaneousType.fromCode(miscellaneousType),
                PartnerGroupCode.of(partnerGroupCode),
                Credit.of(creditLimit, temporaryCreditIncrease),
                List.of(),
                List.of()
        );
    }

    public static Partner of(
            PartnerCode partnerCode,
            PartnerName partnerName,
            VendorType vendorType,
            Address address,
            TradeProhibitedFlag tradeProhibitedFlag,
            MiscellaneousType miscellaneousType,
            PartnerGroupCode partnerGroupCode,
            Credit credit,
            List<Customer> customers,
            List<Vendor> vendors
    ) {
        return new Partner(
                partnerCode,
                partnerName,
                vendorType,
                address,
                tradeProhibitedFlag,
                miscellaneousType,
                partnerGroupCode,
                credit,
                customers,
                vendors
        );
    }

    public static Partner ofWithCustomers(Partner partner, List<Customer> customers) {
        return new Partner(
                partner.partnerCode,
                partner.partnerName,
                partner.vendorType,
                partner.address,
                partner.tradeProhibitedFlag,
                partner.miscellaneousType,
                partner.partnerGroupCode,
                partner.credit,
                customers,
                Collections.emptyList()
        );
    }

    public static Partner ofWithVendors(Partner partner, List<Vendor> vendors) {
        return new Partner(
                partner.partnerCode,
                partner.partnerName,
                partner.vendorType,
                partner.address,
                partner.tradeProhibitedFlag,
                partner.miscellaneousType,
                partner.partnerGroupCode,
                partner.credit,
                Collections.emptyList(),
                vendors
        );
    }
}
