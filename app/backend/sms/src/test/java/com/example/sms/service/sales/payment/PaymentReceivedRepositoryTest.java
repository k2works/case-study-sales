package com.example.sms.service.sales.payment;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales.payment.PaymentReceived;
import com.example.sms.domain.model.sales.payment.PaymentReceivedNumber;
import com.example.sms.domain.type.money.Money;
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
@DisplayName("入金リポジトリ")
class PaymentReceivedRepositoryTest {
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
    private PaymentReceivedRepository paymentReceivedRepository;

    @BeforeEach
    void setUp() {
        paymentReceivedRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        paymentReceivedRepository.deleteAll();
    }

    @Test
    @DisplayName("入金データを保存して取得できること")
    void testSaveAndFindPaymentReceivedData() {
        // Given
        PaymentReceived paymentReceived = getPaymentReceivedData("TEST001");

        // When
        paymentReceivedRepository.save(paymentReceived);
        Optional<PaymentReceived> found = paymentReceivedRepository.findById("TEST001");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getPaymentNumber().getValue()).isEqualTo("TEST001");
        assertThat(found.get().getPaymentAmount().getAmount()).isEqualTo(10000);
    }

    @Test
    @DisplayName("入金データを更新できること")
    void testUpdatePaymentReceivedData() {
        // Given
        PaymentReceived paymentReceived = getPaymentReceivedData("TEST002");
        paymentReceivedRepository.save(paymentReceived);

        // When
        PaymentReceived updatedData = PaymentReceived.builder()
                .paymentNumber(PaymentReceivedNumber.of("TEST002"))
                .paymentDate(LocalDateTime.now())
                .departmentId(paymentReceived.getDepartmentId())
                .customerCode(paymentReceived.getCustomerCode())
                .paymentMethodType(paymentReceived.getPaymentMethodType())
                .paymentAccountCode(paymentReceived.getPaymentAccountCode())
                .paymentAmount(paymentReceived.getPaymentAmount())
                .offsetAmount(paymentReceived.getOffsetAmount().plusMoney(Money.of(5000))) // 消込金額を更新
                .build();
        paymentReceivedRepository.save(updatedData);

        // Then
        Optional<PaymentReceived> found = paymentReceivedRepository.findById("TEST002");
        assertThat(found).isPresent();
        assertThat(found.get().getOffsetAmount().getAmount()).isEqualTo(5000);
    }

    @Test
    @DisplayName("入金データを削除できること")
    void testDeletePaymentReceivedData() {
        // Given
        PaymentReceived paymentReceived = getPaymentReceivedData("TEST003");
        paymentReceivedRepository.save(paymentReceived);

        // When
        paymentReceivedRepository.delete(paymentReceived);

        // Then
        Optional<PaymentReceived> found = paymentReceivedRepository.findById("TEST003");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("全ての入金データを取得できること")
    void testSelectAllPaymentReceivedData() {
        // Given
        paymentReceivedRepository.save(getPaymentReceivedData("TEST004"));
        paymentReceivedRepository.save(getPaymentReceivedData("TEST005"));

        // When
        List<PaymentReceived> allPaymentReceiveds = paymentReceivedRepository.selectAll().asList();

        // Then
        assertThat(allPaymentReceiveds).hasSize(2);
    }


    private PaymentReceived getPaymentReceivedData(String paymentReceivedNumber) {
        return TestDataFactoryImpl.getPaymentData(paymentReceivedNumber);
    }
}
