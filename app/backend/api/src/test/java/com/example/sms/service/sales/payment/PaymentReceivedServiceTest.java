package com.example.sms.service.sales.payment;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceDate;
import com.example.sms.domain.model.sales.invoice.InvoiceLine;
import com.example.sms.domain.model.sales.payment.PaymentReceived;
import com.example.sms.domain.type.money.Money;
import com.example.sms.service.sales.invoice.InvoiceRepository;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
@DisplayName("入金データサービス")
class PaymentReceivedServiceTest {
    @Autowired
    private PaymentReceivedService paymentReceivedService;

    @Autowired
    private PaymentReceivedRepository paymentReceivedRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        paymentReceivedRepository.deleteAll();
        invoiceRepository.deleteAll();
        testDataFactory.setUpForSalesService();
    }

    @Nested
    @DisplayName("入金データ")
    class PaymentReceivedTest {
        @Test
        @DisplayName("入金データ一覧を取得できる")
        void shouldGetPaymentReceivedList() {
            // Given
            paymentReceivedService.save(getPaymentReceived("PAY001"));
            paymentReceivedService.save(getPaymentReceived("PAY002"));

            // When
            List<PaymentReceived> result = paymentReceivedService.selectAll();

            // Then
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("入金データを新規登録できる")
        void shouldRegisterPaymentReceived() {
            // Given
            PaymentReceived paymentReceived = getPaymentReceived("PAY003");

            // When
            paymentReceivedService.register(paymentReceived);

            // Then
            Optional<PaymentReceived> result = paymentReceivedService.findById("PAY003");
            assertTrue(result.isPresent());
            assertEquals(paymentReceived.getPaymentNumber().getValue(), result.get().getPaymentNumber().getValue());
        }

        @Test
        @DisplayName("入金データを編集できる")
        void shouldSavePaymentReceived() {
            // Given
            PaymentReceived paymentReceived = getPaymentReceived("PAY004");
            PaymentReceived updatedPaymentReceived = paymentReceived.toBuilder()
                    .paymentAmount(Money.of(20000))
                    .build();

            // When
            paymentReceivedService.save(paymentReceived);
            paymentReceivedService.save(updatedPaymentReceived);

            // Then
            Optional<PaymentReceived> result = paymentReceivedService.findById("PAY004");
            assertTrue(result.isPresent());
            assertNotEquals(paymentReceived.getPaymentAmount().getAmount(), result.get().getPaymentAmount().getAmount());
            assertEquals(updatedPaymentReceived.getPaymentAmount().getAmount(), result.get().getPaymentAmount().getAmount());
        }

        @Test
        @DisplayName("入金データを削除できる")
        void shouldDeletePaymentReceived() {
            // Given
            PaymentReceived paymentReceived = getPaymentReceived("PAY005");
            paymentReceivedService.save(paymentReceived);

            // When
            paymentReceivedService.delete(paymentReceived);

            // Then
            Optional<PaymentReceived> result = paymentReceivedService.findById("PAY005");
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("ページング情報付きで入金データ一覧を取得できる")
        void shouldGetPaymentReceivedListWithPageInfo() {
            // Given
            paymentReceivedService.save(getPaymentReceived("PAY006"));
            paymentReceivedService.save(getPaymentReceived("PAY007"));

            // When
            PageInfo<PaymentReceived> result = paymentReceivedService.selectAllWithPageInfo();

            // Then
            assertEquals(2, result.getList().size());
            assertEquals(2, result.getTotal());
        }

        @Test
        @DisplayName("顧客コードで入金データを検索できる")
        void shouldFindByCustomer() {
            // Given
            PaymentReceived paymentReceived1 = getPaymentReceived("PAY008");
            PaymentReceived paymentReceived2 = getPaymentReceived("PAY009");
            PaymentReceived paymentReceived3 = getPaymentReceivedWithCustomer("PAY010", "002", 1);
            paymentReceivedService.save(paymentReceived1);
            paymentReceivedService.save(paymentReceived2);
            paymentReceivedService.save(paymentReceived3);

            // When
            List<PaymentReceived> result = paymentReceivedService.findByCustomer("002", 1);

            // Then
            assertEquals(1, result.size());
            assertEquals("PAY010", result.get(0).getPaymentNumber().getValue());
        }

        @Test
        @DisplayName("入金口座コードで入金データを検索できる")
        void shouldFindByAccount() {
            // Given
            PaymentReceived paymentReceived1 = getPaymentReceived("PAY011");
            PaymentReceived paymentReceived2 = getPaymentReceived("PAY012");
            PaymentReceived paymentReceived3 = getPaymentReceivedWithAccount("PAY013", "ACC002");
            paymentReceivedService.save(paymentReceived1);
            paymentReceivedService.save(paymentReceived2);
            paymentReceivedService.save(paymentReceived3);

            // When
            List<PaymentReceived> result = paymentReceivedService.findByAccount("ACC002");

            // Then
            assertEquals(1, result.size());
            assertEquals("PAY013", result.get(0).getPaymentNumber().getValue());
        }
    }

    private PaymentReceived getPaymentReceived(String paymentReceivedNumber) {
        return TestDataFactoryImpl.getPaymentData(paymentReceivedNumber);
    }

    private Invoice createInvoice(String invoiceNumber, PaymentReceived paymentReceived) {
        InvoiceLine invoiceLine = TestDataFactoryImpl.getInvoiceLine(invoiceNumber, "SA00000001", 1);
        Invoice invoice = TestDataFactoryImpl.getInvoice(invoiceNumber).toBuilder().invoiceLines(List.of(invoiceLine)).build();
        invoiceRepository.save(invoice.toBuilder().customerCode(paymentReceived.getCustomerCode()).currentMonthPaymentAmount(Money.of(0)).build());
        return invoice;
    }

    private PaymentReceived getPaymentReceivedWithCustomer(String paymentReceivedNumber, String customerCode, Integer branchNumber) {
        return TestDataFactoryImpl.getPaymentWithCustomer(paymentReceivedNumber, customerCode, branchNumber);
    }

    private PaymentReceived getPaymentReceivedWithAccount(String paymentReceivedNumber, String accountCode) {
        return TestDataFactoryImpl.getPaymentWithAccount(paymentReceivedNumber, accountCode);
    }

    @Nested
    @DisplayName("売掛金消込")
    class PaymentReceivedApplication {
        @Test
        @DisplayName("売掛金消込を登録できる")
        void shouldRegisterPaymentReceivedApplication() {
            // Given
            PaymentReceived paymentReceived = getPaymentReceived("PAY014").toBuilder().paymentAmount(Money.of(10000)).build();
            Invoice preInvoice = createInvoice("IV00000001", paymentReceived).toBuilder().invoiceDate(InvoiceDate.of(LocalDateTime.now())).build();
            paymentReceivedService.save(paymentReceived);

            // When
            paymentReceivedService.registerPaymentReceivedApplication(paymentReceived);

            // Then
            Optional<PaymentReceived> result = paymentReceivedService.findById("PAY014");
            Optional<Invoice> afterInvoice = invoiceRepository.findById(preInvoice.getInvoiceNumber().getValue());
            assertTrue(result.isPresent());
            assertEquals(afterInvoice.get().getCurrentMonthPaymentAmount().getAmount(), result.get().getPaymentAmount().getAmount());
            assertEquals(afterInvoice.get().getInvoiceReconciliationAmount().getAmount(), result.get().getPaymentAmount().getAmount());
            assertEquals(afterInvoice.get().getInvoiceReconciliationAmount(), result.get().getOffsetAmount());
        }
    }
}
