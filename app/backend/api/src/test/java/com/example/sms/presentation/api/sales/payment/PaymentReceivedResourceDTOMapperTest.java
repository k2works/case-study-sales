package com.example.sms.presentation.api.sales.payment;

import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.sales.payment.PaymentReceived;
import com.example.sms.domain.model.sales.payment.PaymentMethodType;
import com.example.sms.domain.model.sales.payment.PaymentReceivedNumber;
import com.example.sms.domain.type.money.Money;
import com.example.sms.service.sales.payment.PaymentReceivedCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("入金データDTOマッパーテスト")
class PaymentReceivedResourceDTOMapperTest {

    @Test
    @DisplayName("入金データリソースを入金データエンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnPaymentReceived() {
        // Arrange
        String paymentReceivedNumber = "PAY001";
        LocalDateTime paymentDate = LocalDateTime.of(2023,1,1,0,0);
        String departmentCode = "10000";
        LocalDateTime departmentStartDate = LocalDateTime.of(2023,1,1,0,0);
        String customerCode = "101";
        Integer customerBranchNumber = 1;
        String paymentMethodType = "4"; // 振込
        String paymentAccountCode = "ACC001";
        Integer paymentAmount = 10000;
        Integer offsetAmount = 5000;

        PaymentReceivedResource resource = PaymentReceivedResource.builder()
                .paymentNumber(paymentReceivedNumber)
                .paymentDate(paymentDate)
                .departmentCode(departmentCode)
                .departmentStartDate(departmentStartDate)
                .customerCode(customerCode)
                .customerBranchNumber(customerBranchNumber)
                .paymentMethodType(PaymentMethodType.振込)
                .paymentAccountCode(paymentAccountCode)
                .paymentAmount(paymentAmount)
                .offsetAmount(offsetAmount)
                .build();

        // Act
        PaymentReceived paymentReceived = PaymentReceivedResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(paymentReceived);
        assertEquals(PaymentReceivedNumber.of(paymentReceivedNumber), paymentReceived.getPaymentNumber());
        assertEquals(paymentDate, paymentReceived.getPaymentDate());
        assertEquals(DepartmentId.of(departmentCode, departmentStartDate), paymentReceived.getDepartmentId());
        assertEquals(CustomerCode.of(customerCode, customerBranchNumber), paymentReceived.getCustomerCode());
        assertEquals(PaymentMethodType.fromCode(Integer.parseInt(paymentMethodType)), paymentReceived.getPaymentMethodType());
        assertEquals(paymentAccountCode, paymentReceived.getPaymentAccountCode());
        assertEquals(Money.of(paymentAmount), paymentReceived.getPaymentAmount());
        assertEquals(Money.of(offsetAmount), paymentReceived.getOffsetAmount());
        assertNull(paymentReceived.getCustomer());
        assertNull(paymentReceived.getPaymentAccount());
    }

    @Test
    @DisplayName("入金データリソースにnull値がある場合は例外をスローする")
    void testConvertToEntity_nullValuesInResource_shouldThrowException() {
        // Arrange
        PaymentReceivedResource resource = PaymentReceivedResource.builder()
                .paymentNumber(null)
                .paymentDate(null)
                .departmentCode(null)
                .departmentStartDate(null)
                .customerCode(null)
                .customerBranchNumber(null)
                .paymentMethodType(null)
                .paymentAccountCode(null)
                .paymentAmount(null)
                .offsetAmount(null)
                .build();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            PaymentReceivedResourceDTOMapper.convertToEntity(resource);
        });
    }

    @Test
    @DisplayName("入金データリソースの支払方法区分が無効な場合は例外をスローする")
    void testConvertToEntity_invalidPaymentMethodType_shouldThrowException() {
        // Arrange
        PaymentReceivedResource resource = PaymentReceivedResource.builder()
                .paymentNumber("PAY001")
                .paymentDate(LocalDateTime.of(2023,1,1,0,0))
                .departmentCode("10000")
                .departmentStartDate(LocalDateTime.of(2023,1,1,0,0))
                .customerCode("101")
                .customerBranchNumber(1)
                .paymentMethodType(null)
                .paymentAccountCode("ACC001")
                .paymentAmount(10000)
                .offsetAmount(5000)
                .build();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            PaymentReceivedResourceDTOMapper.convertToEntity(resource);
        });
    }

    @Test
    @DisplayName("入金検索条件リソースを入金検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnPaymentReceivedCriteria() {
        // Arrange
        String paymentReceivedNumber = "PAY001";
        LocalDateTime paymentDate = LocalDateTime.of(2023,1,1,0,0);
        String departmentCode = "10000";
        String customerCode = "101";
        PaymentMethodType paymentMethodType = PaymentMethodType.振込;
        String paymentAccountCode = "ACC001";

        PaymentReceivedCriteriaResource resource = new PaymentReceivedCriteriaResource();
        resource.setPaymentNumber(paymentReceivedNumber);
        resource.setPaymentDate(paymentDate);
        resource.setDepartmentCode(departmentCode);
        resource.setCustomerCode(customerCode);
        resource.setPaymentMethodType(paymentMethodType);
        resource.setPaymentAccountCode(paymentAccountCode);

        // Act
        PaymentReceivedCriteria criteria = PaymentReceivedResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(paymentReceivedNumber, criteria.getPaymentNumber());
        assertEquals(paymentDate, criteria.getPaymentDate());
        assertEquals(departmentCode, criteria.getDepartmentCode());
        assertEquals(customerCode, criteria.getCustomerCode());
        assertEquals(paymentMethodType.getCode(), criteria.getPaymentMethodType());
        assertEquals(paymentAccountCode, criteria.getPaymentAccountCode());
    }

    @Test
    @DisplayName("入金検索条件リソースがnullの場合はnullを返す")
    void testConvertToCriteria_nullResource_shouldReturnNull() {
        // Arrange
        PaymentReceivedCriteriaResource resource = null;

        // Act
        PaymentReceivedCriteria criteria = PaymentReceivedResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNull(criteria);
    }

    @Test
    @DisplayName("入金検索条件リソースのフィールドがnullの場合も正常に変換する")
    void testConvertToCriteria_nullFieldsInResource_shouldHandleNullFields() {
        // Arrange
        PaymentReceivedCriteriaResource resource = new PaymentReceivedCriteriaResource();
        // すべてのフィールドがnull

        // Act
        PaymentReceivedCriteria criteria = PaymentReceivedResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertNull(criteria.getPaymentNumber());
        assertNull(criteria.getPaymentDate());
        assertNull(criteria.getDepartmentCode());
        assertNull(criteria.getCustomerCode());
        assertNull(criteria.getPaymentMethodType());
        assertNull(criteria.getPaymentAccountCode());
    }
}
