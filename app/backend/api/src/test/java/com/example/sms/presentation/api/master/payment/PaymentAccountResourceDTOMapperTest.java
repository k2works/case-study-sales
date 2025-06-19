package com.example.sms.presentation.api.master.payment;

import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccount;
import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccountCode;
import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccountType;
import com.example.sms.domain.model.master.payment.account.incoming.BankAccountType;
import com.example.sms.domain.model.master.department.DepartmentId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("入金口座DTOマッパーテスト")
class PaymentAccountResourceDTOMapperTest {

    @Test
    @DisplayName("入金口座リソースを入金口座エンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnPaymentAccount() {
        // Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        String accountCode = "ACC001";
        String accountName = "テスト口座";
        String startDate = "2023-01-01T10:00:00Z";
        String endDate = "2023-12-31T10:00:00Z";
        String accountNameAfterStart = "テスト口座（適用後）";
        String accountType = "1"; // 普通
        String accountNumber = "1234567";
        String bankAccountType = "1"; // 普通
        String accountHolder = "テスト太郎";
        String departmentCode = "10000";
        String departmentStartDate = "2023-01-01T00:00:00Z";
        String bankCode = "0001";
        String branchCode = "001";

        PaymentAccountResource resource = PaymentAccountResource.builder()
                .accountCode(accountCode)
                .accountName(accountName)
                .startDate(startDate)
                .endDate(endDate)
                .accountNameAfterStart(accountNameAfterStart)
                .accountType(accountType)
                .accountNumber(accountNumber)
                .bankAccountType(bankAccountType)
                .accountHolder(accountHolder)
                .departmentCode(departmentCode)
                .departmentStartDate(departmentStartDate)
                .bankCode(bankCode)
                .branchCode(branchCode)
                .build();

        // Act
        PaymentAccount paymentAccount = PaymentAccountResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(paymentAccount);
        assertEquals(PaymentAccountCode.of(accountCode), paymentAccount.getAccountCode());
        assertEquals(accountName, paymentAccount.getAccountName());
        assertEquals(LocalDateTime.parse(startDate, formatter), paymentAccount.getStartDate());
        assertEquals(LocalDateTime.parse(endDate, formatter), paymentAccount.getEndDate());
        assertEquals(accountNameAfterStart, paymentAccount.getAccountNameAfterStart());
        assertEquals(PaymentAccountType.fromCode(accountType), paymentAccount.getAccountType());
        assertEquals(accountNumber, paymentAccount.getAccountNumber());
        assertEquals(BankAccountType.fromCode(bankAccountType), paymentAccount.getBankAccountType());
        assertEquals(accountHolder, paymentAccount.getAccountHolder());
        assertEquals(DepartmentId.of(departmentCode, LocalDateTime.parse(departmentStartDate, formatter)), paymentAccount.getDepartmentId());
        assertEquals(bankCode, paymentAccount.getBankCode());
        assertEquals(branchCode, paymentAccount.getBranchCode());
    }

    @Test
    @DisplayName("入金口座リソースにnull値がある場合は例外をスローする")
    void testConvertToEntity_nullValuesInResource_shouldThrowException() {
        // Arrange
        PaymentAccountResource resource = PaymentAccountResource.builder()
                .accountCode(null)
                .accountName(null)
                .startDate(null)
                .endDate(null)
                .build();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            PaymentAccountResourceDTOMapper.convertToEntity(resource);
        });
    }

    @Test
    @DisplayName("入金口座リソースの日付フォーマットが無効な場合は例外をスローする")
    void testConvertToEntity_invalidDateFormat_shouldThrowException() {
        // Arrange
        String invalidDate = "2023/01/01 10:00:00";
        PaymentAccountResource resource = PaymentAccountResource.builder()
                .accountCode("ACC001")
                .accountName("テスト口座")
                .startDate(invalidDate)
                .endDate("2023-12-31T10:00:00Z")
                .build();

        // Act & Assert
        assertThrows(Exception.class, () -> {
            PaymentAccountResourceDTOMapper.convertToEntity(resource);
        });
    }
}