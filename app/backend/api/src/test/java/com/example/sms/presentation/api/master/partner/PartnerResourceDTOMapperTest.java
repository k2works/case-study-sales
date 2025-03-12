package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.MiscellaneousType;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.TradeProhibitedFlag;
import com.example.sms.domain.model.master.partner.customer.*;
import com.example.sms.domain.model.master.partner.invoice.ClosingDate;
import com.example.sms.domain.model.master.partner.invoice.PaymentDay;
import com.example.sms.domain.model.master.partner.invoice.PaymentMethod;
import com.example.sms.domain.model.master.partner.invoice.PaymentMonth;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.domain.model.master.partner.vendor.VendorType;
import com.example.sms.domain.model.master.region.RegionCode;
import com.example.sms.domain.type.address.Address;
import com.example.sms.service.master.partner.PartnerCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("取引先DTOマッパーテスト")
class PartnerResourceDTOMapperTest {

    @Test
    @DisplayName("取引先リソースを取引先エンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnPartner() {
        // Arrange
        String partnerCode = "123";
        String partnerName = "テスト取引先";
        String partnerNameKana = "テストトリヒキサキ";
        VendorType vendorType = VendorType.仕入先;
        String postalCode = "123-4567";
        String prefecture = "東京都";
        String address1 = "千代田区";
        String address2 = "1-1-1";
        TradeProhibitedFlag tradeProhibitedFlag = TradeProhibitedFlag.OFF;
        MiscellaneousType miscellaneousType = MiscellaneousType.対象;
        String partnerGroupCode = "1234";
        Integer creditLimit = 1000000;
        Integer temporaryCreditIncrease = 500000;

        // Create customer resource
        CustomerResource customerResource = CustomerResource.builder()
                .customerCode("001")
                .customerBranchNumber(1)
                .customerType(CustomerType.顧客)
                .billingCode("001")
                .billingBranchNumber(1)
                .collectionCode("001")
                .collectionBranchNumber(1)
                .customerName("テスト顧客")
                .customerNameKana("テストキャクサマ")
                .companyRepresentativeCode("CR001")
                .customerRepresentativeName("担当者太郎")
                .customerDepartmentName("営業部")
                .customerPostalCode("123-4567")
                .customerPrefecture("東京都")
                .customerAddress1("千代田区")
                .customerAddress2("1-1-1")
                .customerPhoneNumber("03-1234-5678")
                .customerFaxNumber("03-8765-4321")
                .customerEmailAddress("test@example.com")
                .customerBillingType(CustomerBillingCategory.都度請求)
                .customerClosingDay1(ClosingDate.末日)
                .customerPaymentMonth1(PaymentMonth.翌月)
                .customerPaymentDay1(PaymentDay.末日)
                .customerPaymentMethod1(PaymentMethod.振込)
                .customerClosingDay2(ClosingDate.十日)
                .customerPaymentMonth2(PaymentMonth.翌月)
                .customerPaymentDay2(PaymentDay.十日)
                .customerPaymentMethod2(PaymentMethod.手形)
                .shippings(List.of(ShippingResource.builder()
                        .shippingCode(ShippingCode.of("001", 1, 1))
                        .destinationName("配送先1")
                        .regionCode(RegionCode.of("R001"))
                        .shippingAddress(Address.of(
                                "123-4567",
                                "東京都",
                                "千代田区",
                                "1-1-1"
                        ))
                        .build()))
                .build();

        // Create vendor resource
        VendorResource vendorResource = VendorResource.builder()
                .vendorCode("001")
                .vendorBranchNumber(1)
                .vendorName("テスト仕入先")
                .vendorNameKana("テストシイレサキ")
                .vendorContactName("仕入担当者")
                .vendorDepartmentName("仕入部")
                .vendorPostalCode("123-4567")
                .vendorPrefecture("東京都")
                .vendorAddress1("千代田区")
                .vendorAddress2("1-1-1")
                .vendorPhoneNumber("03-1234-5678")
                .vendorFaxNumber("03-8765-4321")
                .vendorEmailAddress("vendor@example.com")
                .vendorClosingDate(ClosingDate.末日)
                .vendorPaymentMonth(PaymentMonth.翌月)
                .vendorPaymentDate(PaymentDay.末日)
                .vendorPaymentMethod(PaymentMethod.振込)
                .build();

        PartnerResource resource = PartnerResource.builder()
                .partnerCode(partnerCode)
                .partnerName(partnerName)
                .partnerNameKana(partnerNameKana)
                .vendorType(vendorType)
                .postalCode(postalCode)
                .prefecture(prefecture)
                .address1(address1)
                .address2(address2)
                .tradeProhibitedFlag(tradeProhibitedFlag)
                .miscellaneousType(miscellaneousType)
                .partnerGroupCode(partnerGroupCode)
                .creditLimit(creditLimit)
                .temporaryCreditIncrease(temporaryCreditIncrease)
                .customers(List.of(customerResource))
                .vendors(List.of(vendorResource))
                .build();

        // Act
        Partner partner = PartnerResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(partner);
        assertEquals(partnerCode, partner.getPartnerCode().getValue());
        assertEquals(partnerName, partner.getPartnerName().getName());
        assertEquals(partnerNameKana, partner.getPartnerName().getNameKana());
        assertEquals(vendorType.getValue(), partner.getVendorType().getValue());
        assertEquals(postalCode.replace("-", ""), partner.getAddress().getPostalCode().getValue());
        assertEquals(prefecture, partner.getAddress().getPrefecture().name());
        assertEquals(address1, partner.getAddress().getAddress1());
        assertEquals(address2, partner.getAddress().getAddress2());
        assertEquals(tradeProhibitedFlag.getValue(), partner.getTradeProhibitedFlag().getValue());
        assertEquals(miscellaneousType.getCode(), partner.getMiscellaneousType().getCode());
        assertEquals(partnerGroupCode, partner.getPartnerGroupCode().getValue());
        assertEquals(creditLimit, partner.getCredit().getCreditLimit().getAmount());
        assertEquals(temporaryCreditIncrease, partner.getCredit().getTemporaryCreditIncrease().getAmount());

        // Check customers
        assertNotNull(partner.getCustomers());
        assertEquals(1, partner.getCustomers().size());
        Customer customer = partner.getCustomers().get(0);
        assertEquals("001", customer.getCustomerCode().getCode().getValue());
        assertEquals(1, customer.getCustomerCode().getBranchNumber());

        // Check vendors
        assertNotNull(partner.getVendors());
        assertEquals(1, partner.getVendors().size());
        Vendor vendor = partner.getVendors().get(0);
        assertEquals("001", vendor.getVendorCode().getCode().getValue());
        assertEquals(1, vendor.getVendorCode().getBranchNumber());
    }

    @Test
    @DisplayName("取引先検索条件リソースを取引先検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnPartnerCriteria() {
        // Arrange
        String partnerCode = "123";
        String partnerName = "テスト取引先";
        String partnerNameKana = "テストトリヒキサキ";
        String vendorType = "仕入先";
        String postalCode = "123-4567";
        String prefecture = "東京都";
        String address1 = "千代田区";
        String address2 = "1-1-1";
        String tradeProhibitedFlag = "ON";
        String miscellaneousType = "対象";
        String partnerGroupCode = "1234";
        Integer creditLimit = 1000000;
        Integer temporaryCreditIncrease = 500000;

        PartnerCriteriaResource resource = new PartnerCriteriaResource();
        resource.setPartnerCode(partnerCode);
        resource.setPartnerName(partnerName);
        resource.setPartnerNameKana(partnerNameKana);
        resource.setVendorType(vendorType);
        resource.setPostalCode(postalCode);
        resource.setPrefecture(prefecture);
        resource.setAddress1(address1);
        resource.setAddress2(address2);
        resource.setTradeProhibitedFlag(tradeProhibitedFlag);
        resource.setMiscellaneousType(miscellaneousType);
        resource.setPartnerGroupCode(partnerGroupCode);
        resource.setCreditLimit(creditLimit);
        resource.setTemporaryCreditIncrease(temporaryCreditIncrease);

        // Act
        PartnerCriteria criteria = PartnerResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(partnerCode, criteria.getPartnerCode());
        assertEquals(partnerName, criteria.getPartnerName());
        assertEquals(partnerNameKana, criteria.getPartnerNameKana());
        assertEquals(VendorType.仕入先.getValue(), criteria.getVendorType());
        assertEquals(postalCode, criteria.getPostalCode());
        assertEquals(prefecture, criteria.getPrefecture());
        assertEquals(address1, criteria.getAddress1());
        assertEquals(address2, criteria.getAddress2());
        assertEquals(TradeProhibitedFlag.ON.getValue(), criteria.getTradeProhibitedFlag());
        assertEquals(MiscellaneousType.対象.getCode(), criteria.getMiscellaneousType());
        assertEquals(partnerGroupCode, criteria.getPartnerGroupCode());
        assertEquals(creditLimit, criteria.getCreditLimit());
        assertEquals(temporaryCreditIncrease, criteria.getTemporaryCreditIncrease());
    }

    @Test
    @DisplayName("取引先検索条件リソースにnull値がある場合でも変換できる")
    void testConvertToCriteria_nullValuesInResource_shouldHandleNulls() {
        // Arrange
        PartnerCriteriaResource resource = new PartnerCriteriaResource();
        resource.setPartnerCode(null);
        resource.setPartnerName(null);
        resource.setPartnerNameKana(null);
        resource.setVendorType(null);
        resource.setPostalCode(null);
        resource.setPrefecture(null);
        resource.setAddress1(null);
        resource.setAddress2(null);
        resource.setTradeProhibitedFlag(null);
        resource.setMiscellaneousType(null);
        resource.setPartnerGroupCode(null);
        resource.setCreditLimit(null);
        resource.setTemporaryCreditIncrease(null);

        // Act
        PartnerCriteria criteria = PartnerResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertNull(criteria.getPartnerCode());
        assertNull(criteria.getPartnerName());
        assertNull(criteria.getPartnerNameKana());
        assertNull(criteria.getVendorType());
        assertNull(criteria.getPostalCode());
        assertNull(criteria.getPrefecture());
        assertNull(criteria.getAddress1());
        assertNull(criteria.getAddress2());
        assertNull(criteria.getTradeProhibitedFlag());
        assertNull(criteria.getMiscellaneousType());
        assertNull(criteria.getPartnerGroupCode());
        assertNull(criteria.getCreditLimit());
        assertNull(criteria.getTemporaryCreditIncrease());
    }
}
