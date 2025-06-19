package com.example.sms.service.master.payment;

import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccount;
import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccountCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("入金口座リポジトリ")
class PaymentAccountRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                    .withUsername("root")
                    .withPassword("password")
                    .withDatabaseName("postgres");

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }


    @Autowired
    private PaymentAccountRepository paymentAccountRepository;

    @BeforeEach
    void setUp() {
        paymentAccountRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        paymentAccountRepository.deleteAll();
    }

    @Test
    @DisplayName("入金口座マスタを保存して取得できること")
    void testSaveAndFindPaymentAccount() {
        // Given
        PaymentAccount account = createTestPaymentAccount("ACC001");

        // When
        paymentAccountRepository.save(account);
        Optional<PaymentAccount> found = paymentAccountRepository.findById("ACC001");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getAccountCode().getValue()).isEqualTo("ACC001");
        assertThat(found.get().getAccountName()).isEqualTo("テスト口座");
    }

    @Test
    @DisplayName("入金口座マスタを更新できること")
    void testUpdatePaymentAccount() {
        // Given
        PaymentAccount account = createTestPaymentAccount("ACC002");
        paymentAccountRepository.save(account);

        // When
        PaymentAccount updatedAccount = PaymentAccount.builder()
                .accountCode(PaymentAccountCode.of("ACC002"))
                .accountName("更新テスト口座")
                .startDate(account.getStartDate())
                .endDate(account.getEndDate())
                .accountNameAfterStart(account.getAccountNameAfterStart())
                .accountType(account.getAccountType())
                .accountNumber(account.getAccountNumber())
                .bankAccountType(account.getBankAccountType())
                .accountHolder(account.getAccountHolder())
                .departmentId(account.getDepartmentId())
                .bankCode(account.getBankCode())
                .branchCode(account.getBranchCode())
                .build();
        paymentAccountRepository.save(updatedAccount);

        // Then
        Optional<PaymentAccount> found = paymentAccountRepository.findById("ACC002");
        assertThat(found).isPresent();
        assertThat(found.get().getAccountName()).isEqualTo("更新テスト口座");
    }

    @Test
    @DisplayName("入金口座マスタを削除できること")
    void testDeletePaymentAccount() {
        // Given
        PaymentAccount account = createTestPaymentAccount("ACC003");
        paymentAccountRepository.save(account);

        // When
        paymentAccountRepository.delete(account);

        // Then
        Optional<PaymentAccount> found = paymentAccountRepository.findById("ACC003");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("全ての入金口座マスタを取得できること")
    void testSelectAllPaymentAccounts() {
        // Given
        paymentAccountRepository.save(createTestPaymentAccount("ACC004"));
        paymentAccountRepository.save(createTestPaymentAccount("ACC005"));

        // When
        List<PaymentAccount> allAccounts = paymentAccountRepository.selectAll();

        // Then
        assertThat(allAccounts).hasSize(2);
    }

    private PaymentAccount createTestPaymentAccount(String accountCode) {
        return PaymentAccount.of(
                accountCode,
                "テスト口座",
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                "テスト口座（適用後）",
                "1", // 普通
                "1234567",
                "1", // 普通
                "テスト太郎",
                "10000",
                LocalDateTime.now(),
                "0001",
                "001"
        );
    }
}