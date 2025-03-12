package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.customer.*;
import com.example.sms.domain.model.master.partner.invoice.ClosingDate;
import com.example.sms.domain.model.master.partner.invoice.PaymentDay;
import com.example.sms.domain.model.master.partner.invoice.PaymentMethod;
import com.example.sms.domain.model.master.partner.invoice.PaymentMonth;
import com.example.sms.domain.model.master.region.RegionCode;
import com.example.sms.domain.type.address.Address;
import com.example.sms.service.master.partner.CustomerCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("顧客DTOマッパーテスト")
class CustomerResourceDTOMapperTest {

    @Test
    @DisplayName("顧客リソースを顧客エンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnCustomer() {
        // Arrange
        String customerCode = "123";
        Integer customerBranchNumber = 1;
        CustomerType customerType = CustomerType.顧客;
        String billingCode = "123";
        Integer billingBranchNumber = 1;
        String collectionCode = "123";
        Integer collectionBranchNumber = 1;
        String customerName = "テスト顧客";
        String customerNameKana = "テストキャクサマ";
        String companyRepresentativeCode = "CR001";
        String customerRepresentativeName = "担当者太郎";
        String customerDepartmentName = "営業部";
        String customerPostalCode = "123-4567";
        String customerPrefecture = "東京都";
        String customerAddress1 = "千代田区";
        String customerAddress2 = "1-1-1";
        String customerPhoneNumber = "03-1234-5678";
        String customerFaxNumber = "03-8765-4321";
        String customerEmailAddress = "test@example.com";
        CustomerBillingCategory customerBillingType = CustomerBillingCategory.都度請求;
        ClosingDate customerClosingDay1 = ClosingDate.末日;
        PaymentMonth customerPaymentMonth1 = PaymentMonth.翌月;
        PaymentDay customerPaymentDay1 = PaymentDay.末日;
        PaymentMethod customerPaymentMethod1 = PaymentMethod.振込;
        ClosingDate customerClosingDay2 = ClosingDate.十日;
        PaymentMonth customerPaymentMonth2 = PaymentMonth.翌月;
        PaymentDay customerPaymentDay2 = PaymentDay.十日;
        PaymentMethod customerPaymentMethod2 = PaymentMethod.手形;

        // Create shipping resource
        ShippingResource shippingResource = ShippingResource.builder()
                .shippingCode(ShippingCode.of("001", 1, 1))
                .destinationName("配送先1")
                .regionCode(RegionCode.of("R001"))
                .shippingAddress(Address.of(
                        "123-4567",
                        "東京都",
                        "千代田区",
                        "1-1-1"
                ))
                .build();

        CustomerResource resource = CustomerResource.builder()
                .customerCode(customerCode)
                .customerBranchNumber(customerBranchNumber)
                .customerType(customerType)
                .billingCode(billingCode)
                .billingBranchNumber(billingBranchNumber)
                .collectionCode(collectionCode)
                .collectionBranchNumber(collectionBranchNumber)
                .customerName(customerName)
                .customerNameKana(customerNameKana)
                .companyRepresentativeCode(companyRepresentativeCode)
                .customerRepresentativeName(customerRepresentativeName)
                .customerDepartmentName(customerDepartmentName)
                .customerPostalCode(customerPostalCode)
                .customerPrefecture(customerPrefecture)
                .customerAddress1(customerAddress1)
                .customerAddress2(customerAddress2)
                .customerPhoneNumber(customerPhoneNumber)
                .customerFaxNumber(customerFaxNumber)
                .customerEmailAddress(customerEmailAddress)
                .customerBillingType(customerBillingType)
                .customerClosingDay1(customerClosingDay1)
                .customerPaymentMonth1(customerPaymentMonth1)
                .customerPaymentDay1(customerPaymentDay1)
                .customerPaymentMethod1(customerPaymentMethod1)
                .customerClosingDay2(customerClosingDay2)
                .customerPaymentMonth2(customerPaymentMonth2)
                .customerPaymentDay2(customerPaymentDay2)
                .customerPaymentMethod2(customerPaymentMethod2)
                .shippings(List.of(shippingResource))
                .build();

        // Act
        Customer customer = CustomerResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(customer);
        assertEquals(customerCode, customer.getCustomerCode().getCode().getValue());
        assertEquals(customerBranchNumber, customer.getCustomerCode().getBranchNumber());
        assertEquals(customerType.getValue(), customer.getCustomerType().getValue());
        assertEquals(billingCode, customer.getBillingCode().getCode().getValue());
        assertEquals(billingBranchNumber, customer.getBillingCode().getBranchNumber());
        assertEquals(collectionCode, customer.getCollectionCode().getCode().getValue());
        assertEquals(collectionBranchNumber, customer.getCollectionCode().getBranchNumber());
        assertEquals(customerName, customer.getCustomerName().getValue().getName());
        assertEquals(customerNameKana, customer.getCustomerName().getValue().getNameKana());
        assertEquals(companyRepresentativeCode, customer.getCompanyRepresentativeCode());
        assertEquals(customerRepresentativeName, customer.getCustomerRepresentativeName());
        assertEquals(customerDepartmentName, customer.getCustomerDepartmentName());
        assertEquals(customerPostalCode.replace("-", ""), customer.getCustomerAddress().getPostalCode().getValue());
        assertEquals(customerPrefecture, customer.getCustomerAddress().getPrefecture().name());
        assertEquals(customerAddress1, customer.getCustomerAddress().getAddress1());
        assertEquals(customerAddress2, customer.getCustomerAddress().getAddress2());
        assertEquals(customerPhoneNumber, customer.getCustomerPhoneNumber().getValue());
        assertEquals(customerFaxNumber, customer.getCustomerFaxNumber().getValue());
        assertEquals(customerEmailAddress, customer.getCustomerEmailAddress().getValue());
        assertEquals(customerBillingType.getValue(), customer.getInvoice().getCustomerBillingCategory().getValue());
        assertEquals(customerClosingDay1.getValue(), customer.getInvoice().getClosingInvoice1().getClosingDay().getValue());
        assertEquals(customerPaymentMonth1.getValue(), customer.getInvoice().getClosingInvoice1().getPaymentMonth().getValue());
        assertEquals(customerPaymentDay1.getValue(), customer.getInvoice().getClosingInvoice1().getPaymentDay().getValue());
        assertEquals(customerPaymentMethod1.getValue(), customer.getInvoice().getClosingInvoice1().getPaymentMethod().getValue());
        assertEquals(customerClosingDay2.getValue(), customer.getInvoice().getClosingInvoice2().getClosingDay().getValue());
        assertEquals(customerPaymentMonth2.getValue(), customer.getInvoice().getClosingInvoice2().getPaymentMonth().getValue());
        assertEquals(customerPaymentDay2.getValue(), customer.getInvoice().getClosingInvoice2().getPaymentDay().getValue());
        assertEquals(customerPaymentMethod2.getValue(), customer.getInvoice().getClosingInvoice2().getPaymentMethod().getValue());

        // Check shipping
        assertNotNull(customer.getShippings());
        assertEquals(1, customer.getShippings().size());
        Shipping shipping = customer.getShippings().get(0);
        assertEquals("001", shipping.getShippingCode().getCustomerCode().getCode().getValue());
        assertEquals(1, shipping.getShippingCode().getDestinationNumber());
        assertEquals("配送先1", shipping.getDestinationName());
        assertEquals("R001", shipping.getRegionCode().getValue());
        assertEquals("1234567", shipping.getShippingAddress().getPostalCode().getValue());
        assertEquals("東京都", shipping.getShippingAddress().getPrefecture().name());
        assertEquals("千代田区", shipping.getShippingAddress().getAddress1());
        assertEquals("1-1-1", shipping.getShippingAddress().getAddress2());
    }

    @Test
    @DisplayName("顧客検索条件リソースを顧客検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnCustomerCriteria() {
        // Arrange
        String customerCode = "123";
        String customerName = "テスト顧客";
        String customerNameKana = "テストキャクサマ";
        String customerType = "顧客";
        String companyRepresentativeCode = "CR001";
        String customerRepresentativeName = "担当者太郎";
        String customerDepartmentName = "営業部";
        String postalCode = "123-4567";
        String prefecture = "東京都";
        String address1 = "千代田区";
        String address2 = "1-1-1";
        String customerPhoneNumber = "03-1234-5678";
        String customerFaxNumber = "03-8765-4321";
        String customerEmailAddress = "test@example.com";
        String customerBillingCategory = "都度請求";

        CustomerCriteriaResource resource = new CustomerCriteriaResource();
        resource.setCustomerCode(customerCode);
        resource.setCustomerName(customerName);
        resource.setCustomerNameKana(customerNameKana);
        resource.setCustomerType(customerType);
        resource.setCompanyRepresentativeCode(companyRepresentativeCode);
        resource.setCustomerRepresentativeName(customerRepresentativeName);
        resource.setCustomerDepartmentName(customerDepartmentName);
        resource.setPostalCode(postalCode);
        resource.setPrefecture(prefecture);
        resource.setAddress1(address1);
        resource.setAddress2(address2);
        resource.setCustomerPhoneNumber(customerPhoneNumber);
        resource.setCustomerFaxNumber(customerFaxNumber);
        resource.setCustomerEmailAddress(customerEmailAddress);
        resource.setCustomerBillingCategory(customerBillingCategory);

        // Act
        CustomerCriteria criteria = CustomerResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(customerCode, criteria.getCustomerCode());
        assertEquals(customerName, criteria.getCustomerName());
        assertEquals(customerNameKana, criteria.getCustomerNameKana());
        assertEquals(CustomerType.顧客.getValue(), criteria.getCustomerType());
        assertEquals(companyRepresentativeCode, criteria.getCompanyRepresentativeCode());
        assertEquals(customerRepresentativeName, criteria.getCustomerRepresentativeName());
        assertEquals(customerDepartmentName, criteria.getCustomerDepartmentName());
        assertEquals(postalCode, criteria.getPostalCode());
        assertEquals(prefecture, criteria.getPrefecture());
        assertEquals(address1, criteria.getAddress1());
        assertEquals(address2, criteria.getAddress2());
        assertEquals(customerPhoneNumber, criteria.getCustomerPhoneNumber());
        assertEquals(customerFaxNumber, criteria.getCustomerFaxNumber());
        assertEquals(customerEmailAddress, criteria.getCustomerEmailAddress());
        assertEquals(CustomerBillingCategory.都度請求.getValue(), criteria.getCustomerBillingCategory());
    }

    @Test
    @DisplayName("顧客検索条件リソースにnull値がある場合でも変換できる")
    void testConvertToCriteria_nullValuesInResource_shouldHandleNulls() {
        // Arrange
        CustomerCriteriaResource resource = new CustomerCriteriaResource();
        resource.setCustomerCode(null);
        resource.setCustomerName(null);
        resource.setCustomerNameKana(null);
        resource.setCustomerType(null);
        resource.setCompanyRepresentativeCode(null);
        resource.setCustomerRepresentativeName(null);
        resource.setCustomerDepartmentName(null);
        resource.setPostalCode(null);
        resource.setPrefecture(null);
        resource.setAddress1(null);
        resource.setAddress2(null);
        resource.setCustomerPhoneNumber(null);
        resource.setCustomerFaxNumber(null);
        resource.setCustomerEmailAddress(null);
        resource.setCustomerBillingCategory(null);

        // Act
        CustomerCriteria criteria = CustomerResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertNull(criteria.getCustomerCode());
        assertNull(criteria.getCustomerName());
        assertNull(criteria.getCustomerNameKana());
        assertNull(criteria.getCustomerType());
        assertNull(criteria.getCompanyRepresentativeCode());
        assertNull(criteria.getCustomerRepresentativeName());
        assertNull(criteria.getCustomerDepartmentName());
        assertNull(criteria.getPostalCode());
        assertNull(criteria.getPrefecture());
        assertNull(criteria.getAddress1());
        assertNull(criteria.getAddress2());
        assertNull(criteria.getCustomerPhoneNumber());
        assertNull(criteria.getCustomerFaxNumber());
        assertNull(criteria.getCustomerEmailAddress());
        assertNull(criteria.getCustomerBillingCategory());
    }
}
