package com.example.sms.domain.model.master.payment.account.incoming;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("入金口座")
class PaymentAccountTest {

    @Test
    @DisplayName("入金口座を作成できる")
    void shouldCreatePaymentAccount() {
        String accountCode = "A001";
        String accountName = "テスト口座";
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        String accountNameAfterStart = "テスト口座（開始後）";
        String accountType = "1";
        String accountNumber = "1234567";
        String bankAccountType = "1";
        String accountHolder = "テスト名義人";
        String departmentCode = "10000";
        LocalDateTime departmentStartDate = LocalDateTime.now();
        String bankCode = "0001";
        String branchCode = "001";

        PaymentAccount paymentAccount = PaymentAccount.of(
                accountCode, accountName, startDate, endDate, accountNameAfterStart,
                accountType, accountNumber, bankAccountType, accountHolder,
                departmentCode, departmentStartDate, bankCode, branchCode
        );

        assertNotNull(paymentAccount, "PaymentAccount creation resulted in Null");
        assertEquals(accountCode, paymentAccount.getAccountCode().getValue(), "Mismatch in accountCode");
        assertEquals(accountName, paymentAccount.getAccountName(), "Mismatch in accountName");
        assertEquals(startDate, paymentAccount.getStartDate(), "Mismatch in startDate");
        assertEquals(endDate, paymentAccount.getEndDate(), "Mismatch in endDate");
        assertEquals(accountNameAfterStart, paymentAccount.getAccountNameAfterStart(), "Mismatch in accountNameAfterStart");
        assertEquals(PaymentAccountType.銀行, paymentAccount.getAccountType(), "Mismatch in accountType");
        assertEquals(accountNumber, paymentAccount.getAccountNumber(), "Mismatch in accountNumber");
        assertEquals(BankAccountType.普通, paymentAccount.getBankAccountType(), "Mismatch in bankAccountType");
        assertEquals(accountHolder, paymentAccount.getAccountHolder(), "Mismatch in accountHolder");
        assertEquals(departmentCode, paymentAccount.getDepartmentId().getDeptCode().getValue(), "Mismatch in departmentCode");
        assertEquals(departmentStartDate, paymentAccount.getDepartmentId().getDepartmentStartDate().getValue(), "Mismatch in departmentStartDate");
        assertEquals(bankCode, paymentAccount.getBankCode(), "Mismatch in bankCode");
        assertEquals(branchCode, paymentAccount.getBranchCode(), "Mismatch in branchCode");
    }

    @Nested
    @DisplayName("入金口座コード")
    class PaymentAccountCodeTest {
        @Test
        @DisplayName("入金口座コードは必須")
        void shouldThrowExceptionWhenAccountCodeIsNull() {
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = LocalDateTime.now().plusDays(1);
            LocalDateTime departmentStartDate = LocalDateTime.now();

            assertThrows(IllegalArgumentException.class, () -> PaymentAccount.of(
                    null, "テスト口座", startDate, endDate, "テスト口座（開始後）",
                    "1", "1234567", "1", "テスト名義人",
                    "10000", departmentStartDate, "0001", "001"
            ));
        }

        @Test
        @DisplayName("入金口座コードは空文字列であってはならない")
        void shouldThrowExceptionWhenAccountCodeIsEmpty() {
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = LocalDateTime.now().plusDays(1);
            LocalDateTime departmentStartDate = LocalDateTime.now();

            assertThrows(IllegalArgumentException.class, () -> PaymentAccount.of(
                    "", "テスト口座", startDate, endDate, "テスト口座（開始後）",
                    "1", "1234567", "1", "テスト名義人",
                    "10000", departmentStartDate, "0001", "001"
            ));
        }
    }

    @Nested
    @DisplayName("日付")
    class DateTest {
        @Test
        @DisplayName("終了日は開始日より後である必要がある")
        void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = LocalDateTime.now().minusDays(1);
            LocalDateTime departmentStartDate = LocalDateTime.now();

            assertThrows(RuntimeException.class, () -> PaymentAccount.of(
                    "A001", "テスト口座", startDate, endDate, "テスト口座（開始後）",
                    "1", "1234567", "1", "テスト名義人",
                    "10000", departmentStartDate, "0001", "001"
            ));
        }
    }

    @Test
    @DisplayName("部門IDは必須")
    void shouldThrowExceptionWhenDepartmentIdIsNull() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        assertThrows(RuntimeException.class, () -> PaymentAccount.of(
                "A001", "テスト口座", startDate, endDate, "テスト口座（開始後）",
                "1", "1234567", "1", "テスト名義人",
                null, null, "0001", "001"
        ));
    }

    @Test
    @DisplayName("入金口座区分のデフォルト値は銀行")
    void shouldDefaultToBank() {
        String accountCode = "A001";
        String accountName = "テスト口座";
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        String accountNameAfterStart = "テスト口座（開始後）";
        String accountNumber = "1234567";
        String bankAccountType = "1";
        String accountHolder = "テスト名義人";
        String departmentCode = "10000";
        LocalDateTime departmentStartDate = LocalDateTime.now();
        String bankCode = "0001";
        String branchCode = "001";

        PaymentAccount paymentAccount = PaymentAccount.of(
                accountCode, accountName, startDate, endDate, accountNameAfterStart,
                null, accountNumber, bankAccountType, accountHolder,
                departmentCode, departmentStartDate, bankCode, branchCode
        );

        assertEquals(PaymentAccountType.銀行, paymentAccount.getAccountType(), "Default accountType should be 銀行");
    }

    @Test
    @DisplayName("銀行口座種別のデフォルト値は普通")
    void shouldDefaultToOrdinary() {
        String accountCode = "A001";
        String accountName = "テスト口座";
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        String accountNameAfterStart = "テスト口座（開始後）";
        String accountType = "1";
        String accountNumber = "1234567";
        String accountHolder = "テスト名義人";
        String departmentCode = "10000";
        LocalDateTime departmentStartDate = LocalDateTime.now();
        String bankCode = "0001";
        String branchCode = "001";

        PaymentAccount paymentAccount = PaymentAccount.of(
                accountCode, accountName, startDate, endDate, accountNameAfterStart,
                accountType, accountNumber, null, accountHolder,
                departmentCode, departmentStartDate, bankCode, branchCode
        );

        assertEquals(BankAccountType.普通, paymentAccount.getBankAccountType(), "Default bankAccountType should be 普通");
    }

    @Nested
    @DisplayName("銀行統合における適用開始日")
    class BankIntegrationStartDateTest {
        @Test
        @DisplayName("変更する")
        void shouldChange() {
            String accountCode = "00103411";
            String accountName = "大和渋谷支店";
            LocalDateTime startDate = LocalDateTime.of(2003,3,1,0,0);
            String accountNameAfterStart = "りそな　渋谷西支店";
            String accountType = PaymentAccountType.銀行.getCode();
            String accountNumber = "1234567";
            String bankAccountType = BankAccountType.普通.getCode();
            String accountHolder = "海原商事株式会社";
            String departmentCode = "10000";
            LocalDateTime departmentStartDate = LocalDateTime.now();
            String bankCode = "0010";
            String branchCode = "341";

            PaymentAccount paymentAccount = PaymentAccount.of(
                    accountCode, accountName, startDate, null, accountNameAfterStart,
                    accountType, accountNumber, bankAccountType, accountHolder,
                    departmentCode, departmentStartDate, bankCode, branchCode
            );

            assertNotNull(paymentAccount, "PaymentAccount should not be null");
        }

        @Test
        @DisplayName("廃止する")
        void shouldClose() {
            String accountCode = "00064732";
            String accountName = "あわひ渋谷支店";
            LocalDateTime startDate = LocalDateTime.of(1995,3,14,0,0);
            LocalDateTime endDate = LocalDateTime.of(2003,2,28,0,0);
            String accountNameAfterStart = null;
            String accountType = PaymentAccountType.銀行.getCode();
            String accountNumber = "2345678";
            String bankAccountType = BankAccountType.当座.getCode();
            String accountHolder = "海原商事株式会社";
            String departmentCode = "10102";
            LocalDateTime departmentStartDate = LocalDateTime.now();
            String bankCode = "0006";
            String branchCode = "473";

            PaymentAccount paymentAccount = PaymentAccount.of(
                    accountCode, accountName, startDate, endDate, accountNameAfterStart,
                    accountType, accountNumber, bankAccountType, accountHolder,
                    departmentCode, departmentStartDate, bankCode, branchCode
            );

            assertNotNull(paymentAccount, "PaymentAccount should not be null");
        }

        @Test
        @DisplayName("新規追加する")
        void shouldOpen() {
            String accountCode = "00104731";
            String accountName = "りそな　渋谷支店";
            LocalDateTime startDate = LocalDateTime.of(1995,3,14,0,0);
            LocalDateTime endDate = null;
            String accountNameAfterStart = "りそな　渋谷支店";
            String accountType = PaymentAccountType.銀行.getCode();
            String accountNumber = "3456789";
            String bankAccountType = BankAccountType.普通.getCode();
            String accountHolder = "海原商事株式会社";
            String departmentCode = "10102";
            LocalDateTime departmentStartDate = LocalDateTime.now();
            String bankCode = "0010";
            String branchCode = "473";

            PaymentAccount paymentAccount = PaymentAccount.of(
                    accountCode, accountName, startDate, endDate, accountNameAfterStart,
                    accountType, accountNumber, bankAccountType, accountHolder,
                    departmentCode, departmentStartDate, bankCode, branchCode
            );

            assertNotNull(paymentAccount, "PaymentAccount should not be null");
        }
    }

    @Nested
    @DisplayName("支店統合のデータ設定")
    class BankIntegrationTest {
        @Test
        @DisplayName("廃止する")
        void shouldClose() {
            String accountCode = "00086551";
            String accountName = "UFJ 赤坂見附支店";
            LocalDateTime startDate = null;
            LocalDateTime endDate = LocalDateTime.of(2002,6,16,0,0);
            String accountNameAfterStart = null;
            String accountType = PaymentAccountType.銀行.getCode();
            String accountNumber = "7654321";
            String bankAccountType = BankAccountType.普通.getCode();
            String accountHolder = "海原商事株式会社";
            String departmentCode = "10102";
            LocalDateTime departmentStartDate = LocalDateTime.now();
            String bankCode = "0008";
            String branchCode = "655";

            PaymentAccount paymentAccount = PaymentAccount.of(
                    accountCode, accountName, startDate, endDate, accountNameAfterStart,
                    accountType, accountNumber, bankAccountType, accountHolder,
                    departmentCode, departmentStartDate, bankCode, branchCode
            );

            assertNotNull(paymentAccount, "PaymentAccount should not be null");
        }

        @Test
        @DisplayName("継続する")
        void shouldOpen() {
            String accountCode = "00086091";
            String accountName = "三和　赤坂支店";
            LocalDateTime startDate = LocalDateTime.of(2002,1,15,0,0);
            LocalDateTime endDate = null;
            String accountNameAfterStart = "UFJ　赤坂支店";
            String accountType = PaymentAccountType.銀行.getCode();
            String accountNumber = "6543210";
            String bankAccountType = BankAccountType.普通.getCode();
            String accountHolder = "海原商事株式会社";
            String departmentCode = "10102";
            LocalDateTime departmentStartDate = LocalDateTime.now();
            String bankCode = "0008";
            String branchCode = "609";

            PaymentAccount paymentAccount = PaymentAccount.of(
                    accountCode, accountName, startDate, endDate, accountNameAfterStart,
                    accountType, accountNumber, bankAccountType, accountHolder,
                    departmentCode, departmentStartDate, bankCode, branchCode
            );

            assertNotNull(paymentAccount, "PaymentAccount should not be null");
        }
    }

}
