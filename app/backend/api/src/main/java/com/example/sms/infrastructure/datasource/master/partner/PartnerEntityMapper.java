package com.example.sms.infrastructure.datasource.master.partner;

import com.example.sms.domain.model.master.partner.Customer;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.Shipping;
import com.example.sms.domain.model.master.partner.Vendor;
import com.example.sms.infrastructure.datasource.autogen.model.仕入先マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.出荷先マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.取引先マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.顧客マスタ;
import com.example.sms.infrastructure.datasource.master.partner.customer.CustomerCustomEntity;
import com.example.sms.infrastructure.datasource.master.partner.vendor.VendorCustomEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PartnerEntityMapper {
    public 取引先マスタ mapToEntity(Partner partner) {
        取引先マスタ partnerEntity = new 取引先マスタ();
        partnerEntity.set取引先コード(partner.getPartnerCode().getValue());
        partnerEntity.set取引先名(partner.getPartnerName().getName());
        partnerEntity.set取引先名カナ(partner.getPartnerName().getNameKana());
        partnerEntity.set仕入先区分(partner.getVendorType().getValue());
        partnerEntity.set郵便番号(partner.getAddress().getPostalCode().getValue());
        partnerEntity.set都道府県(partner.getAddress().getPrefecture().toString());
        partnerEntity.set住所１(partner.getAddress().getAddress1());
        partnerEntity.set住所２(partner.getAddress().getAddress2());
        partnerEntity.set取引禁止フラグ(partner.getTradeProhibitedFlag().getValue());
        partnerEntity.set雑区分(partner.getMiscellaneousType().getCode());
        partnerEntity.set取引先グループコード(partner.getPartnerGroupCode().getValue());
        partnerEntity.set与信限度額(partner.getCredit().getCreditLimit().getAmount());
        partnerEntity.set与信一時増加枠(partner.getCredit().getTemporaryCreditIncrease().getAmount());

        return partnerEntity;
    }

    public Partner mapToDomainModel(PartnerCustomEntity partnerCustomEntity) {
        List<Customer> customers = partnerCustomEntity.get顧客マスタ().stream()
                .map(customer -> {
                    Customer newCustomer = Customer.of(
                            customer.get顧客コード(),
                            customer.get顧客枝番(),
                            customer.get顧客区分(),
                            customer.get請求先コード(),
                            customer.get請求先枝番(),
                            customer.get回収先コード(),
                            customer.get回収先枝番(),
                            customer.get顧客名(),
                            customer.get顧客名カナ(),
                            customer.get自社担当者コード(),
                            customer.get顧客担当者名(),
                            customer.get顧客部門名(),
                            customer.get顧客郵便番号(),
                            customer.get顧客都道府県(),
                            customer.get顧客住所１(),
                            customer.get顧客住所２(),
                            customer.get顧客電話番号(),
                            customer.get顧客ｆａｘ番号(),
                            customer.get顧客メールアドレス(),
                            customer.get顧客請求区分(),
                            customer.get顧客締日１(),
                            customer.get顧客支払月１(),
                            customer.get顧客支払日１(),
                            customer.get顧客支払方法１(),
                            customer.get顧客締日２(),
                            customer.get顧客支払月２(),
                            customer.get顧客支払日２(),
                            customer.get顧客支払方法２()
                    );

                    List<Shipping> shippings = customer.get出荷先マスタ() != null
                            ? customer.get出荷先マスタ().stream()
                            .map(shipping -> Shipping.of(
                                    shipping.get顧客コード(),
                                    shipping.get出荷先番号(),
                                    shipping.get顧客枝番(),
                                    shipping.get出荷先名(),
                                    shipping.get地域コード(),
                                    shipping.get出荷先郵便番号(),
                                    shipping.get出荷先住所１(),
                                    shipping.get出荷先住所２()
                            ))
                            .toList()
                            : List.of();

                    return Customer.of(
                            newCustomer,
                            shippings
                    );
                })
                .toList();

        List<Vendor> vendors = partnerCustomEntity.get仕入先マスタ().stream()
                .map(vendor -> Vendor.of(
                        vendor.get仕入先コード(),
                        vendor.get仕入先枝番(),
                        vendor.get仕入先名(),
                        vendor.get仕入先名カナ(),
                        vendor.get仕入先担当者名(),
                        vendor.get仕入先部門名(),
                        vendor.get仕入先郵便番号(),
                        vendor.get仕入先都道府県(),
                        vendor.get仕入先住所１(),
                        vendor.get仕入先住所２(),
                        vendor.get仕入先電話番号(),
                        vendor.get仕入先ｆａｘ番号(),
                        vendor.get仕入先メールアドレス(),
                        vendor.get仕入先締日(),
                        vendor.get仕入先支払月(),
                        vendor.get仕入先支払日(),
                        vendor.get仕入先支払方法()
                ))
                .toList();

        Partner partner = Partner.of(
                partnerCustomEntity.get取引先コード(),
                partnerCustomEntity.get取引先名(),
                partnerCustomEntity.get取引先名カナ(),
                partnerCustomEntity.get仕入先区分(),
                partnerCustomEntity.get郵便番号(),
                partnerCustomEntity.get都道府県(),
                partnerCustomEntity.get住所１(),
                partnerCustomEntity.get住所２(),
                partnerCustomEntity.get取引禁止フラグ(),
                partnerCustomEntity.get雑区分(),
                partnerCustomEntity.get取引先グループコード(),
                partnerCustomEntity.get与信限度額(),
                partnerCustomEntity.get与信一時増加枠()
        );

        if (!vendors.isEmpty()) {
            return Partner.ofWithVendors(partner, vendors);
        } else {
            return Partner.ofWithCustomers(partner, customers);
        }
    }

    public 顧客マスタ mapToEntity(Customer customer) {
        顧客マスタ customerEntity = new 顧客マスタ();
        customerEntity.set顧客コード(customer.getCustomerCode().getCode().getValue());
        customerEntity.set顧客枝番(customer.getCustomerCode().getBranchNumber());
        customerEntity.set顧客区分(customer.getCustomerType().getValue());
        customerEntity.set請求先コード(customer.getBillingCode().getCode().getValue());
        customerEntity.set請求先枝番(customer.getBillingCode().getBranchNumber());
        customerEntity.set回収先コード(customer.getCollectionCode().getCode().getValue());
        customerEntity.set回収先枝番(customer.getCollectionCode().getBranchNumber());
        customerEntity.set顧客名(customer.getCustomerName().getValue().getName());
        customerEntity.set顧客名カナ(customer.getCustomerName().getValue().getNameKana());
        customerEntity.set自社担当者コード(customer.getCompanyRepresentativeCode());
        customerEntity.set顧客担当者名(customer.getCustomerRepresentativeName());
        customerEntity.set顧客部門名(customer.getCustomerDepartmentName());
        customerEntity.set顧客郵便番号(customer.getCustomerAddress().getPostalCode().getValue());
        customerEntity.set顧客都道府県(customer.getCustomerAddress().getPrefecture().toString());
        customerEntity.set顧客住所１(customer.getCustomerAddress().getAddress1());
        customerEntity.set顧客住所２(customer.getCustomerAddress().getAddress2());
        customerEntity.set顧客電話番号(customer.getCustomerPhoneNumber().getValue());
        customerEntity.set顧客ｆａｘ番号(customer.getCustomerFaxNumber().getValue());
        customerEntity.set顧客メールアドレス(customer.getCustomerEmailAddress().getValue());
        customerEntity.set顧客請求区分(customer.getInvoice().getCustomerBillingCategory().getValue());
        customerEntity.set顧客締日１(customer.getInvoice().getClosingInvoice1().getCustomerClosingDay().getValue());
        customerEntity.set顧客支払月１(customer.getInvoice().getClosingInvoice1().getCustomerPaymentMonth().getValue());
        customerEntity.set顧客支払日１(customer.getInvoice().getClosingInvoice1().getCustomerPaymentDay().getValue());
        customerEntity.set顧客支払方法１(customer.getInvoice().getClosingInvoice1().getCustomerPaymentMethod().getValue());
        customerEntity.set顧客締日２(customer.getInvoice().getClosingInvoice2().getCustomerClosingDay().getValue());
        customerEntity.set顧客支払月２(customer.getInvoice().getClosingInvoice2().getCustomerPaymentMonth().getValue());
        customerEntity.set顧客支払日２(customer.getInvoice().getClosingInvoice2().getCustomerPaymentDay().getValue());
        customerEntity.set顧客支払方法２(customer.getInvoice().getClosingInvoice2().getCustomerPaymentMethod().getValue());

        return customerEntity;
    }

    public Customer mapToDomainModel(CustomerCustomEntity customerCustomEntity) {
        return Customer.of(
                customerCustomEntity.get顧客コード(),
                customerCustomEntity.get顧客枝番(),
                customerCustomEntity.get顧客区分(),
                customerCustomEntity.get請求先コード(),
                customerCustomEntity.get請求先枝番(),
                customerCustomEntity.get回収先コード(),
                customerCustomEntity.get回収先枝番(),
                customerCustomEntity.get顧客名(),
                customerCustomEntity.get顧客名カナ(),
                customerCustomEntity.get自社担当者コード(),
                customerCustomEntity.get顧客担当者名(),
                customerCustomEntity.get顧客部門名(),
                customerCustomEntity.get顧客郵便番号(),
                customerCustomEntity.get顧客都道府県(),
                customerCustomEntity.get顧客住所１(),
                customerCustomEntity.get顧客住所２(),
                customerCustomEntity.get顧客電話番号(),
                customerCustomEntity.get顧客ｆａｘ番号(),
                customerCustomEntity.get顧客メールアドレス(),
                customerCustomEntity.get顧客請求区分(),
                customerCustomEntity.get顧客締日１(),
                customerCustomEntity.get顧客支払月１(),
                customerCustomEntity.get顧客支払日１(),
                customerCustomEntity.get顧客支払方法１(),
                customerCustomEntity.get顧客締日２(),
                customerCustomEntity.get顧客支払月２(),
                customerCustomEntity.get顧客支払日２(),
                customerCustomEntity.get顧客支払方法２()
        );
    }

    public 出荷先マスタ mapToEntity(Shipping shipping) {
        出荷先マスタ shippingEntity = new 出荷先マスタ();
        shippingEntity.set顧客コード(shipping.getCustomerCode());
        shippingEntity.set出荷先番号(shipping.getDestinationNumber());
        shippingEntity.set顧客枝番(shipping.getCustomerBranchNumber());
        shippingEntity.set出荷先名(shipping.getDestinationName());
        shippingEntity.set地域コード(shipping.getRegionCode());
        shippingEntity.set出荷先郵便番号(shipping.getDestinationPostalCode());
        shippingEntity.set出荷先住所１(shipping.getDestinationAddress1());
        shippingEntity.set出荷先住所２(shipping.getDestinationAddress2());

        return shippingEntity;
    }

    public 仕入先マスタ mapToEntity(Vendor vendor) {
        仕入先マスタ vendorEntity = new 仕入先マスタ();
        vendorEntity.set仕入先コード(vendor.getVendorCode().getCode().getValue());
        vendorEntity.set仕入先枝番(vendor.getVendorCode().getBranchNumber());
        vendorEntity.set仕入先名(vendor.getVendorName());
        vendorEntity.set仕入先名カナ(vendor.getVendorNameKana());
        vendorEntity.set仕入先担当者名(vendor.getVendorContactName());
        vendorEntity.set仕入先部門名(vendor.getVendorDepartmentName());
        vendorEntity.set仕入先郵便番号(vendor.getVendorPostalCode());
        vendorEntity.set仕入先都道府県(vendor.getVendorPrefecture());
        vendorEntity.set仕入先住所１(vendor.getVendorAddress1());
        vendorEntity.set仕入先住所２(vendor.getVendorAddress2());
        vendorEntity.set仕入先電話番号(vendor.getVendorPhoneNumber());
        vendorEntity.set仕入先ｆａｘ番号(vendor.getVendorFaxNumber());
        vendorEntity.set仕入先メールアドレス(vendor.getVendorEmailAddress());
        vendorEntity.set仕入先締日(vendor.getVendorClosingDate());
        vendorEntity.set仕入先支払月(vendor.getVendorPaymentMonth());
        vendorEntity.set仕入先支払日(vendor.getVendorPaymentDate());
        vendorEntity.set仕入先支払方法(vendor.getVendorPaymentMethod());

        return vendorEntity;
    }

    public Vendor mapToDomainModel(VendorCustomEntity vendorCustomEntity) {
        return Vendor.of(
                vendorCustomEntity.get仕入先コード(),
                vendorCustomEntity.get仕入先枝番(),
                vendorCustomEntity.get仕入先名(),
                vendorCustomEntity.get仕入先名カナ(),
                vendorCustomEntity.get仕入先担当者名(),
                vendorCustomEntity.get仕入先部門名(),
                vendorCustomEntity.get仕入先郵便番号(),
                vendorCustomEntity.get仕入先都道府県(),
                vendorCustomEntity.get仕入先住所１(),
                vendorCustomEntity.get仕入先住所２(),
                vendorCustomEntity.get仕入先電話番号(),
                vendorCustomEntity.get仕入先ｆａｘ番号(),
                vendorCustomEntity.get仕入先メールアドレス(),
                vendorCustomEntity.get仕入先締日(),
                vendorCustomEntity.get仕入先支払月(),
                vendorCustomEntity.get仕入先支払日(),
                vendorCustomEntity.get仕入先支払方法()
        );
    }
}
