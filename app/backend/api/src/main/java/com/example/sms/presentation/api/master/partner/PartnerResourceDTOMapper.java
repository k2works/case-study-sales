package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.TradeProhibitedFlag;
import com.example.sms.domain.model.master.partner.customer.*;
import com.example.sms.domain.model.master.partner.invoice.ClosingInvoice;
import com.example.sms.domain.model.master.partner.invoice.Invoice;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.domain.model.master.partner.vendor.VendorCode;
import com.example.sms.domain.model.master.partner.vendor.VendorName;
import com.example.sms.domain.model.master.partner.vendor.VendorType;
import com.example.sms.domain.model.master.partner.MiscellaneousType;
import com.example.sms.domain.type.address.Address;
import com.example.sms.domain.type.mail.EmailAddress;
import com.example.sms.domain.type.phone.FaxNumber;
import com.example.sms.domain.type.phone.PhoneNumber;
import com.example.sms.presentation.ResourceDTOMapperHelper;
import com.example.sms.service.master.partner.PartnerCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PartnerResourceDTOMapper {

    public static Partner convertToEntity(PartnerResource resource) {
        // Partner の基本プロパティを設定
        Partner partner = Partner.of(
                resource.getPartnerCode(),
                resource.getPartnerName(),
                resource.getPartnerNameKana(),
                resource.getVendorType().getValue(),
                resource.getPostalCode(),
                resource.getPrefecture(),
                resource.getAddress1(),
                resource.getAddress2(),
                resource.getTradeProhibitedFlag().getValue(),
                resource.getMiscellaneousType().getCode(),
                resource.getPartnerGroupCode(),
                resource.getCreditLimit(),
                resource.getTemporaryCreditIncrease()
        );

        // Customers リストを変換 (null 対応とマッピング処理)
        List<Customer> customers = Optional.ofNullable(resource.getCustomers())
                .orElse(Collections.emptyList())  // null の場合は空リスト
                .stream()
                .filter(Objects::nonNull)         // null エントリーを除外
                .map(PartnerResourceDTOMapper::convertToCustomer)     // 変換ロジック
                .toList();

        // Vendors リストを変換 (null 対応とマッピング処理)
        List<Vendor> vendors = Optional.ofNullable(resource.getVendors())
                .orElse(Collections.emptyList())  // null の場合は空リスト
                .stream()
                .filter(Objects::nonNull)         // null エントリーを除外
                .map(PartnerResourceDTOMapper::convertToVendor)       // 変換ロジック
                .toList();

        // Partner の拡張プロパティを設定
        partner = Partner.of(
                partner.getPartnerCode(),
                partner.getPartnerName(),
                partner.getVendorType(),
                partner.getAddress(),
                partner.getTradeProhibitedFlag(),
                partner.getMiscellaneousType(),
                partner.getPartnerGroupCode(),
                partner.getCredit(),
                customers,  // 変換した Customers リスト
                vendors     // 変換した Vendors リスト
        );

        return partner;
    }

    private static Customer convertToCustomer(CustomerResource resource) {
        // Invoiceオブジェクトの生成（ClosingInvoiceを2つ含む）
        Invoice invoice = Invoice.of(
                resource.getCustomerBillingType(),
                ClosingInvoice.of(
                        resource.getCustomerClosingDay1().getValue(),
                        resource.getCustomerPaymentMonth1().getValue(),
                        resource.getCustomerPaymentDay1().getValue(),
                        resource.getCustomerPaymentMethod1().getValue()
                ),
                ClosingInvoice.of(
                        resource.getCustomerClosingDay2().getValue(),
                        resource.getCustomerPaymentMonth2().getValue(),
                        resource.getCustomerPaymentDay2().getValue(),
                        resource.getCustomerPaymentMethod2().getValue()
                )
        );

        // Customerオブジェクトの生成
        return Customer.of(
                CustomerCode.of(resource.getCustomerCode(), resource.getCustomerBranchNumber()),
                resource.getCustomerType(),
                BillingCode.of(resource.getBillingCode(), resource.getBillingBranchNumber()),
                CollectionCode.of(resource.getCollectionCode(), resource.getCollectionBranchNumber()),
                CustomerName.of(resource.getCustomerName(), resource.getCustomerNameKana()),
                resource.getCompanyRepresentativeCode(),
                resource.getCustomerRepresentativeName(),
                resource.getCustomerDepartmentName(),
                Address.of(
                        resource.getCustomerPostalCode(),
                        resource.getCustomerPrefecture(),
                        resource.getCustomerAddress1(),
                        resource.getCustomerAddress2()
                ),
                PhoneNumber.of(resource.getCustomerPhoneNumber()),
                FaxNumber.of(resource.getCustomerFaxNumber()),
                EmailAddress.of(resource.getCustomerEmailAddress()),
                invoice,
                Optional.ofNullable(resource.getShippings())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(PartnerResourceDTOMapper::convertToShipping)
                        .toList()
        );
    }

    static Shipping convertToShipping(ShippingResource shippingResource) {
        return Shipping.of(
                shippingResource.getCustomerCode(),
                shippingResource.getDestinationNumber(),
                shippingResource.getCustomerBranchNumber(),
                shippingResource.getDestinationName(),
                shippingResource.getRegionCode(),
                shippingResource.getPostalCode(),
                shippingResource.getAddress1(),
                shippingResource.getAddress2()
        );
    }

    private static Vendor convertToVendor(VendorResource resource) {
        return Vendor.of(
                VendorCode.of(resource.getVendorCode(), resource.getVendorBranchNumber()),
                VendorName.of(resource.getVendorName(), resource.getVendorNameKana()),
                resource.getVendorContactName(),
                resource.getVendorDepartmentName(),
                Address.of(
                        resource.getVendorPostalCode(),
                        resource.getVendorPrefecture(),
                        resource.getVendorAddress1(),
                        resource.getVendorAddress2()
                ),
                PhoneNumber.of(resource.getVendorPhoneNumber()),
                FaxNumber.of(resource.getVendorFaxNumber()),
                EmailAddress.of(resource.getVendorEmailAddress()),
                ClosingInvoice.of(
                        resource.getVendorClosingDate().getValue(),
                        resource.getVendorPaymentMonth().getValue(),
                        resource.getVendorPaymentDate().getValue(),
                        resource.getVendorPaymentMethod().getValue()
                )
        );
    }

    public static PartnerCriteria convertToCriteria(PartnerCriteriaResource resource) {
        return PartnerCriteria.builder()
                .partnerCode(resource.getPartnerCode())
                .partnerName(resource.getPartnerName())
                .partnerNameKana(resource.getPartnerNameKana())
                .vendorType(ResourceDTOMapperHelper.mapStringToCode(resource.getVendorType(), VendorType::getCodeByName))
                .postalCode(resource.getPostalCode())
                .prefecture(resource.getPrefecture())
                .address1(resource.getAddress1())
                .address2(resource.getAddress2())
                .tradeProhibitedFlag(ResourceDTOMapperHelper.mapStringToCode(resource.getTradeProhibitedFlag(), TradeProhibitedFlag::getCodeByName))
                .miscellaneousType(ResourceDTOMapperHelper.mapStringToCode(resource.getMiscellaneousType(), MiscellaneousType::getCodeByName))
                .partnerGroupCode(resource.getPartnerGroupCode())
                .creditLimit(resource.getCreditLimit())
                .temporaryCreditIncrease(resource.getTemporaryCreditIncrease())
                .build();
    }

}
