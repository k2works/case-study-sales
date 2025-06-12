package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.customer.*;
import com.example.sms.domain.model.master.partner.billing.ClosingDate;
import com.example.sms.domain.model.master.partner.billing.PaymentDay;
import com.example.sms.domain.model.master.partner.billing.PaymentMethod;
import com.example.sms.domain.model.master.partner.billing.PaymentMonth;
import com.example.sms.domain.model.master.region.RegionCode;
import com.example.sms.domain.type.address.Address;
import com.example.sms.domain.type.address.PostalCode;
import com.example.sms.domain.type.address.Prefecture;
import com.example.sms.service.master.partner.CustomerCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
                .customerCode("001")
                .customerBranchNumber(1)
                .destinationNumber(1)
                .destinationName("配送先1")
                .regionCode("R001")
                .postalCode("123-4567")
                .prefecture("東京都")
                .address1("千代田区")
                .address2("1-1-1")
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

    @Nested
    @DisplayName("出荷先リソース")
    class ShippingResourceTest {
        @Test
        @DisplayName("単一のShippingインスタンスをShippingResourceに変換する")
        void testFrom_withSingleShipping_shouldReturnShippingResource() {
            // Arrange
            ShippingCode shippingCode = new ShippingCode("001", 1, 1);
            Address address = new Address(PostalCode.of("123-4567"), Prefecture.東京都, "address1", "address2");
            Shipping shipping = new Shipping(shippingCode, "destination_name", RegionCode.of("R001"), address);

            // Act
            ShippingResource result = ShippingResource.from(shipping);

            // Assert
            assertNotNull(result);
            assertEquals("001", result.getCustomerCode());
            assertEquals(1, result.getCustomerBranchNumber());
            assertEquals(1, result.getDestinationNumber());
            assertEquals("destination_name", result.getDestinationName());
            assertEquals("R001", result.getRegionCode());
            assertEquals("1234567", result.getPostalCode());
            assertEquals("東京都", result.getPrefecture());
            assertEquals("address1", result.getAddress1());
            assertEquals("address2", result.getAddress2());
        }

        @Test
        @DisplayName("ShippingのリストをShippingResourceのリストに変換する")
        void testFrom_withListOfShipping_shouldReturnListOfShippingResource() {
            // Arrange
            ShippingCode shippingCode1 = new ShippingCode("001", 1, 1);
            Address address1 = new Address(PostalCode.of("123-4567"), Prefecture.東京都, "address11", "address21");
            Shipping shipping1 = new Shipping(shippingCode1, "destination_name1", RegionCode.of("R001"), address1);

            ShippingCode shippingCode2 = new ShippingCode("002", 2, 2);
            Address address2 = new Address(PostalCode.of("987-5432"), Prefecture.大阪府, "address12", "address22");
            Shipping shipping2 = new Shipping(shippingCode2, "destination_name2", RegionCode.of("R002"), address2);

            List<Shipping> shippings = new ArrayList<>();
            shippings.add(shipping1);
            shippings.add(shipping2);

            // Act
            List<ShippingResource> result = ShippingResource.from(shippings);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());

            ShippingResource result1 = result.get(0);
            ShippingResource result2 = result.get(1);

            // 1つ目のShippingResourceを検証
            assertEquals("001", result1.getCustomerCode());
            assertEquals(1, result1.getCustomerBranchNumber());
            assertEquals(1, result1.getDestinationNumber());
            assertEquals("destination_name1", result1.getDestinationName());
            assertEquals("R001", result1.getRegionCode());
            assertEquals("1234567", result1.getPostalCode());
            assertEquals("東京都", result1.getPrefecture());
            assertEquals("address11", result1.getAddress1());
            assertEquals("address21", result1.getAddress2());

            // 2つ目のShippingResourceを検証
            assertEquals("002", result2.getCustomerCode());
            assertEquals(2, result2.getCustomerBranchNumber());
            assertEquals(2, result2.getDestinationNumber());
            assertEquals("destination_name2", result2.getDestinationName());
            assertEquals("R002", result2.getRegionCode());
            assertEquals("9875432", result2.getPostalCode());
            assertEquals("大阪府", result2.getPrefecture());
            assertEquals("address12", result2.getAddress1());
            assertEquals("address22", result2.getAddress2());
        }

        @Test
        @DisplayName("nullのShippingを渡した場合、例外をスローする")
        void testFrom_withNullShipping_shouldThrowException() {
            // Arrange, Act & Assert
            try {
                ShippingResource.from((Shipping.of(null, null, null, null)));
            } catch (NullPointerException e) {
                assertNotNull(e.getMessage());
            }
        }

        @Test
        @DisplayName("nullのリストを渡すと例外をスローする")
        void testFrom_withNullList_shouldThrowException() {
            // Arrange, Act & Assert
            try {
                ShippingResource.from(List.of(Shipping.of(null, null, null, null)));
            } catch (NullPointerException e) {
                assertNotNull(e.getMessage());
            }
        }
    }
}
