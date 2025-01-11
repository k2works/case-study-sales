package com.example.sms.domain.model.master.partner;

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
    Integer supplierType;        // 仕入先区分
    Address address;             // 住所
    Integer tradeProhibitedFlag; // 取引禁止フラグ
    Integer miscellaneousType;   // 雑区分
    String partnerGroupCode;     // 取引先グループコード
    Integer creditLimit;         // 与信限度額
    Integer temporaryCreditIncrease; // 与信一時増加枠
    List<Customer> customers;   // 顧客
    List<Vendor> vendors;       // 仕入先

    public static Partner of(
            String partnerCode,
            String partnerName,
            String partnerNameKana,
            Integer supplierType,
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
                supplierType,
                Address.of(postalCode, prefecture, address1, address2),
                tradeProhibitedFlag,
                miscellaneousType,
                partnerGroupCode,
                creditLimit,
                temporaryCreditIncrease,
                List.of(),
                List.of()
        );
    }

    public static Partner ofWithCustomers(Partner partner, List<Customer> customers) {
        return new Partner(
                partner.partnerCode,
                partner.partnerName,
                partner.supplierType,
                partner.address,
                partner.tradeProhibitedFlag,
                partner.miscellaneousType,
                partner.partnerGroupCode,
                partner.creditLimit,
                partner.temporaryCreditIncrease,
                customers,
                Collections.emptyList()
        );
    }

    public static Partner ofWithVendors(Partner partner, List<Vendor> vendors) {
        return new Partner(
                partner.partnerCode,
                partner.partnerName,
                partner.supplierType,
                partner.address,
                partner.tradeProhibitedFlag,
                partner.miscellaneousType,
                partner.partnerGroupCode,
                partner.creditLimit,
                partner.temporaryCreditIncrease,
                Collections.emptyList(),
                vendors
        );
    }
}
