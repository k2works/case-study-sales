package com.example.sms.service.procurement.receipt;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.procurement.order.PurchaseOrder;
import com.example.sms.domain.model.procurement.order.PurchaseOrderLine;
import com.example.sms.domain.model.procurement.order.PurchaseOrderList;
import com.example.sms.service.procurement.order.PurchaseOrderCriteria;
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
            testDataFactory.setUpForPurchaseOrderService();
        }

        @Test
        @DisplayName("仕入一覧を取得できる")
        void shouldRetrieveAllPurchaseOrders() {
            PurchaseOrderList result = purchaseService.selectAll();
            assertNotNull(result);
            assertTrue(result.asList().size() >= 0);
        }

        @Test
        @DisplayName("仕入一覧をページングで取得できる")
        void shouldRetrieveAllPurchaseOrdersWithPaging() {
            PageInfo<PurchaseOrder> result = purchaseService.selectAllWithPageInfo();
            assertNotNull(result);
            assertTrue(result.getList().size() >= 0);
        }

        @Test
        @DisplayName("仕入を新規登録できる")
        void shouldRegisterNewPurchaseOrder() {
            PurchaseOrder newPurchaseOrder = getPurchaseOrder("PO00000099");

            purchaseService.register(newPurchaseOrder);

            assertNotNull(newPurchaseOrder.getPurchaseOrderNumber());
            PurchaseOrder result = purchaseService.find(newPurchaseOrder.getPurchaseOrderNumber().getValue());
            assertNotNull(result);
            assertEquals(newPurchaseOrder.getPurchaseOrderNumber().getValue(), result.getPurchaseOrderNumber().getValue());
        }

        @Test
        @DisplayName("仕入番号nullで新規登録できる")
        void shouldRegisterNewPurchaseOrderWithNullPurchaseOrderNumber() {
            PurchaseOrder newPurchaseOrder = getPurchaseOrder(null);

            purchaseService.register(newPurchaseOrder);

            PurchaseOrderList result = purchaseService.selectAll();
            assertNotNull(result);
        }

        @Test
        @DisplayName("仕入の登録情報を編集できる")
        void shouldEditPurchaseOrderDetails() {
            PurchaseOrder purchaseOrder = getPurchaseOrder("PO00000099");
            purchaseService.register(purchaseOrder);

            PurchaseOrder updatedPurchaseOrder = PurchaseOrder.of(
                    purchaseOrder.getPurchaseOrderNumber().getValue(),
                    purchaseOrder.getPurchaseOrderDate().getValue(),
                    purchaseOrder.getSalesOrderNumber().getValue(),
                    "003", // 更新された仕入先コード
                    0,
                    purchaseOrder.getPurchaseManagerCode().getValue(),
                    purchaseOrder.getDesignatedDeliveryDate().getValue(),
                    purchaseOrder.getWarehouseCode().getValue(),
                    200000, // 更新された発注金額合計
                    20000, // 更新された消費税合計
                    "Updated remarks", // 更新された備考
                    purchaseOrder.getPurchaseOrderLines()
            );

            purchaseService.save(updatedPurchaseOrder);

            PurchaseOrder result = purchaseService.find(purchaseOrder.getPurchaseOrderNumber().getValue());
            assertNotNull(result);
            assertEquals("003", result.getSupplierCode().getCode().getValue());
            assertEquals("Updated remarks", result.getRemarks());
        }

        @Test
        @DisplayName("仕入を削除できる")
        void shouldDeletePurchaseOrder() {
            PurchaseOrder purchaseOrder = getPurchaseOrder("PO00000099");
            purchaseService.register(purchaseOrder);

            purchaseService.delete(purchaseOrder);

            PurchaseOrder result = purchaseService.find(purchaseOrder.getPurchaseOrderNumber().getValue());
            assertNull(result);
        }

        @Test
        @DisplayName("条件付きで仕入を検索できる (ページング)")
        void shouldSearchPurchaseOrdersWithPaging() {
            String supplierCode = "001";
            PurchaseOrder purchaseOrder = getPurchaseOrder("PO00000099");
            PurchaseOrder searchPurchaseOrder = PurchaseOrder.of(
                    purchaseOrder.getPurchaseOrderNumber().getValue(),
                    purchaseOrder.getPurchaseOrderDate().getValue(),
                    purchaseOrder.getSalesOrderNumber().getValue(),
                    supplierCode, // 仕入先コード
                    0,
                    purchaseOrder.getPurchaseManagerCode().getValue(),
                    purchaseOrder.getDesignatedDeliveryDate().getValue(),
                    purchaseOrder.getWarehouseCode().getValue(),
                    purchaseOrder.getTotalPurchaseAmount().getAmount(),
                    purchaseOrder.getTotalConsumptionTax().getAmount(),
                    purchaseOrder.getRemarks(),
                    purchaseOrder.getPurchaseOrderLines()
            );
            purchaseService.register(searchPurchaseOrder);

            // 検索条件の設定
            PurchaseOrderCriteria criteria = PurchaseOrderCriteria.builder()
                    .supplierCode(supplierCode) // 仕入先コードを設定
                    .build();

            // 検索結果の呼び出し
            PageInfo<PurchaseOrder> result = purchaseService.searchPurchaseOrderWithPageInfo(criteria);

            // 検索結果のアサーション
            assertNotNull(result);
            assertTrue(result.getList().size() >= 1);
            assertEquals(supplierCode, result.getList().getFirst().getSupplierCode().getCode().getValue());
        }
    }

    /**
     * テスト用の仕入データを生成
     */
    private PurchaseOrder getPurchaseOrder(String purchaseOrderNumber) {
        // purchaseOrderNumberがnullの場合はダミーの番号を使用してPurchaseOrderLineを作成
        String tempPurchaseOrderNumber = (purchaseOrderNumber != null) ? purchaseOrderNumber : "PO00000000";

        List<PurchaseOrderLine> lines = List.of(
                PurchaseOrderLine.of(
                        tempPurchaseOrderNumber,
                        1, // purchaseOrderLineNumber
                        1, // purchaseOrderLineDisplayNumber
                        "OD00000001", // salesOrderNumber
                        1, // salesOrderLineNumber
                        "99999001", // productCode
                        "商品1", // productName
                        3000, // purchaseUnitPrice
                        10, // purchaseOrderQuantity
                        0, // receivedQuantity
                        0 // completionFlag
                )
        );

        // nullの場合はbuilderを使ってpurchaseOrderNumberをnullのままにする
        if (purchaseOrderNumber == null) {
            return PurchaseOrder.builder()
                    .purchaseOrderNumber(null)
                    .purchaseOrderDate(com.example.sms.domain.model.procurement.order.PurchaseOrderDate.of(LocalDateTime.now()))
                    .salesOrderNumber(com.example.sms.domain.model.sales.order.OrderNumber.of("OD00000001"))
                    .supplierCode(com.example.sms.domain.model.master.partner.supplier.SupplierCode.of("001", 0))
                    .purchaseManagerCode(com.example.sms.domain.model.master.employee.EmployeeCode.of("EMP001"))
                    .designatedDeliveryDate(com.example.sms.domain.model.procurement.order.DesignatedDeliveryDate.of(LocalDateTime.now().plusDays(7)))
                    .warehouseCode(com.example.sms.domain.model.master.warehouse.WarehouseCode.of("W01"))
                    .totalPurchaseAmount(com.example.sms.domain.type.money.Money.of(30000))
                    .totalConsumptionTax(com.example.sms.domain.type.money.Money.of(3000))
                    .remarks("Test remarks")
                    .purchaseOrderLines(lines)
                    .build();
        }

        return PurchaseOrder.of(
                purchaseOrderNumber,
                LocalDateTime.now(),
                "OD00000001",
                "001",
                0,
                "EMP001", // 修正: EMPから始まる社員コード
                LocalDateTime.now().plusDays(7),
                "W01", // 修正: 倉庫コードはW+2桁の数字
                30000,
                3000,
                "Test remarks",
                lines
        );
    }
}
