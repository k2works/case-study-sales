package com.example.sms.service.procurement.payment;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.procurement.payment.PurchasePayment;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("支払サービス")
class PurchasePaymentServiceTest {
    @Autowired
    private PurchasePaymentService purchasePaymentService;

    @Autowired
    private TestDataFactory testDataFactory;

    @Nested
    @DisplayName("支払")
    class PaymentTest {
        @BeforeEach
        void setUp() {
            testDataFactory.setUpForPurchasePaymentService();
        }

        @Test
        @DisplayName("支払一覧を取得できる")
        void shouldRetrieveAllPayments() {
            List<PurchasePayment> result = purchasePaymentService.selectAll();
            assertNotNull(result);
            assertTrue(result.size() >= 0);
        }

        @Test
        @DisplayName("支払一覧をページングで取得できる")
        void shouldRetrieveAllPaymentsWithPaging() {
            PageInfo<PurchasePayment> result = purchasePaymentService.selectAllWithPageInfo();
            assertNotNull(result);
            assertTrue(result.getList().size() >= 0);
        }

        @Test
        @DisplayName("支払を新規登録できる")
        void shouldRegisterNewPayment() {
            PurchasePayment newPayment = getPayment("PAY0000099");

            purchasePaymentService.register(newPayment);

            assertNotNull(newPayment.getPaymentNumber());
            Optional<PurchasePayment> result = purchasePaymentService.findByPaymentNumber(newPayment.getPaymentNumber().getValue());
            assertTrue(result.isPresent());
            assertEquals(newPayment.getPaymentNumber().getValue(), result.get().getPaymentNumber().getValue());
        }

        @Test
        @DisplayName("支払の登録情報を編集できる")
        void shouldEditPaymentDetails() {
            PurchasePayment payment = getPayment("PAY0000099");
            purchasePaymentService.register(payment);

            PurchasePayment updatedPayment = PurchasePayment.of(
                    payment.getPaymentNumber().getValue(),
                    payment.getPaymentDate().getValue(),
                    payment.getDepartmentCode().getValue(),
                    payment.getDepartmentStartDate(),
                    "003", // 更新された仕入先コード
                    1,
                    payment.getPaymentMethodType().getValue(),
                    200000, // 更新された支払金額
                    20000, // 更新された消費税合計
                    true // 支払完了フラグ
            );

            purchasePaymentService.save(updatedPayment);

            Optional<PurchasePayment> result = purchasePaymentService.findByPaymentNumber(payment.getPaymentNumber().getValue());
            assertTrue(result.isPresent());
            assertEquals("003", result.get().getSupplierCode().getCode().getValue());
            assertEquals(200000, result.get().getPaymentAmount().getAmount());
        }

        @Test
        @DisplayName("支払を削除できる")
        void shouldDeletePayment() {
            PurchasePayment payment = getPayment("PAY0000099");
            purchasePaymentService.register(payment);

            purchasePaymentService.delete(payment.getPaymentNumber().getValue());

            Optional<PurchasePayment> result = purchasePaymentService.findByPaymentNumber(payment.getPaymentNumber().getValue());
            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("条件付きで支払を検索できる (ページング)")
        void shouldSearchPaymentsWithPaging() {
            String supplierCode = "001";
            PurchasePayment payment = getPayment("PAY0000099");
            PurchasePayment searchPayment = PurchasePayment.of(
                    payment.getPaymentNumber().getValue(),
                    payment.getPaymentDate().getValue(),
                    payment.getDepartmentCode().getValue(),
                    payment.getDepartmentStartDate(),
                    supplierCode, // 仕入先コード
                    1,
                    payment.getPaymentMethodType().getValue(),
                    payment.getPaymentAmount().getAmount(),
                    payment.getTotalConsumptionTax().getAmount(),
                    payment.getPaymentCompletedFlag()
            );
            purchasePaymentService.register(searchPayment);

            // 検索条件の設定
            PurchasePaymentCriteria criteria = PurchasePaymentCriteria.builder()
                    .supplierCode(supplierCode) // 仕入先コードを設定
                    .build();

            // 検索結果の呼び出し
            PageInfo<PurchasePayment> result = purchasePaymentService.searchWithPageInfo(criteria);

            // 検索結果のアサーション
            assertNotNull(result);
            assertTrue(result.getList().size() >= 1);
            assertEquals(supplierCode, result.getList().getFirst().getSupplierCode().getCode().getValue());
        }
    }

    /**
     * テスト用の支払データを生成
     */
    private PurchasePayment getPayment(String paymentNumber) {
        return PurchasePayment.of(
                paymentNumber,
                20231001, // paymentDate (YYYYMMDD形式)
                "10000", // departmentCode (5桁の数字)
                LocalDateTime.of(2021, 1, 1, 0, 0), // departmentStartDate
                "001", // supplierCode
                1, // supplierBranchNumber
                1, // paymentMethodType (現金=1)
                100000, // paymentAmount
                10000, // totalConsumptionTax
                false // paymentCompletedFlag
        );
    }
}
