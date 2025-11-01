package com.example.sms.presentation.api.procurement.payment;

import com.example.sms.domain.model.procurement.payment.PurchasePayment;
import com.example.sms.service.procurement.payment.PurchasePaymentCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("支払リソースDTOマッパーテスト")
class PurchasePaymentResourceDTOMapperTest {

    @Test
    @DisplayName("PurchasePaymentResourceからPurchasePaymentに変換できる")
    void shouldConvertResourceToEntity() {
        // Arrange
        LocalDateTime departmentStartDate = LocalDateTime.of(2021, 1, 1, 0, 0);
        PurchasePaymentResource resource = PurchasePaymentResource.builder()
                .paymentNumber("PAY0000001")
                .paymentDate(20250101)
                .departmentCode("10000")
                .departmentStartDate(departmentStartDate)
                .supplierCode("001")
                .supplierBranchNumber(1)
                .paymentMethodType(1)
                .paymentAmount(100000)
                .totalConsumptionTax(10000)
                .paymentCompletedFlag(false)
                .build();

        // Act
        PurchasePayment result = PurchasePaymentResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getPaymentNumber());
        assertEquals("PAY0000001", result.getPaymentNumber().getValue());
        assertEquals(20250101, result.getPaymentDate().getValue());
        assertEquals("10000", result.getDepartmentCode().getValue());
        assertEquals(departmentStartDate, result.getDepartmentStartDate());
        assertEquals("001", result.getSupplierCode().getCode().getValue());
        assertEquals(1, result.getSupplierCode().getBranchNumber());
        assertEquals(1, result.getPaymentMethodType().getCode());
        assertEquals(100000, result.getPaymentAmount().getAmount());
        assertEquals(10000, result.getTotalConsumptionTax().getAmount());
        assertEquals(false, result.getPaymentCompletedFlag());
    }

    @Test
    @DisplayName("PurchasePaymentCriteriaResourceからPurchasePaymentCriteriaに変換できる")
    void shouldConvertCriteriaResourceToCriteria() {
        // Arrange
        PurchasePaymentCriteriaResource resource = new PurchasePaymentCriteriaResource();
        resource.setPaymentNumber("PAY0000001");
        resource.setPaymentDate(20250101);
        resource.setDepartmentCode("10000");
        resource.setSupplierCode("001");
        resource.setPaymentMethodType(1);
        resource.setPaymentCompletedFlag(true);

        // Act
        PurchasePaymentCriteria result = PurchasePaymentResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(result);
        assertEquals("PAY0000001", result.getPaymentNumber());
        assertEquals(20250101, result.getPaymentDate());
        assertEquals("10000", result.getDepartmentCode());
        assertEquals("001", result.getSupplierCode());
        assertEquals(1, result.getPaymentMethodType());
        assertEquals(1, result.getPaymentCompletedFlag());
    }

    @Test
    @DisplayName("nullのリソースを渡してもnullが返る")
    void shouldReturnNullWhenCriteriaResourceIsNull() {
        // Act
        PurchasePaymentCriteria result = PurchasePaymentResourceDTOMapper.convertToCriteria(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("null値を含む検索条件リソースでも変換できる")
    void shouldConvertCriteriaResourceWithNullValues() {
        // Arrange
        PurchasePaymentCriteriaResource resource = new PurchasePaymentCriteriaResource();
        resource.setPaymentNumber("PAY0000001");
        // 他のフィールドはnull

        // Act
        PurchasePaymentCriteria result = PurchasePaymentResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(result);
        assertEquals("PAY0000001", result.getPaymentNumber());
        assertNull(result.getPaymentDate());
        assertNull(result.getDepartmentCode());
        assertNull(result.getSupplierCode());
        assertNull(result.getPaymentMethodType());
        assertNull(result.getPaymentCompletedFlag());
    }

    @Test
    @DisplayName("支払完了フラグがfalseの場合は0に変換される")
    void shouldConvertPaymentCompletedFlagToZeroWhenFalse() {
        // Arrange
        PurchasePaymentCriteriaResource resource = new PurchasePaymentCriteriaResource();
        resource.setPaymentNumber("PAY0000001");
        resource.setPaymentCompletedFlag(false);

        // Act
        PurchasePaymentCriteria result = PurchasePaymentResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getPaymentCompletedFlag());
    }

    @Test
    @DisplayName("支払完了フラグがtrueの場合は1に変換される")
    void shouldConvertPaymentCompletedFlagToOneWhenTrue() {
        // Arrange
        PurchasePaymentCriteriaResource resource = new PurchasePaymentCriteriaResource();
        resource.setPaymentNumber("PAY0000001");
        resource.setPaymentCompletedFlag(true);

        // Act
        PurchasePaymentCriteria result = PurchasePaymentResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPaymentCompletedFlag());
    }

    @Test
    @DisplayName("支払完了フラグがnullの場合はnullのまま")
    void shouldKeepPaymentCompletedFlagNullWhenNull() {
        // Arrange
        PurchasePaymentCriteriaResource resource = new PurchasePaymentCriteriaResource();
        resource.setPaymentNumber("PAY0000001");
        resource.setPaymentCompletedFlag(null);

        // Act
        PurchasePaymentCriteria result = PurchasePaymentResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(result);
        assertNull(result.getPaymentCompletedFlag());
    }
}
