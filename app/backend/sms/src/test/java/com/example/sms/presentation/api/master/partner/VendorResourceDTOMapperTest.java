package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.billing.ClosingDate;
import com.example.sms.domain.model.master.partner.billing.PaymentDay;
import com.example.sms.domain.model.master.partner.billing.PaymentMethod;
import com.example.sms.domain.model.master.partner.billing.PaymentMonth;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.service.master.partner.VendorCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("仕入先DTOマッパーテスト")
class VendorResourceDTOMapperTest {

    @Test
    @DisplayName("仕入先リソースを仕入先エンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnVendor() {
        // Arrange
        String vendorCode = "123";
        Integer vendorBranchNumber = 1;
        String vendorName = "テスト仕入先";
        String vendorNameKana = "テストシイレサキ";
        String vendorContactName = "担当者太郎";
        String vendorDepartmentName = "購買部";
        String vendorPostalCode = "123-4567";
        String vendorPrefecture = "東京都";
        String vendorAddress1 = "千代田区";
        String vendorAddress2 = "1-1-1";
        String vendorPhoneNumber = "03-1234-5678";
        String vendorFaxNumber = "03-8765-4321";
        String vendorEmailAddress = "vendor@example.com";
        ClosingDate vendorClosingDate = ClosingDate.末日;
        PaymentMonth vendorPaymentMonth = PaymentMonth.翌月;
        PaymentDay vendorPaymentDate = PaymentDay.末日;
        PaymentMethod vendorPaymentMethod = PaymentMethod.振込;

        VendorResource resource = VendorResource.builder()
                .vendorCode(vendorCode)
                .vendorBranchNumber(vendorBranchNumber)
                .vendorName(vendorName)
                .vendorNameKana(vendorNameKana)
                .vendorContactName(vendorContactName)
                .vendorDepartmentName(vendorDepartmentName)
                .vendorPostalCode(vendorPostalCode)
                .vendorPrefecture(vendorPrefecture)
                .vendorAddress1(vendorAddress1)
                .vendorAddress2(vendorAddress2)
                .vendorPhoneNumber(vendorPhoneNumber)
                .vendorFaxNumber(vendorFaxNumber)
                .vendorEmailAddress(vendorEmailAddress)
                .vendorClosingDate(vendorClosingDate)
                .vendorPaymentMonth(vendorPaymentMonth)
                .vendorPaymentDate(vendorPaymentDate)
                .vendorPaymentMethod(vendorPaymentMethod)
                .build();

        // Act
        Vendor vendor = VendorResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(vendor);
        assertEquals(vendorCode, vendor.getVendorCode().getCode().getValue());
        assertEquals(vendorBranchNumber, vendor.getVendorCode().getBranchNumber());
        assertEquals(vendorName, vendor.getVendorName().getValue().getName());
        assertEquals(vendorNameKana, vendor.getVendorName().getValue().getNameKana());
        assertEquals(vendorContactName, vendor.getVendorContactName());
        assertEquals(vendorDepartmentName, vendor.getVendorDepartmentName());
        assertEquals(vendorPostalCode.replace("-", ""), vendor.getVendorAddress().getPostalCode().getValue());
        assertEquals(vendorPrefecture, vendor.getVendorAddress().getPrefecture().name());
        assertEquals(vendorAddress1, vendor.getVendorAddress().getAddress1());
        assertEquals(vendorAddress2, vendor.getVendorAddress().getAddress2());
        assertEquals(vendorPhoneNumber, vendor.getVendorPhoneNumber().getValue());
        assertEquals(vendorFaxNumber, vendor.getVendorFaxNumber().getValue());
        assertEquals(vendorEmailAddress, vendor.getVendorEmailAddress().getValue());
        assertEquals(vendorClosingDate.getValue(), vendor.getVendorClosingBilling().getClosingDay().getValue());
        assertEquals(vendorPaymentMonth.getValue(), vendor.getVendorClosingBilling().getPaymentMonth().getValue());
        assertEquals(vendorPaymentDate.getValue(), vendor.getVendorClosingBilling().getPaymentDay().getValue());
        assertEquals(vendorPaymentMethod.getValue(), vendor.getVendorClosingBilling().getPaymentMethod().getValue());
    }

    @Test
    @DisplayName("仕入先検索条件リソースを仕入先検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnVendorCriteria() {
        // Arrange
        String vendorCode = "123";
        String vendorName = "テスト仕入先";
        String vendorContactName = "担当者太郎";
        String vendorDepartmentName = "購買部";
        String postalCode = "123-4567";
        String prefecture = "東京都";
        String address1 = "千代田区";
        String address2 = "1-1-1";
        String vendorPhoneNumber = "03-1234-5678";
        String vendorFaxNumber = "03-8765-4321";
        String vendorEmailAddress = "vendor@example.com";

        VendorCriteriaResource resource = new VendorCriteriaResource();
        resource.setVendorCode(vendorCode);
        resource.setVendorName(vendorName);
        resource.setVendorContactName(vendorContactName);
        resource.setVendorDepartmentName(vendorDepartmentName);
        resource.setPostalCode(postalCode);
        resource.setPrefecture(prefecture);
        resource.setAddress1(address1);
        resource.setAddress2(address2);
        resource.setVendorPhoneNumber(vendorPhoneNumber);
        resource.setVendorFaxNumber(vendorFaxNumber);
        resource.setVendorEmailAddress(vendorEmailAddress);

        // Act
        VendorCriteria criteria = VendorResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(vendorCode, criteria.getVendorCode());
        assertEquals(vendorName, criteria.getVendorName());
        assertEquals(vendorContactName, criteria.getVendorContactName());
        assertEquals(vendorDepartmentName, criteria.getVendorDepartmentName());
        assertEquals(postalCode, criteria.getPostalCode());
        assertEquals(prefecture, criteria.getPrefecture());
        assertEquals(address1, criteria.getAddress1());
        assertEquals(address2, criteria.getAddress2());
        assertEquals(vendorPhoneNumber, criteria.getVendorPhoneNumber());
        assertEquals(vendorFaxNumber, criteria.getVendorFaxNumber());
        assertEquals(vendorEmailAddress, criteria.getVendorEmailAddress());
    }

    @Test
    @DisplayName("仕入先リソースに必須項目がnullの場合は例外をスローする")
    void testConvertToEntity_nullRequiredValues_shouldThrowException() {
        // Arrange
        VendorResource resource = VendorResource.builder()
                .vendorCode(null)
                .vendorBranchNumber(null)
                .build();

        // Act & Assert
        try {
            VendorResourceDTOMapper.convertToEntity(resource);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }
    }
}