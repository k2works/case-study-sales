package com.example.sms.service.sales.payment.incoming;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceDate;
import com.example.sms.domain.model.sales.invoice.InvoiceLine;
import com.example.sms.domain.model.sales.payment.incoming.Payment;
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
class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        invoiceRepository.deleteAll();
        testDataFactory.setUpForSalesService();
    }

    @Nested
    @DisplayName("入金データ")
    class PaymentTest {
        @Test
        @DisplayName("入金データ一覧を取得できる")
        void shouldGetPaymentList() {
            // Given
            paymentService.save(getPayment("PAY001"));
            paymentService.save(getPayment("PAY002"));
            
            // When
            List<Payment> result = paymentService.selectAll();
            
            // Then
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("入金データを新規登録できる")
        void shouldRegisterPayment() {
            // Given
            Payment payment = getPayment("PAY003");
            
            // When
            paymentService.register(payment);
            
            // Then
            Optional<Payment> result = paymentService.findById("PAY003");
            assertTrue(result.isPresent());
            assertEquals(payment.getPaymentNumber().getValue(), result.get().getPaymentNumber().getValue());
        }

        @Test
        @DisplayName("入金データを編集できる")
        void shouldSavePayment() {
            // Given
            Payment payment = getPayment("PAY004");
            Payment updatedPayment = payment.toBuilder()
                    .paymentAmount(Money.of(20000))
                    .build();
            
            // When
            paymentService.save(payment);
            paymentService.save(updatedPayment);
            
            // Then
            Optional<Payment> result = paymentService.findById("PAY004");
            assertTrue(result.isPresent());
            assertNotEquals(payment.getPaymentAmount().getAmount(), result.get().getPaymentAmount().getAmount());
            assertEquals(updatedPayment.getPaymentAmount().getAmount(), result.get().getPaymentAmount().getAmount());
        }

        @Test
        @DisplayName("入金データを削除できる")
        void shouldDeletePayment() {
            // Given
            Payment payment = getPayment("PAY005");
            paymentService.save(payment);
            
            // When
            paymentService.delete(payment);
            
            // Then
            Optional<Payment> result = paymentService.findById("PAY005");
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("ページング情報付きで入金データ一覧を取得できる")
        void shouldGetPaymentListWithPageInfo() {
            // Given
            paymentService.save(getPayment("PAY006"));
            paymentService.save(getPayment("PAY007"));
            
            // When
            PageInfo<Payment> result = paymentService.selectAllWithPageInfo();
            
            // Then
            assertEquals(2, result.getList().size());
            assertEquals(2, result.getTotal());
        }

        @Test
        @DisplayName("顧客コードで入金データを検索できる")
        void shouldFindByCustomer() {
            // Given
            Payment payment1 = getPayment("PAY008");
            Payment payment2 = getPayment("PAY009");
            Payment payment3 = getPaymentWithCustomer("PAY010", "002", 1);
            paymentService.save(payment1);
            paymentService.save(payment2);
            paymentService.save(payment3);
            
            // When
            List<Payment> result = paymentService.findByCustomer("002", 1);
            
            // Then
            assertEquals(1, result.size());
            assertEquals("PAY010", result.get(0).getPaymentNumber().getValue());
        }

        @Test
        @DisplayName("入金口座コードで入金データを検索できる")
        void shouldFindByAccount() {
            // Given
            Payment payment1 = getPayment("PAY011");
            Payment payment2 = getPayment("PAY012");
            Payment payment3 = getPaymentWithAccount("PAY013", "ACC002");
            paymentService.save(payment1);
            paymentService.save(payment2);
            paymentService.save(payment3);
            
            // When
            List<Payment> result = paymentService.findByAccount("ACC002");
            
            // Then
            assertEquals(1, result.size());
            assertEquals("PAY013", result.get(0).getPaymentNumber().getValue());
        }
    }

    private Payment getPayment(String paymentNumber) {
        return TestDataFactoryImpl.getPaymentData(paymentNumber);
    }

    private Invoice createInvoice(String invoiceNumber, Payment payment) {
        InvoiceLine invoiceLine = TestDataFactoryImpl.getInvoiceLine(invoiceNumber, "SA00000001", 1);
        Invoice invoice = TestDataFactoryImpl.getInvoice(invoiceNumber).toBuilder().invoiceLines(List.of(invoiceLine)).build();
        invoiceRepository.save(invoice.toBuilder().customerCode(payment.getCustomerCode()).currentMonthPaymentAmount(Money.of(0)).build());
        return invoice;
    }

    private Payment getPaymentWithCustomer(String paymentNumber, String customerCode, Integer branchNumber) {
        return TestDataFactoryImpl.getPaymentWithCustomer(paymentNumber, customerCode, branchNumber);
    }

    private Payment getPaymentWithAccount(String paymentNumber, String accountCode) {
        return TestDataFactoryImpl.getPaymentWithAccount(paymentNumber, accountCode);
    }

    @Nested
    @DisplayName("売掛金消込")
    class PaymentApplication {
        @Test
        @DisplayName("売掛金消込を登録できる")
        void shouldRegisterPaymentApplication() {
            // Given
            Payment payment = getPayment("PAY014").toBuilder().paymentAmount(Money.of(10000)).build();
            Invoice preInvoice = createInvoice("IV00000001", payment).toBuilder().invoiceDate(InvoiceDate.of(LocalDateTime.now())).build();
            paymentService.save(payment);

            // When
            paymentService.registerPaymentApplication(payment);

            // Then
            Optional<Payment> result = paymentService.findById("PAY014");
            Optional<Invoice> afterInvoice = invoiceRepository.findById(preInvoice.getInvoiceNumber().getValue());
            assertTrue(result.isPresent());
            assertEquals(afterInvoice.get().getCurrentMonthPaymentAmount().getAmount(), result.get().getPaymentAmount().getAmount());
            assertEquals(afterInvoice.get().getInvoiceReconciliationAmount().getAmount(), result.get().getPaymentAmount().getAmount());
            assertEquals(afterInvoice.get().getInvoiceReconciliationAmount(), result.get().getOffsetAmount());
        }
    }
}