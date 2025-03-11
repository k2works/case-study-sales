package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.MiscellaneousType;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.TradeProhibitedFlag;
import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.domain.model.master.partner.vendor.VendorType;
import com.example.sms.domain.type.address.Prefecture;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Schema(description = "取引先")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerResource {
    String partnerCode;          // 取引先コード
    String partnerName;          // 取引先名
    String partnerNameKana;      // 取引先名カナ
    VendorType vendorType;        // 仕入先区分
    String postalCode;
    String prefecture;
    String address1;
    String address2;
    TradeProhibitedFlag tradeProhibitedFlag; // 取引禁止フラグ
    MiscellaneousType miscellaneousType;   // 雑区分
    String partnerGroupCode;     // 取引先グループコード
    Integer creditLimit; // 与信限度額
    Integer temporaryCreditIncrease; // 与信一時増加枠
    List<CustomerResource> customers; // 取引先顧客
    List<VendorResource> vendors; // 取引先仕入先

    public static PartnerResource from(Partner partner) {
        return PartnerResource.builder()
                .partnerCode(partner.getPartnerCode().getValue())
                .partnerName(partner.getPartnerName().getName())
                .partnerNameKana(partner.getPartnerName().getNameKana())
                .vendorType(partner.getVendorType())
                .postalCode(partner.getAddress().getPostalCode().getValue())
                .prefecture(partner.getAddress().getPrefecture().name())
                .address1(partner.getAddress().getAddress1())
                .address2(partner.getAddress().getAddress2())
                .tradeProhibitedFlag(partner.getTradeProhibitedFlag())
                .miscellaneousType(partner.getMiscellaneousType())
                .partnerGroupCode(partner.getPartnerGroupCode().getValue())
                .creditLimit(partner.getCredit().getCreditLimit().getAmount())
                .temporaryCreditIncrease(partner.getCredit().getTemporaryCreditIncrease().getAmount())
                .customers(CustomerResource.from(partner.getCustomers()))
                .vendors(VendorResource.from(partner.getVendors()))
                .build();
    }
}
