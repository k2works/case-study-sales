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

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("入金データDTOマッパーテスト")
class PaymentResourceDTOMapperTest {

    @Test
    @DisplayName("入金データリソースを入金データエンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnPayment() {
        // Arrange
        String paymentNumber = "PAY001";
        LocalDateTime paymentDate = LocalDateTime.of(2023,1,1,0,0);
        String departmentCode = "10000";
        LocalDateTime departmentStartDate = LocalDateTime.of(2023,1,1,0,0);
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
                .paymentMethodType(PaymentMethodType.振込)
                .paymentAccountCode(paymentAccountCode)
                .paymentAmount(paymentAmount)
                .offsetAmount(offsetAmount)
                .build();

        // Act
        Payment payment = PaymentResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(payment);
        assertEquals(PaymentNumber.of(paymentNumber), payment.getPaymentNumber());
        assertEquals(paymentDate, payment.getPaymentDate());
        assertEquals(DepartmentId.of(departmentCode, departmentStartDate), payment.getDepartmentId());
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
    @DisplayName("入金データリソースの支払方法区分が無効な場合は例外をスローする")
    void testConvertToEntity_invalidPaymentMethodType_shouldThrowException() {
        // Arrange
        PaymentResource resource = PaymentResource.builder()
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
            PaymentResourceDTOMapper.convertToEntity(resource);
        });
    }
}