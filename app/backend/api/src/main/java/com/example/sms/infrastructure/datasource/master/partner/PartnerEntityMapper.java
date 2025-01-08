package com.example.sms.infrastructure.datasource.master.partner;

import com.example.sms.domain.model.master.partner.Customer;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.infrastructure.datasource.autogen.model.取引先マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.顧客マスタ;
import com.example.sms.infrastructure.datasource.master.partner.customer.CustomerCustomEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PartnerEntityMapper {
    public 取引先マスタ mapToEntity(Partner partner) {
        取引先マスタ partnerEntity = new 取引先マスタ();
        partnerEntity.set取引先コード(partner.getPartnerCode());
        partnerEntity.set取引先名(partner.getPartnerName());
        partnerEntity.set取引先名カナ(partner.getPartnerNameKana());
        partnerEntity.set仕入先区分(partner.getSupplierType());
        partnerEntity.set郵便番号(partner.getPostalCode());
        partnerEntity.set都道府県(partner.getPrefecture());
        partnerEntity.set住所１(partner.getAddress1());
        partnerEntity.set住所２(partner.getAddress2());
        partnerEntity.set取引禁止フラグ(partner.getTradeProhibitedFlag());
        partnerEntity.set雑区分(partner.getMiscellaneousType());
        partnerEntity.set取引先グループコード(partner.getPartnerGroupCode());
        partnerEntity.set与信限度額(partner.getCreditLimit());
        partnerEntity.set与信一時増加枠(partner.getTemporaryCreditIncrease());

        return partnerEntity;
    }

    public Partner mapToDomain(PartnerCustomEntity partnerCustomEntity) {
        List<Customer> customers = partnerCustomEntity.get顧客マスタ().stream()
                .map(customer -> Customer.of(
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
                )).toList();

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

        return Partner.of(partner, customers);
    }

    public 顧客マスタ mapToEntity(Customer customer) {
        顧客マスタ customerEntity = new 顧客マスタ();
        customerEntity.set顧客コード(customer.getCustomerCode());
        customerEntity.set顧客枝番(customer.getCustomerBranchNumber());
        customerEntity.set顧客区分(customer.getCustomerCategory());
        customerEntity.set請求先コード(customer.getBillingCode());
        customerEntity.set請求先枝番(customer.getBillingBranchNumber());
        customerEntity.set回収先コード(customer.getCollectionCode());
        customerEntity.set回収先枝番(customer.getCollectionBranchNumber());
        customerEntity.set顧客名(customer.getCustomerName());
        customerEntity.set顧客名カナ(customer.getCustomerNameKana());
        customerEntity.set自社担当者コード(customer.getCompanyRepresentativeCode());
        customerEntity.set顧客担当者名(customer.getCustomerRepresentativeName());
        customerEntity.set顧客部門名(customer.getCustomerDepartmentName());
        customerEntity.set顧客郵便番号(customer.getCustomerPostalCode());
        customerEntity.set顧客都道府県(customer.getCustomerPrefecture());
        customerEntity.set顧客住所１(customer.getCustomerAddress1());
        customerEntity.set顧客住所２(customer.getCustomerAddress2());
        customerEntity.set顧客電話番号(customer.getCustomerPhoneNumber());
        customerEntity.set顧客ｆａｘ番号(customer.getCustomerFaxNumber());
        customerEntity.set顧客メールアドレス(customer.getCustomerEmailAddress());
        customerEntity.set顧客請求区分(customer.getCustomerBillingCategory());
        customerEntity.set顧客締日１(customer.getCustomerClosingDay1());
        customerEntity.set顧客支払月１(customer.getCustomerPaymentMonth1());
        customerEntity.set顧客支払日１(customer.getCustomerPaymentDay1());
        customerEntity.set顧客支払方法１(customer.getCustomerPaymentMethod1());
        customerEntity.set顧客締日２(customer.getCustomerClosingDay2());
        customerEntity.set顧客支払月２(customer.getCustomerPaymentMonth2());
        customerEntity.set顧客支払日２(customer.getCustomerPaymentDay2());
        customerEntity.set顧客支払方法２(customer.getCustomerPaymentMethod2());

        return customerEntity;
    }

    public Customer mapToDomain(CustomerCustomEntity customerCustomEntity) {
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
}
