package com.example.sms.service.sales.incoming;

import com.example.sms.domain.model.sales.payment.incoming.Payment;
import com.example.sms.domain.model.sales.payment.incoming.PaymentNumber;
import com.example.sms.domain.type.money.Money;
import com.example.sms.service.sales.payment.incoming.PaymentRepository;
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
class PaymentRepositoryTest {
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
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        paymentRepository.deleteAll();
    }

    @Test
    @DisplayName("入金データを保存して取得できること")
    void testSaveAndFindPaymentData() {
        // Given
        Payment payment = createTestPaymentData("TEST001");

        // When
        paymentRepository.save(payment);
        Optional<Payment> found = paymentRepository.findById("TEST001");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getPaymentNumber().getValue()).isEqualTo("TEST001");
        assertThat(found.get().getPaymentAmount().getAmount()).isEqualTo(10000);
    }

    @Test
    @DisplayName("入金データを更新できること")
    void testUpdatePaymentData() {
        // Given
        Payment payment = createTestPaymentData("TEST002");
        paymentRepository.save(payment);

        // When
        Payment updatedData = Payment.builder()
                .paymentNumber(PaymentNumber.of("TEST002"))
                .paymentDate(LocalDateTime.now())
                .departmentId(payment.getDepartmentId())
                .customerCode(payment.getCustomerCode())
                .paymentMethodType(payment.getPaymentMethodType())
                .paymentAccountCode(payment.getPaymentAccountCode())
                .paymentAmount(payment.getPaymentAmount())
                .offsetAmount(payment.getOffsetAmount().plusMoney(Money.of(5000))) // 消込金額を更新
                .build();
        paymentRepository.save(updatedData);

        // Then
        Optional<Payment> found = paymentRepository.findById("TEST002");
        assertThat(found).isPresent();
        assertThat(found.get().getOffsetAmount().getAmount()).isEqualTo(5000);
    }

    @Test
    @DisplayName("入金データを削除できること")
    void testDeletePaymentData() {
        // Given
        Payment payment = createTestPaymentData("TEST003");
        paymentRepository.save(payment);

        // When
        paymentRepository.delete(payment);

        // Then
        Optional<Payment> found = paymentRepository.findById("TEST003");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("全ての入金データを取得できること")
    void testSelectAllPaymentData() {
        // Given
        paymentRepository.save(createTestPaymentData("TEST004"));
        paymentRepository.save(createTestPaymentData("TEST005"));

        // When
        List<Payment> allPayments = paymentRepository.selectAll();

        // Then
        assertThat(allPayments).hasSize(2);
    }


    private Payment createTestPaymentData(String paymentNumber) {
        return Payment.of(
                paymentNumber,
                LocalDateTime.now(),
                "10000",
                LocalDateTime.now(),
                "001",
                1,
                4, // 振込
                "ACC001",
                10000,
                0
        );
    }
}