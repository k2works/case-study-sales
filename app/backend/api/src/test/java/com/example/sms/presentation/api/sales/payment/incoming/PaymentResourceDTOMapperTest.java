package com.example.sms.presentation.api.sales.payment.incoming;

import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.sales.payment.incoming.Payment;
import com.example.sms.domain.model.sales.payment.incoming.PaymentMethodType;
import com.example.sms.domain.model.sales.payment.incoming.PaymentNumber;
import com.example.sms.domain.type.money.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("入金データDTOマッパーテスト")
class PaymentResourceDTOMapperTest {

    @Test
    @DisplayName("入金データリソースを入金データエンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnPayment() {
        // Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        String paymentNumber = "PAY001";
        String paymentDate = "2023-01-01T10:00:00Z";
        String departmentCode = "10000";
        String departmentStartDate = "2023-01-01T00:00:00Z";
        String customerCode = "101";
        Integer customerBranchNumber = 1;
        String paymentMethodType = "4"; // 振込
        String paymentAccountCode = "ACC001";
        Integer paymentAmount = 10000;
        Integer offsetAmount = 5000;

        PaymentResource resource = PaymentResource.builder()
                .paymentNumber(paymentNumber)
                .paymentDate(paymentDate)
                .departmentCode(departmentCode)
                .departmentStartDate(departmentStartDate)
                .customerCode(customerCode)
                .customerBranchNumber(customerBranchNumber)
                .paymentMethodType(paymentMethodType)
                .paymentAccountCode(paymentAccountCode)
                .paymentAmount(paymentAmount)
                .offsetAmount(offsetAmount)
                .build();

        // Act
        Payment payment = PaymentResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(payment);
        assertEquals(PaymentNumber.of(paymentNumber), payment.getPaymentNumber());
        assertEquals(LocalDateTime.parse(paymentDate, formatter), payment.getPaymentDate());
        assertEquals(DepartmentId.of(departmentCode, LocalDateTime.parse(departmentStartDate, formatter)), payment.getDepartmentId());
        assertEquals(CustomerCode.of(customerCode, customerBranchNumber), payment.getCustomerCode());
        assertEquals(PaymentMethodType.fromCode(Integer.parseInt(paymentMethodType)), payment.getPaymentMethodType());
        assertEquals(paymentAccountCode, payment.getPaymentAccountCode());
        assertEquals(Money.of(paymentAmount), payment.getPaymentAmount());
        assertEquals(Money.of(offsetAmount), payment.getOffsetAmount());
        assertNull(payment.getCustomer());
        assertNull(payment.getPaymentAccount());
    }

    @Test
    @DisplayName("入金データリソースにnull値がある場合は例外をスローする")
    void testConvertToEntity_nullValuesInResource_shouldThrowException() {
        // Arrange
        PaymentResource resource = PaymentResource.builder()
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
            PaymentResourceDTOMapper.convertToEntity(resource);
        });
    }

    @Test
    @DisplayName("入金データリソースの日付フォーマットが無効な場合は例外をスローする")
    void testConvertToEntity_invalidDateFormat_shouldThrowException() {
        // Arrange
        String invalidDate = "2023/01/01 10:00:00";
        PaymentResource resource = PaymentResource.builder()
                .paymentNumber("PAY001")
                .paymentDate(invalidDate)
                .departmentCode("10000")
                .departmentStartDate("2023-01-01T00:00:00Z")
                .customerCode("101")
                .customerBranchNumber(1)
                .paymentMethodType("4")
                .paymentAccountCode("ACC001")
                .paymentAmount(10000)
                .offsetAmount(5000)
                .build();

        // Act & Assert
        assertThrows(Exception.class, () -> {
            PaymentResourceDTOMapper.convertToEntity(resource);
        });
    }

    @Test
    @DisplayName("入金データリソースの支払方法区分が無効な場合は例外をスローする")
    void testConvertToEntity_invalidPaymentMethodType_shouldThrowException() {
        // Arrange
        String invalidPaymentMethodType = "invalid";
        PaymentResource resource = PaymentResource.builder()
                .paymentNumber("PAY001")
                .paymentDate("2023-01-01T10:00:00Z")
                .departmentCode("10000")
                .departmentStartDate("2023-01-01T00:00:00Z")
                .customerCode("101")
                .customerBranchNumber(1)
                .paymentMethodType(invalidPaymentMethodType)
                .paymentAccountCode("ACC001")
                .paymentAmount(10000)
                .offsetAmount(5000)
                .build();

        // Act & Assert
        assertThrows(NumberFormatException.class, () -> {
            PaymentResourceDTOMapper.convertToEntity(resource);
        });
    }
}