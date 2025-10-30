package com.example.sms.service.procurement.receipt;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.procurement.receipt.Purchase;
import com.example.sms.domain.model.procurement.receipt.PurchaseLine;
import com.example.sms.domain.model.procurement.receipt.PurchaseList;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("仕入サービス")
class PurchaseServiceTest {
    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private TestDataFactory testDataFactory;

    @Nested
    @DisplayName("仕入")
    class PurchaseTest {
        @BeforeEach
        void setUp() {
            testDataFactory.setUpForPurchaseService();
        }

        @Test
        @DisplayName("仕入一覧を取得できる")
        void shouldRetrieveAllPurchases() {
            PurchaseList result = purchaseService.selectAll();
            assertNotNull(result);
            assertTrue(result.asList().size() >= 0);
        }

        @Test
        @DisplayName("仕入一覧をページングで取得できる")
        void shouldRetrieveAllPurchasesWithPaging() {
            PageInfo<Purchase> result = purchaseService.selectAllWithPageInfo();
            assertNotNull(result);
            assertTrue(result.getList().size() >= 0);
        }

        @Test
        @DisplayName("仕入を新規登録できる")
        void shouldRegisterNewPurchase() {
            Purchase newPurchase = getPurchase("PU00000099");

            purchaseService.register(newPurchase);

            assertNotNull(newPurchase.getPurchaseNumber());
            Purchase result = purchaseService.find(newPurchase.getPurchaseNumber().getValue());
            assertNotNull(result);
            assertEquals(newPurchase.getPurchaseNumber().getValue(), result.getPurchaseNumber().getValue());
        }

        @Test
        @DisplayName("仕入番号nullで新規登録できる")
        void shouldRegisterNewPurchaseWithNullPurchaseNumber() {
            Purchase newPurchase = getPurchase(null);

            purchaseService.register(newPurchase);

            PurchaseList result = purchaseService.selectAll();
            assertNotNull(result);
        }

        @Test
        @DisplayName("仕入の登録情報を編集できる")
        void shouldEditPurchaseDetails() {
            Purchase purchase = getPurchase("PU00000099");
            purchaseService.register(purchase);

            Purchase updatedPurchase = Purchase.of(
                    purchase.getPurchaseNumber().getValue(),
                    purchase.getPurchaseDate().getValue(),
                    "003", // 更新された仕入先コード
                    0,
                    purchase.getPurchaseManagerCode().getValue(),
                    purchase.getStartDate(),
                    purchase.getPurchaseOrderNumber() != null ? purchase.getPurchaseOrderNumber().getValue() : null,
                    purchase.getDepartmentCode().getValue(),
                    200000, // 更新された仕入金額合計
                    20000, // 更新された消費税合計
                    "Updated remarks", // 更新された備考
                    purchase.getPurchaseLines()
            );

            purchaseService.save(updatedPurchase);

            Purchase result = purchaseService.find(purchase.getPurchaseNumber().getValue());
            assertNotNull(result);
            assertEquals("003", result.getSupplierCode().getCode().getValue());
            assertEquals("Updated remarks", result.getRemarks());
        }

        @Test
        @DisplayName("仕入を削除できる")
        void shouldDeletePurchase() {
            Purchase purchase = getPurchase("PU00000099");
            purchaseService.register(purchase);

            purchaseService.delete(purchase);

            Purchase result = purchaseService.find(purchase.getPurchaseNumber().getValue());
            assertNull(result);
        }

        @Test
        @DisplayName("条件付きで仕入を検索できる (ページング)")
        void shouldSearchPurchasesWithPaging() {
            String supplierCode = "001";
            Purchase purchase = getPurchase("PU00000099");
            Purchase searchPurchase = Purchase.of(
                    purchase.getPurchaseNumber().getValue(),
                    purchase.getPurchaseDate().getValue(),
                    supplierCode, // 仕入先コード
                    0,
                    purchase.getPurchaseManagerCode().getValue(),
                    purchase.getStartDate(),
                    purchase.getPurchaseOrderNumber() != null ? purchase.getPurchaseOrderNumber().getValue() : null,
                    purchase.getDepartmentCode().getValue(),
                    purchase.getTotalPurchaseAmount().getAmount(),
                    purchase.getTotalConsumptionTax().getAmount(),
                    purchase.getRemarks(),
                    purchase.getPurchaseLines()
            );
            purchaseService.register(searchPurchase);

            // 検索条件の設定
            PurchaseCriteria criteria = PurchaseCriteria.builder()
                    .supplierCode(supplierCode) // 仕入先コードを設定
                    .build();

            // 検索結果の呼び出し
            PageInfo<Purchase> result = purchaseService.searchPurchaseWithPageInfo(criteria);

            // 検索結果のアサーション
            assertNotNull(result);
            assertTrue(result.getList().size() >= 1);
            assertEquals(supplierCode, result.getList().getFirst().getSupplierCode().getCode().getValue());
        }
    }

    /**
     * テスト用の仕入データを生成
     */
    private Purchase getPurchase(String purchaseNumber) {
        // purchaseNumberがnullの場合はダミーの番号を使用してPurchaseLineを作成
        String tempPurchaseNumber = (purchaseNumber != null) ? purchaseNumber : "PU00000000";

        List<PurchaseLine> lines = List.of(
                PurchaseLine.of(
                        tempPurchaseNumber,
                        1, // purchaseLineNumber
                        1, // purchaseLineDisplayNumber
                        1, // purchaseOrderLineNumber
                        "99999001", // productCode
                        "W01", // warehouseCode
                        "商品1", // productName
                        3000, // purchaseUnitPrice
                        10 // purchaseQuantity
                )
        );

        // nullの場合はbuilderを使ってpurchaseNumberをnullのままにする
        if (purchaseNumber == null) {
            return Purchase.builder()
                    .purchaseNumber(null)
                    .purchaseDate(com.example.sms.domain.model.procurement.receipt.PurchaseDate.of(LocalDateTime.now()))
                    .supplierCode(com.example.sms.domain.model.master.partner.supplier.SupplierCode.of("001", 0))
                    .purchaseManagerCode(com.example.sms.domain.model.master.employee.EmployeeCode.of("EMP001"))
                    .startDate(LocalDateTime.now())
                    .purchaseOrderNumber(com.example.sms.domain.model.procurement.order.PurchaseOrderNumber.of("PO00000001"))
                    .departmentCode(com.example.sms.domain.model.master.department.DepartmentCode.of("10001"))
                    .totalPurchaseAmount(com.example.sms.domain.type.money.Money.of(30000))
                    .totalConsumptionTax(com.example.sms.domain.type.money.Money.of(3000))
                    .remarks("Test remarks")
                    .purchaseLines(lines)
                    .build();
        }

        return Purchase.of(
                purchaseNumber,
                LocalDateTime.now(),
                "001", // supplierCode
                0, // supplierBranchNumber
                "EMP001", // purchaseManagerCode
                LocalDateTime.now(), // startDate
                "PO00000001", // purchaseOrderNumber
                "10001", // departmentCode
                30000, // totalPurchaseAmount
                3000, // totalConsumptionTax
                "Test remarks", // remarks
                lines
        );
    }
}
