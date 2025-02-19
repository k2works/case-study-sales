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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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
            SalesOrder newSalesOrder = TestDataFactoryImpl.getSalesOrder("SO999999");

            salesOrderService.register(newSalesOrder);

            SalesOrderList result = salesOrderService.selectAll();
            assertEquals(4, result.asList().size());
            SalesOrder salesOrder = salesOrderService.find(newSalesOrder.getOrderNumber());
            assertEquals(newSalesOrder, salesOrder);
        }

        @Test
        @DisplayName("受注の登録情報を編集できる")
        void shouldEditSalesOrderDetails() {
            SalesOrder salesOrder = TestDataFactoryImpl.getSalesOrder("SO999999");
            salesOrderService.register(salesOrder);

            SalesOrder updatedSalesOrder = SalesOrder.of(
                    salesOrder.getOrderNumber(),
                    salesOrder.getOrderDate(),
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

            SalesOrder result = salesOrderService.find(salesOrder.getOrderNumber());
            assertEquals("999", result.getCustomerCode());
            assertEquals(100000, result.getTotalOrderAmount());
            assertEquals("Updated remarks", result.getRemarks());
            assertEquals(updatedSalesOrder, result);
        }

        @Test
        @DisplayName("受注を削除できる")
        void shouldDeleteSalesOrder() {
            SalesOrder salesOrder = TestDataFactoryImpl.getSalesOrder("SO999999");
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
            SalesOrder salesOrder = TestDataFactoryImpl.getSalesOrder("SO999999");
            SalesOrder searchOrder = SalesOrder.of(
                    salesOrder.getOrderNumber(),
                    salesOrder.getOrderDate(),
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
                SalesOrder result = salesOrderService.find("ORD001");

                // Assert
                assertNotNull(result);
                assertEquals("ORD001", result.getOrderNumber());
                assertEquals(LocalDateTime.parse("2025-02-19T00:00"), result.getOrderDate());
                assertEquals("D001", result.getDepartmentCode());
                assertEquals(15000, result.getTotalOrderAmount());
                assertEquals(1500, result.getTotalConsumptionTax());
                assertEquals("初回注文", result.getRemarks());

                assertEquals(2, result.getSalesOrderLines().size());

                SalesOrderLine line1 = result.getSalesOrderLines().get(0);
                assertEquals(1, line1.getOrderLineNumber());
                assertEquals("ITEM001", line1.getProductCode());
                assertEquals("商品A", line1.getProductName());
                assertEquals(3000, line1.getSalesUnitPrice());
                assertEquals(5, line1.getOrderQuantity());

                SalesOrderLine line2 = result.getSalesOrderLines().get(1);
                assertEquals(2, line2.getOrderLineNumber());
                assertEquals("ITEM002", line2.getProductCode());
                assertEquals("商品B", line2.getProductName());
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
                assertEquals("ORD001", order1.getOrderNumber());
                assertEquals(15000, order1.getTotalOrderAmount());
                assertEquals(2, order1.getSalesOrderLines().size());

                SalesOrder order2 = result.asList().get(1);
                assertEquals("ORD002", order2.getOrderNumber());
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
                assertThrows(DataIntegrityViolationException.class, () -> {
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
        }
    }
}
