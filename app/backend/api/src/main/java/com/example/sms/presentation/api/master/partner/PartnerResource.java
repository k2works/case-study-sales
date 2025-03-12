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
    @Schema(description = "取引先コード")
    String partnerCode;
    @Schema(description = "取引先名")
    String partnerName;
    @Schema(description = "取引先名カナ")
    String partnerNameKana;
    @Schema(description = "仕入先区分")
    VendorType vendorType;
    @Schema(description = "郵便番号")
    String postalCode;
    @Schema(description = "都道府県")
    String prefecture;
    @Schema(description = "住所1")
    String address1;
    @Schema(description = "住所2")
    String address2;
    @Schema(description = "取引禁止フラグ")
    TradeProhibitedFlag tradeProhibitedFlag;
    @Schema(description = "雑区分")
    MiscellaneousType miscellaneousType;
    @Schema(description = "取引先グループコード")
    String partnerGroupCode;
    @Schema(description = "与信限度額")
    Integer creditLimit;
    @Schema(description = "与信一時増加枠")
    Integer temporaryCreditIncrease;
    @Schema(description = "取引先顧客リスト")
    List<CustomerResource> customers;
    @Schema(description = "取引先仕入先リスト")
    List<VendorResource> vendors;

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
