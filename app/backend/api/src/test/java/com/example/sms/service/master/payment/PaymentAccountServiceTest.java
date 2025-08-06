package com.example.sms.service.master.payment;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccount;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
@DisplayName("入金口座マスタサービス")
class PaymentAccountServiceTest {
    @Autowired
    private PaymentAccountService paymentAccountService;
    
    @Autowired
    private PaymentAccountRepository paymentAccountRepository;

    @BeforeEach
    void setUp() {
        paymentAccountRepository.deleteAll();
    }

    @Nested
    @DisplayName("入金口座マスタ")
    class PaymentAccountTest {
        @Test
        @DisplayName("入金口座マスタ一覧を取得できる")
        void shouldGetPaymentAccountList() {
            // Given
            paymentAccountService.save(getPaymentAccount("ACC001"));
            paymentAccountService.save(getPaymentAccount("ACC002"));
            
            // When
            List<PaymentAccount> result = paymentAccountService.selectAll();
            
            // Then
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("入金口座マスタを新規登録できる")
        void shouldRegisterPaymentAccount() {
            // Given
            PaymentAccount paymentAccount = getPaymentAccount("ACC003");
            
            // When
            paymentAccountService.register(paymentAccount);
            
            // Then
            Optional<PaymentAccount> result = paymentAccountService.findById("ACC003");
            assertTrue(result.isPresent());
            assertEquals(paymentAccount, result.get());
        }

        @Test
        @DisplayName("入金口座マスタを編集できる")
        void shouldSavePaymentAccount() {
            // Given
            PaymentAccount paymentAccount = getPaymentAccount("ACC004");
            PaymentAccount updatedPaymentAccount = paymentAccount.toBuilder()
                    .accountName("更新テスト口座")
                    .build();
            
            // When
            paymentAccountService.save(paymentAccount);
            paymentAccountService.save(updatedPaymentAccount);
            
            // Then
            Optional<PaymentAccount> result = paymentAccountService.findById("ACC004");
            assertTrue(result.isPresent());
            assertNotEquals(paymentAccount.getAccountName(), result.get().getAccountName());
            assertEquals(updatedPaymentAccount.getAccountName(), result.get().getAccountName());
        }

        @Test
        @DisplayName("入金口座マスタを削除できる")
        void shouldDeletePaymentAccount() {
            // Given
            PaymentAccount paymentAccount = getPaymentAccount("ACC005");
            paymentAccountService.save(paymentAccount);
            
            // When
            paymentAccountService.delete(paymentAccount);
            
            // Then
            Optional<PaymentAccount> result = paymentAccountService.findById("ACC005");
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("ページング情報付きで入金口座マスタ一覧を取得できる")
        void shouldGetPaymentAccountListWithPageInfo() {
            // Given
            paymentAccountService.save(getPaymentAccount("ACC006"));
            paymentAccountService.save(getPaymentAccount("ACC007"));
            
            // When
            PageInfo<PaymentAccount> result = paymentAccountService.selectAllWithPageInfo();
            
            // Then
            assertEquals(2, result.getList().size());
            assertEquals(2, result.getTotal());
        }
    }

    private PaymentAccount getPaymentAccount(String accountCode) {
        return TestDataFactoryImpl.getPaymentAccount(accountCode);
    }
}