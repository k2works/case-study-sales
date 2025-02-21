package com.example.sms.service.sales_order;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;
import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("受注サービス")
class SalesOrderServiceTest {
    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private TestDataFactory testDataFactory;

    @Nested
    @DisplayName("受注")
    class SalesOrderTest {
        @BeforeEach
        void setUp() {
            testDataFactory.setUpForSalesOrderService();
        }

        @Test
        @DisplayName("受注一覧を取得できる")
        void shouldRetrieveAllSalesOrders() {
            SalesOrderList result = salesOrderService.selectAll();
            assertEquals(3, result.asList().size());
        }

        @Test
        @DisplayName("受注を新規登録できる")
        void shouldRegisterNewSalesOrder() {
            SalesOrder newSalesOrder = TestDataFactoryImpl.getSalesOrder("1000000009");

            salesOrderService.register(newSalesOrder);

            SalesOrderList result = salesOrderService.selectAll();
            assertEquals(4, result.asList().size());
            SalesOrder salesOrder = salesOrderService.find(newSalesOrder.getOrderNumber().getValue());
            assertEquals(newSalesOrder, salesOrder);
        }

        @Test
        @DisplayName("受注の登録情報を編集できる")
        void shouldEditSalesOrderDetails() {
            SalesOrder salesOrder = TestDataFactoryImpl.getSalesOrder("1000000009");
            salesOrderService.register(salesOrder);

            SalesOrder updatedSalesOrder = SalesOrder.of(
                    salesOrder.getOrderNumber().getValue(),
                    salesOrder.getOrderDate().getValue(),
                    salesOrder.getDepartmentCode(),
                    salesOrder.getDepartmentStartDate(),
                    "999", // 更新された顧客コード
                    salesOrder.getCustomerBranchNumber(),
                    salesOrder.getEmployeeCode(),
                    salesOrder.getDesiredDeliveryDate(),
                    salesOrder.getCustomerOrderNumber(),
                    salesOrder.getWarehouseCode(),
                    100000, // 更新された受注金額合計
                    salesOrder.getTotalConsumptionTax(),
                    "Updated remarks", // 更新された備考
                    salesOrder.getSalesOrderLines()
            );

            salesOrderService.save(updatedSalesOrder);

            SalesOrder result = salesOrderService.find(salesOrder.getOrderNumber().getValue());
            assertEquals("999", result.getCustomerCode());
            assertEquals(100000, result.getTotalOrderAmount());
            assertEquals("Updated remarks", result.getRemarks());
            assertEquals(updatedSalesOrder, result);
        }

        @Test
        @DisplayName("受注を削除できる")
        void shouldDeleteSalesOrder() {
            SalesOrder salesOrder = TestDataFactoryImpl.getSalesOrder("1000000009");
            salesOrderService.register(salesOrder);

            salesOrderService.delete(salesOrder);

            SalesOrderList result = salesOrderService.selectAll();
            assertEquals(3, result.asList().size());
        }

        @Test
        @DisplayName("条件付きで受注を検索できる (ページング)")
        void shouldSearchSalesOrdersWithPaging() {
            String customerCode = "001";
            String warehouseCode = "002";
            SalesOrder salesOrder = TestDataFactoryImpl.getSalesOrder("1000000009");
            SalesOrder searchOrder = SalesOrder.of(
                    salesOrder.getOrderNumber().getValue(),
                    salesOrder.getOrderDate().getValue(),
                    salesOrder.getDepartmentCode(),
                    salesOrder.getDepartmentStartDate(),
                    customerCode, // 顧客コード
                    salesOrder.getCustomerBranchNumber(),
                    salesOrder.getEmployeeCode(),
                    salesOrder.getDesiredDeliveryDate(),
                    salesOrder.getCustomerOrderNumber(),
                    warehouseCode, // 倉庫コード
                    salesOrder.getTotalOrderAmount(),
                    salesOrder.getTotalConsumptionTax(),
                    salesOrder.getRemarks(),
                    salesOrder.getSalesOrderLines()
            );
            salesOrderService.register(searchOrder);
            // 検索条件の設定
            SalesOrderCriteria criteria = SalesOrderCriteria.builder()
                    .customerCode(customerCode) // 顧客コードを設定
                    .warehouseCode(warehouseCode)  // 必要に応じて他の条件も設定
                    .build();

            // 検索結果の呼び出し
            PageInfo<SalesOrder> result = salesOrderService.searchSalesOrderWithPageInfo(criteria);

            // 検索結果のアサーション
            assertNotNull(result); // 結果がnullでないことを確認
            assertEquals(1, result.getList().size(), "検索結果のサイズが期待値と一致");
            assertEquals(customerCode, result.getList().getFirst().getCustomerCode(), "顧客コードが一致");
            assertEquals(warehouseCode, result.getList().getFirst().getWarehouseCode(), "倉庫コードが一致");
        }
    }

    @Nested
    @DisplayName("受注アップロード")
    class SalesOrderUploadTest {
        @BeforeEach
        void setUp() {
            testDataFactory.setUpForSalesOrderUploadService();
        }

        @Nested
        @DisplayName("正常系")
        class Normal {
            @Test
            @DisplayName("単一の受注レコードをアップロードできる")
            void uploadSingleOrderRecord() throws Exception {
                // Arrange
                InputStream is = getClass().getResourceAsStream("/csv/sales_order/sales_order_single.csv");
                MockMultipartFile multipartFile = new MockMultipartFile(
                        "sales_order_single.csv",
                        "sales_order_single.csv",
                        "text/csv",
                        is
                );

                // Act
                salesOrderService.uploadCsvFile(multipartFile);
                SalesOrder result = salesOrderService.find("1000000001");

                // Assert
                assertNotNull(result);
                assertEquals("1000000001", result.getOrderNumber().getValue());
                assertEquals(LocalDateTime.parse("2025-02-19T00:00"), result.getOrderDate().getValue());
                assertEquals("10000", result.getDepartmentCode());
                assertEquals(15000, result.getTotalOrderAmount());
                assertEquals(1500, result.getTotalConsumptionTax());
                assertEquals("初回注文", result.getRemarks());

                assertEquals(2, result.getSalesOrderLines().size());

                SalesOrderLine line1 = result.getSalesOrderLines().get(0);
                assertEquals(1, line1.getOrderLineNumber());
                assertEquals("99999001", line1.getProductCode());
                assertEquals("商品1", line1.getProductName());
                assertEquals(3000, line1.getSalesUnitPrice());
                assertEquals(5, line1.getOrderQuantity());

                SalesOrderLine line2 = result.getSalesOrderLines().get(1);
                assertEquals(2, line2.getOrderLineNumber());
                assertEquals("99999002", line2.getProductCode());
                assertEquals("商品2", line2.getProductName());
                assertEquals(2000, line2.getSalesUnitPrice());
                assertEquals(3, line2.getOrderQuantity());
            }

            @Test
            @DisplayName("単一の受注レコードを２重アップロードしても正常に処理される")
            void uploadSingleOrderRecordTwice() throws Exception {
                // Arrange
                InputStream is = getClass().getResourceAsStream("/csv/sales_order/sales_order_single.csv");
                MockMultipartFile multipartFile = new MockMultipartFile(
                        "sales_order_single.csv",
                        "sales_order_single.csv",
                        "text/csv",
                        is
                );

                // Act
                salesOrderService.uploadCsvFile(multipartFile);
                salesOrderService.uploadCsvFile(multipartFile);
                SalesOrder result = salesOrderService.find("1000000001");

                // Assert
                assertNotNull(result);
                assertEquals("1000000001", result.getOrderNumber().getValue());
                assertEquals(LocalDateTime.parse("2025-02-19T00:00"), result.getOrderDate().getValue());
                assertEquals("10000", result.getDepartmentCode());
                assertEquals(15000, result.getTotalOrderAmount());
                assertEquals(1500, result.getTotalConsumptionTax());
                assertEquals("初回注文", result.getRemarks());

                assertEquals(2, result.getSalesOrderLines().size());
                assertEquals(2, result.getSalesOrderLines().size());

                SalesOrderLine line1 = result.getSalesOrderLines().get(0);
                assertEquals(1, line1.getOrderLineNumber());
                assertEquals("99999001", line1.getProductCode());
                assertEquals("商品1", line1.getProductName());
                assertEquals(3000, line1.getSalesUnitPrice());
                assertEquals(5, line1.getOrderQuantity());

                SalesOrderLine line2 = result.getSalesOrderLines().get(1);
                assertEquals(2, line2.getOrderLineNumber());
                assertEquals("99999002", line2.getProductCode());
                assertEquals("商品2", line2.getProductName());
                assertEquals(2000, line2.getSalesUnitPrice());
                assertEquals(3, line2.getOrderQuantity());
            }

            @Test
            @DisplayName("複数の受注レコードをアップロードできる")
            void uploadMultipleOrderRecords() throws Exception {
                // Arrange
                InputStream is = getClass().getResourceAsStream("/csv/sales_order/sales_order_multiple.csv");
                MockMultipartFile multipartFile = new MockMultipartFile(
                        "sales_order_multiple.csv",
                        "sales_order_multiple.csv",
                        "text/csv",
                        is
                );

                // Act
                salesOrderService.uploadCsvFile(multipartFile);
                SalesOrderList result = salesOrderService.selectAll();

                // Assert
                assertNotNull(result);
                assertEquals(2, result.size());

                SalesOrder order1 = result.asList().get(0);
                assertEquals("1000000001", order1.getOrderNumber().getValue());
                assertEquals(15000, order1.getTotalOrderAmount());
                assertEquals(2, order1.getSalesOrderLines().size());

                SalesOrder order2 = result.asList().get(1);
                assertEquals("1000000002", order2.getOrderNumber().getValue());
                assertEquals(30000, order2.getTotalOrderAmount());
                assertEquals(2, order2.getSalesOrderLines().size());
            }

            @Test
            @DisplayName("複数の受注レコードを２重アップロードしても正常に処理される")
            void uploadMultipleOrderRecordsTwice() throws Exception {
                // Arrange
                InputStream is = getClass().getResourceAsStream("/csv/sales_order/sales_order_multiple.csv");
                MockMultipartFile multipartFile = new MockMultipartFile(
                        "sales_order_multiple.csv",
                        "sales_order_multiple.csv",
                        "text/csv",
                        is
                );

                // Act
                salesOrderService.uploadCsvFile(multipartFile);
                salesOrderService.uploadCsvFile(multipartFile);
                SalesOrderList result = salesOrderService.selectAll();

                // Assert
                assertNotNull(result);
                assertEquals(2, result.size());

                SalesOrder order1 = result.asList().get(0);
                assertEquals("1000000001", order1.getOrderNumber().getValue());
                assertEquals(15000, order1.getTotalOrderAmount());
                assertEquals(2, order1.getSalesOrderLines().size());

                SalesOrder order2 = result.asList().get(1);
                assertEquals("1000000002", order2.getOrderNumber().getValue());
                assertEquals(30000, order2.getTotalOrderAmount());
                assertEquals(2, order2.getSalesOrderLines().size());
            }
        }


        @Nested
        @DisplayName("異常系")
        class Abnormal {
            @Test
            @DisplayName("不正なCSVファイルの場合")
            void uploadCsvFile_withInValidFile() {
                // Arrange
                MockMultipartFile file = new MockMultipartFile(
                        "file", "orders.csv", "text/csv",
                        "sample".getBytes(StandardCharsets.UTF_8));

                // Act & Assert
                assertThrows(NullPointerException.class, () -> {
                    salesOrderService.uploadCsvFile(file);
                });
            }

            @Test
            @DisplayName("アップロードファイルがnullの場合")
            void uploadCsvFile_withNullFile_throwsNullPointerException() {
                // Act & Assert
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> salesOrderService.uploadCsvFile(null));
                assertEquals("アップロードファイルは必須です。", exception.getMessage());
            }

            @Test
            @DisplayName("アップロードファイルが空の場合")
            void uploadCsvFile_withEmptyFile_throwsIllegalArgumentException() {
                // Arrange
                MockMultipartFile file = new MockMultipartFile(
                        "file", "orders.csv", "text/csv", new byte[0]);

                // Act & Assert
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> salesOrderService.uploadCsvFile(file));
                assertEquals("アップロードファイルが空です。", exception.getMessage());
            }

            @Test
            @DisplayName("アップロードファイルがCSVではない場合")
            void uploadCsvFile_withNonCsvFile_throwsIllegalArgumentException() {
                // Arrange
                MockMultipartFile file = new MockMultipartFile(
                        "file", "orders.txt", "text/plain",
                        "sample data".getBytes(StandardCharsets.UTF_8));

                // Act & Assert
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> salesOrderService.uploadCsvFile(file));
                assertEquals("アップロードファイルがCSVではありません。", exception.getMessage());
            }

            @Test
            @DisplayName("アップロードファイルが10MBを超える場合")
            void uploadCsvFile_withLargeFile_throwsIllegalArgumentException() {
                // Arrange
                byte[] largeData = new byte[10000001]; // 10 MB + 1 byte
                MockMultipartFile file = new MockMultipartFile(
                        "file", "orders.csv", "text/csv", largeData);

                // Act & Assert
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> salesOrderService.uploadCsvFile(file));
                assertEquals("アップロードファイルが大きすぎます。", exception.getMessage());
            }

            @Test
            @DisplayName("マスタ未登録のコードが含まれる場合")
            void uploadCsvFile_withUnregisteredMasterCode_throwsIllegalArgumentException() throws Exception {
                // Arrange
                InputStream is = getClass().getResourceAsStream("/csv/sales_order/sales_order_unregistered_code.csv");
                MockMultipartFile multipartFile = new MockMultipartFile(
                        "sales_order_unregistered_code.csv",
                        "sales_order_unregistered_code.csv",
                        "text/csv",
                        is
                );

                // Act & Assert
                List<Map<String, String>> errorList = salesOrderService.uploadCsvFile(multipartFile);
                assertEquals(4, errorList.size());
                assertTrue(errorList.get(0).containsValue("部門マスタに存在しません:99999"));
                assertTrue(errorList.get(1).containsValue("取引先マスタに存在しません:CUST999"));
                assertTrue(errorList.get(2).containsValue("商品マスタに存在しません:ITEM999"));
                assertTrue(errorList.get(3).containsValue("社員マスタに存在しません:EMP999"));
            }
        }
    }

    @Nested
    @DisplayName("受注ルール")
    class SalesOrderRuleTest {
        @BeforeEach
        void setUp() {
            testDataFactory.setUpForSalesOrderService();
        }

        @Test
        @DisplayName("受注金額が100万円以上の場合")
        void shouldThrowExceptionWhenTotalOrderAmountIsOver1000000() {
            SalesOrder salesOrder = TestDataFactoryImpl.getSalesOrder("1000000009");
            SalesOrderLine salesOrderLine = TestDataFactoryImpl.getSalesOrderLine("1000000009", 1);
            SalesOrderLine newSalesOrderLine = SalesOrderLine.of(
                    salesOrderLine.getOrderNumber(),
                    salesOrderLine.getOrderLineNumber(),
                    salesOrderLine.getProductCode(),
                    salesOrderLine.getProductName(),
                    10000, // 販売単価が10000円
                    100, // 受注数量が100個
                    salesOrderLine.getTaxRate(),
                    salesOrderLine.getAllocationQuantity(),
                    salesOrderLine.getShipmentInstructionQuantity(),
                    salesOrderLine.getShippedQuantity(),
                    0,
                    salesOrderLine.getDiscountAmount(),
                    salesOrderLine.getDeliveryDate()
            );
            SalesOrder newSalesOrder = SalesOrder.of(
                    salesOrder.getOrderNumber().getValue(),
                    salesOrder.getOrderDate().getValue(),
                    salesOrder.getDepartmentCode(),
                    salesOrder.getDepartmentStartDate(),
                    salesOrder.getCustomerCode(),
                    salesOrder.getCustomerBranchNumber(),
                    salesOrder.getEmployeeCode(),
                    salesOrder.getDesiredDeliveryDate(),
                    salesOrder.getCustomerOrderNumber(),
                    salesOrder.getWarehouseCode(),
                    1000000, // 受注金額が100万円
                    salesOrder.getTotalConsumptionTax(),
                    salesOrder.getRemarks(),
                    List.of(newSalesOrderLine)
            );
            salesOrderService.save(newSalesOrder);

            List<Map<String, String>> checkList = salesOrderService.checkRule();
            assertEquals(1, checkList.size());
            assertTrue(checkList.getFirst().containsValue("受注金額が100万円を超えています。"));
        }

        @Test
        @DisplayName("完了済みの受注の場合")
        void shouldNotThrowExceptionWhenSalesOrderIsCompleted() {
            SalesOrder salesOrder = TestDataFactoryImpl.getSalesOrder("1000000009");
            SalesOrderLine salesOrderLine = TestDataFactoryImpl.getSalesOrderLine("1000000009", 1);
            SalesOrderLine newSalesOrderLine = SalesOrderLine.of(
                    salesOrderLine.getOrderNumber(),
                    salesOrderLine.getOrderLineNumber(),
                    salesOrderLine.getProductCode(),
                    salesOrderLine.getProductName(),
                    salesOrderLine.getSalesUnitPrice(),
                    salesOrderLine.getOrderQuantity(),
                    salesOrderLine.getTaxRate(),
                    salesOrderLine.getAllocationQuantity(),
                    salesOrderLine.getShipmentInstructionQuantity(),
                    salesOrderLine.getShippedQuantity(),
                    1, // 完了済み
                    salesOrderLine.getDiscountAmount(),
                    salesOrderLine.getDeliveryDate()
            );
            SalesOrder newSalesOrder = SalesOrder.of(
                    salesOrder.getOrderNumber().getValue(),
                    salesOrder.getOrderDate().getValue(),
                    salesOrder.getDepartmentCode(),
                    salesOrder.getDepartmentStartDate(),
                    salesOrder.getCustomerCode(),
                    salesOrder.getCustomerBranchNumber(),
                    salesOrder.getEmployeeCode(),
                    salesOrder.getDesiredDeliveryDate(),
                    salesOrder.getCustomerOrderNumber(),
                    salesOrder.getWarehouseCode(),
                    salesOrder.getTotalOrderAmount(),
                    salesOrder.getTotalConsumptionTax(),
                    salesOrder.getRemarks(),
                    List.of(newSalesOrderLine)
            );
            salesOrderService.save(newSalesOrder);

            List<Map<String, String>> checkList = salesOrderService.checkRule();
            assertEquals(0, checkList.size());
        }
    }
}
