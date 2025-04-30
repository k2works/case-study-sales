package com.example.sms.service.order;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.product.TaxType;
import com.example.sms.domain.model.sales.order.DeliveryDate;
import com.example.sms.domain.model.sales.order.Order;
import com.example.sms.domain.model.sales.order.OrderLine;
import com.example.sms.domain.model.sales.order.OrderList;
import com.example.sms.domain.model.sales.order.rule.OrderRuleCheckList;
import com.example.sms.service.sales.order.SalesOrderCriteria;
import com.example.sms.service.sales.order.SalesOrderService;
import com.example.sms.service.sales.order.SalesOrderUploadErrorList;
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

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("受注サービス")
class OrderServiceTest {
    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private TestDataFactory testDataFactory;

    @Nested
    @DisplayName("受注")
    class OrderTest {
        @BeforeEach
        void setUp() {
            testDataFactory.setUpForOrderService();
        }

        @Test
        @DisplayName("受注一覧を取得できる")
        void shouldRetrieveAllSalesOrders() {
            OrderList result = salesOrderService.selectAll();
            assertEquals(3, result.asList().size());
            assertNotNull(result.asList().getFirst().getDepartment());
            assertNotNull(result.asList().getFirst().getCustomer());
            assertNotNull(result.asList().getFirst().getCustomer().getShippings());
            assertNotNull(result.asList().getFirst().getEmployee());
            assertNotNull(result.asList().getFirst().getOrderLines());
            assertNotNull(result.asList().getFirst().getOrderLines().getFirst().getProduct());
        }

        @Test
        @DisplayName("受注を新規登録できる")
        void shouldRegisterNewSalesOrder() {
            Order newOrder = TestDataFactoryImpl.getSalesOrder("OD00000009");

            salesOrderService.register(newOrder);

            OrderList result = salesOrderService.selectAll();
            assertEquals(4, result.asList().size());
            assertNotNull(newOrder.getOrderNumber());
            Order order = salesOrderService.find(newOrder.getOrderNumber().getValue());
            assertEquals(newOrder, order);
        }

        @Test
        @DisplayName("受注を新規登録できる")
        void shouldRegisterNewSalesOrderNullOrderNumber() {
            Order newOrder = TestDataFactoryImpl.getSalesOrder(null);

            salesOrderService.register(newOrder);

            OrderList result = salesOrderService.selectAll();
            assertEquals(4, result.asList().size());
        }

        @Test
        @DisplayName("受注の登録情報を編集できる")
        void shouldEditSalesOrderDetails() {
            Order order = TestDataFactoryImpl.getSalesOrder("OD00000009");
            salesOrderService.register(order);

            Order updatedOrder = Order.of(
                    order.getOrderNumber().getValue(),
                    order.getOrderDate().getValue(),
                    order.getDepartmentCode().getValue(),
                    order.getDepartmentStartDate(),
                    "999", // 更新された顧客コード
                    order.getCustomerCode().getBranchNumber(),
                    order.getEmployeeCode().getValue(),
                    order.getDesiredDeliveryDate().getValue(),
                    order.getCustomerOrderNumber(),
                    order.getWarehouseCode(),
                    100000, // 更新された受注金額合計
                    order.getTotalConsumptionTax().getAmount(),
                    "Updated remarks", // 更新された備考
                    order.getOrderLines()
            );

            salesOrderService.save(updatedOrder);

            Order result = salesOrderService.find(order.getOrderNumber().getValue());
            assertEquals("999", result.getCustomerCode().getCode().getValue());
            assertEquals(0, result.getTotalOrderAmount().getAmount());
            assertEquals("Updated remarks", result.getRemarks());
            assertEquals(updatedOrder, result);
        }

        @Test
        @DisplayName("受注を削除できる")
        void shouldDeleteSalesOrder() {
            Order order = TestDataFactoryImpl.getSalesOrder("OD00000009");
            salesOrderService.register(order);

            salesOrderService.delete(order);

            OrderList result = salesOrderService.selectAll();
            assertEquals(3, result.asList().size());
        }

        @Test
        @DisplayName("条件付きで受注を検索できる (ページング)")
        void shouldSearchSalesOrdersWithPaging() {
            String customerCode = "001";
            String warehouseCode = "002";
            Order order = TestDataFactoryImpl.getSalesOrder("OD00000009");
            Order searchOrder = Order.of(
                    order.getOrderNumber().getValue(),
                    order.getOrderDate().getValue(),
                    order.getDepartmentCode().getValue(),
                    order.getDepartmentStartDate(),
                    customerCode, // 顧客コード
                    order.getCustomerCode().getBranchNumber(),
                    order.getEmployeeCode().getValue(),
                    order.getDesiredDeliveryDate().getValue(),
                    order.getCustomerOrderNumber(),
                    warehouseCode, // 倉庫コード
                    order.getTotalOrderAmount().getAmount(),
                    order.getTotalConsumptionTax().getAmount(),
                    order.getRemarks(),
                    order.getOrderLines()
            );
            salesOrderService.register(searchOrder);
            // 検索条件の設定
            SalesOrderCriteria criteria = SalesOrderCriteria.builder()
                    .customerCode(customerCode) // 顧客コードを設定
                    .warehouseCode(warehouseCode)  // 必要に応じて他の条件も設定
                    .build();

            // 検索結果の呼び出し
            PageInfo<Order> result = salesOrderService.searchSalesOrderWithPageInfo(criteria);

            // 検索結果のアサーション
            assertNotNull(result); // 結果がnullでないことを確認
            assertEquals(1, result.getList().size(), "検索結果のサイズが期待値と一致");
            assertEquals(customerCode, result.getList().getFirst().getCustomerCode().getCode().getValue(), "顧客コードが一致");
            assertEquals(warehouseCode, result.getList().getFirst().getWarehouseCode(), "倉庫コードが一致");
        }
    }

    @Nested
    @DisplayName("受注アップロード")
    class OrderUploadTest {
        @BeforeEach
        void setUp() {
            testDataFactory.setUpForOrderUploadService();
        }

        @Nested
        @DisplayName("正常系")
        class Normal {
            @Test
            @DisplayName("単一の受注レコードをアップロードできる")
            void uploadSingleOrderRecord() throws Exception {
                // Arrange
                InputStream is = getClass().getResourceAsStream("/csv/order/order_single.csv");
                MockMultipartFile multipartFile = new MockMultipartFile(
                        "order_single.csv",
                        "order_single.csv",
                        "text/csv",
                        is
                );

                // Act
                salesOrderService.uploadCsvFile(multipartFile);
                Order result = salesOrderService.find("OD00000001");

                // Assert
                assertNotNull(result);
                assertEquals("OD00000001", result.getOrderNumber().getValue());
                assertEquals(LocalDateTime.parse("2025-02-19T00:00"), result.getOrderDate().getValue());
                assertEquals("10000", result.getDepartmentCode().getValue());
                assertEquals(20454, result.getTotalOrderAmount().getAmount());
                assertEquals(2045, result.getTotalConsumptionTax().getAmount());
                assertEquals("初回注文", result.getRemarks());

                assertEquals(2, result.getOrderLines().size());

                OrderLine line1 = result.getOrderLines().get(0);
                assertEquals(1, line1.getOrderLineNumber());
                assertEquals("99999001", line1.getProductCode().getValue());
                assertEquals("商品1", line1.getProductName());
                assertEquals(3000, line1.getSalesUnitPrice().getAmount());
                assertEquals(5, line1.getOrderQuantity().getAmount());
                assertEquals(TaxType.外税, line1.getProduct().getTaxType());

                OrderLine line2 = result.getOrderLines().get(1);
                assertEquals(2, line2.getOrderLineNumber());
                assertEquals("99999002", line2.getProductCode().getValue());
                assertEquals("商品2", line2.getProductName());
                assertEquals(2000, line2.getSalesUnitPrice().getAmount());
                assertEquals(3, line2.getOrderQuantity().getAmount());
                assertEquals(TaxType.内税, line2.getProduct().getTaxType());
            }

            @Test
            @DisplayName("単一の受注レコードを２重アップロードしても正常に処理される")
            void uploadSingleOrderRecordTwice() throws Exception {
                // Arrange
                InputStream is = getClass().getResourceAsStream("/csv/order/order_single.csv");
                MockMultipartFile multipartFile = new MockMultipartFile(
                        "order_single.csv",
                        "order_single.csv",
                        "text/csv",
                        is
                );

                // Act
                salesOrderService.uploadCsvFile(multipartFile);
                salesOrderService.uploadCsvFile(multipartFile);
                Order result = salesOrderService.find("OD00000001");

                // Assert
                assertNotNull(result);
                assertEquals("OD00000001", result.getOrderNumber().getValue());
                assertEquals(LocalDateTime.parse("2025-02-19T00:00"), result.getOrderDate().getValue());
                assertEquals("10000", result.getDepartmentCode().getValue());
                assertEquals(20454, result.getTotalOrderAmount().getAmount());
                assertEquals(2045, result.getTotalConsumptionTax().getAmount());
                assertEquals("初回注文", result.getRemarks());

                assertEquals(2, result.getOrderLines().size());
                assertEquals(2, result.getOrderLines().size());

                OrderLine line1 = result.getOrderLines().get(0);
                assertEquals(1, line1.getOrderLineNumber());
                assertEquals("99999001", line1.getProductCode().getValue());
                assertEquals("商品1", line1.getProductName());
                assertEquals(3000, line1.getSalesUnitPrice().getAmount());
                assertEquals(5, line1.getOrderQuantity().getAmount());
                assertEquals(TaxType.外税, line1.getProduct().getTaxType());

                OrderLine line2 = result.getOrderLines().get(1);
                assertEquals(2, line2.getOrderLineNumber());
                assertEquals("99999002", line2.getProductCode().getValue());
                assertEquals("商品2", line2.getProductName());
                assertEquals(2000, line2.getSalesUnitPrice().getAmount());
                assertEquals(3, line2.getOrderQuantity().getAmount());
                assertEquals(TaxType.内税, line2.getProduct().getTaxType());
            }

            @Test
            @DisplayName("複数の受注レコードをアップロードできる")
            void uploadMultipleOrderRecords() throws Exception {
                // Arrange
                InputStream is = getClass().getResourceAsStream("/csv/order/order_multiple.csv");
                MockMultipartFile multipartFile = new MockMultipartFile(
                        "order_multiple.csv",
                        "order_multiple.csv",
                        "text/csv",
                        is
                );

                // Act
                salesOrderService.uploadCsvFile(multipartFile);
                OrderList result = salesOrderService.selectAll();

                // Assert
                assertNotNull(result);
                assertEquals(2, result.size());

                Order order1 = result.asList().get(0);
                assertEquals("OD00000001", order1.getOrderNumber().getValue());
                assertEquals(21000, order1.getTotalOrderAmount().getAmount());
                assertEquals(2, order1.getOrderLines().size());

                Order order2 = result.asList().get(1);
                assertEquals("OD00000002", order2.getOrderNumber().getValue());
                assertEquals(54540, order2.getTotalOrderAmount().getAmount());
                assertEquals(2, order2.getOrderLines().size());
            }

            @Test
            @DisplayName("複数の受注レコードを２重アップロードしても正常に処理される")
            void uploadMultipleOrderRecordsTwice() throws Exception {
                // Arrange
                InputStream is = getClass().getResourceAsStream("/csv/order/order_multiple.csv");
                MockMultipartFile multipartFile = new MockMultipartFile(
                        "order_multiple.csv",
                        "order_multiple.csv",
                        "text/csv",
                        is
                );

                // Act
                salesOrderService.uploadCsvFile(multipartFile);
                salesOrderService.uploadCsvFile(multipartFile);
                OrderList result = salesOrderService.selectAll();

                // Assert
                assertNotNull(result);
                assertEquals(2, result.size());

                Order order1 = result.asList().getFirst();
                assertEquals("OD00000001", order1.getOrderNumber().getValue());
                assertEquals(21000, order1.getTotalOrderAmount().getAmount());
                assertEquals(2, order1.getOrderLines().size());

                Order order2 = result.asList().get(1);
                assertEquals("OD00000002", order2.getOrderNumber().getValue());
                assertEquals(54540, order2.getTotalOrderAmount().getAmount());
                assertEquals(2, order2.getOrderLines().size());
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
                InputStream is = getClass().getResourceAsStream("/csv/order/order_unregistered_code.csv");
                MockMultipartFile multipartFile = new MockMultipartFile(
                        "order_unregistered_code.csv",
                        "order_unregistered_code.csv",
                        "text/csv",
                        is
                );

                // Act & Assert
                SalesOrderUploadErrorList errorList = salesOrderService.uploadCsvFile(multipartFile);
                assertEquals(4, errorList.size());
                assertTrue(errorList.asList().getFirst().containsValue("部門マスタに存在しません:99999"));
                assertTrue(errorList.asList().get(1).containsValue("取引先マスタに存在しません:CUST999"));
                assertTrue(errorList.asList().get(2).containsValue("商品マスタに存在しません:ITEM999"));
                assertTrue(errorList.asList().get(3).containsValue("社員マスタに存在しません:EMP999"));
            }
        }
    }

    @Nested
    @DisplayName("受注ルール")
    class OrderRuleTest {
        @BeforeEach
        void setUp() {
            testDataFactory.setUpForOrderRuleCheckService();
        }

        @Test
        @DisplayName("受注金額が100万円以上の場合")
        void shouldThrowExceptionWhenTotalOrderAmountIsOver1000000() {
            Order order = TestDataFactoryImpl.getSalesOrder("OD00000009");
            OrderLine orderLine = TestDataFactoryImpl.getSalesOrderLine("OD00000009", 1);
            OrderLine newOrderLine = OrderLine.of(
                    orderLine.getOrderNumber().getValue(),
                    orderLine.getOrderLineNumber(),
                    orderLine.getProductCode().getValue(),
                    orderLine.getProductName(),
                    10000, // 販売単価が10000円
                    100, // 受注数量が100個
                    orderLine.getTaxRate().getRate(),
                    orderLine.getAllocationQuantity().getAmount(),
                    orderLine.getShipmentInstructionQuantity().getAmount(),
                    orderLine.getShippedQuantity().getAmount(),
                    0,
                    orderLine.getDiscountAmount().getAmount(),
                    orderLine.getDeliveryDate().getValue()
            );
            Order newOrder = Order.of(
                    order.getOrderNumber().getValue(),
                    order.getOrderDate().getValue(),
                    order.getDepartmentCode().getValue(),
                    order.getDepartmentStartDate(),
                    order.getCustomerCode().getCode().getValue(),
                    order.getCustomerCode().getBranchNumber(),
                    order.getEmployeeCode().getValue(),
                    order.getDesiredDeliveryDate().getValue(),
                    order.getCustomerOrderNumber(),
                    order.getWarehouseCode(),
                    1000000, // 受注金額が100万円
                    order.getTotalConsumptionTax().getAmount(),
                    order.getRemarks(),
                    List.of(newOrderLine)
            );
            salesOrderService.save(newOrder);

            OrderRuleCheckList checkList = salesOrderService.checkRule();
            assertEquals(2, checkList.size());
            assertTrue(checkList.asList().getFirst().containsValue("受注金額が100万円を超えています。"));
        }

        @Test
        @DisplayName("納期が受注日より前の場合")
        void shouldThrowExceptionWhenDeliveryDateIsBeforeOrderDate() {
            Order order = TestDataFactoryImpl.getSalesOrder("OD00000009");
            OrderLine orderLine = TestDataFactoryImpl.getSalesOrderLine("OD00000009", 1);
            OrderLine newOrderLine = OrderLine.of(
                    orderLine.getOrderNumber().getValue(),
                    orderLine.getOrderLineNumber(),
                    orderLine.getProductCode().getValue(),
                    orderLine.getProductName(),
                    orderLine.getSalesUnitPrice().getAmount(),
                    orderLine.getOrderQuantity().getAmount(),
                    orderLine.getTaxRate().getRate(),
                    orderLine.getAllocationQuantity().getAmount(),
                    orderLine.getShipmentInstructionQuantity().getAmount(),
                    orderLine.getShippedQuantity().getAmount(),
                    orderLine.getCompletionFlag().getValue(),
                    orderLine.getDiscountAmount().getAmount(),
                    orderLine.getDeliveryDate().getValue().minusDays(1) // 納期が受注日より前
            );
            Order newOrder = Order.of(
                    order.getOrderNumber().getValue(),
                    order.getOrderDate().getValue(),
                    order.getDepartmentCode().getValue(),
                    order.getDepartmentStartDate(),
                    order.getCustomerCode().getCode().getValue(),
                    order.getCustomerCode().getBranchNumber(),
                    order.getEmployeeCode().getValue(),
                    order.getDesiredDeliveryDate().getValue(),
                    order.getCustomerOrderNumber(),
                    order.getWarehouseCode(),
                    order.getTotalOrderAmount().getAmount(),
                    order.getTotalConsumptionTax().getAmount(),
                    order.getRemarks(),
                    List.of(newOrderLine)
            );
            salesOrderService.save(newOrder);

            OrderRuleCheckList checkList = salesOrderService.checkRule();
            assertEquals(2, checkList.size());
            assertTrue(checkList.asList().getFirst().containsValue("納期が受注日より前です。"));
        }

        @Test
        @DisplayName("納期を超過している場合")
        void shouldThrowExceptionWhenDeliveryDateIsExceeded() {
            Order order = TestDataFactoryImpl.getSalesOrder("OD00000009");
            OrderLine orderLine = TestDataFactoryImpl.getSalesOrderLine("OD00000009", 1);
            OrderLine newOrderLine = OrderLine.of(
                    orderLine.getOrderNumber().getValue(),
                    orderLine.getOrderLineNumber(),
                    orderLine.getProductCode().getValue(),
                    orderLine.getProductName(),
                    orderLine.getSalesUnitPrice().getAmount(),
                    orderLine.getOrderQuantity().getAmount(),
                    orderLine.getTaxRate().getRate(),
                    orderLine.getAllocationQuantity().getAmount(),
                    orderLine.getShipmentInstructionQuantity().getAmount(),
                    orderLine.getShippedQuantity().getAmount(),
                    orderLine.getCompletionFlag().getValue(),
                    orderLine.getDiscountAmount().getAmount(),
                    DeliveryDate.of(LocalDateTime.now().minusDays(1)).getValue() // 納期を超過
            );
            Order newOrder = Order.of(
                    order.getOrderNumber().getValue(),
                    order.getOrderDate().getValue(),
                    order.getDepartmentCode().getValue(),
                    order.getDepartmentStartDate(),
                    order.getCustomerCode().getCode().getValue(),
                    order.getCustomerCode().getBranchNumber(),
                    order.getEmployeeCode().getValue(),
                    order.getDesiredDeliveryDate().getValue(),
                    order.getCustomerOrderNumber(),
                    order.getWarehouseCode(),
                    order.getTotalOrderAmount().getAmount(),
                    order.getTotalConsumptionTax().getAmount(),
                    order.getRemarks(),
                    List.of(newOrderLine)
            );
            salesOrderService.save(newOrder);

            OrderRuleCheckList checkList = salesOrderService.checkRule();
            assertEquals(1, checkList.size());
            assertTrue(checkList.asList().getFirst().containsValue("納期を超過しています。"));
        }

        @Test
        @DisplayName("完了済みの受注の場合")
        void shouldNotThrowExceptionWhenSalesOrderIsCompleted() {
            Order order = TestDataFactoryImpl.getSalesOrder("OD00000009");
            OrderLine orderLine = TestDataFactoryImpl.getSalesOrderLine("OD00000009", 1);
            OrderLine newOrderLine = OrderLine.of(
                    orderLine.getOrderNumber().getValue(),
                    orderLine.getOrderLineNumber(),
                    orderLine.getProductCode().getValue(),
                    orderLine.getProductName(),
                    orderLine.getSalesUnitPrice().getAmount(),
                    orderLine.getOrderQuantity().getAmount(),
                    orderLine.getTaxRate().getRate(),
                    orderLine.getAllocationQuantity().getAmount(),
                    orderLine.getShipmentInstructionQuantity().getAmount(),
                    orderLine.getShippedQuantity().getAmount(),
                    1, // 完了済み
                    orderLine.getDiscountAmount().getAmount(),
                    orderLine.getDeliveryDate().getValue()
            );
            Order newOrder = Order.of(
                    order.getOrderNumber().getValue(),
                    order.getOrderDate().getValue(),
                    order.getDepartmentCode().getValue(),
                    order.getDepartmentStartDate(),
                    order.getCustomerCode().getCode().getValue(),
                    order.getCustomerCode().getBranchNumber(),
                    order.getEmployeeCode().getValue(),
                    order.getDesiredDeliveryDate().getValue(),
                    order.getCustomerOrderNumber(),
                    order.getWarehouseCode(),
                    order.getTotalOrderAmount().getAmount(),
                    order.getTotalConsumptionTax().getAmount(),
                    order.getRemarks(),
                    List.of(newOrderLine)
            );
            salesOrderService.save(newOrder);

            OrderRuleCheckList checkList = salesOrderService.checkRule();
            assertEquals(0, checkList.size());
        }
    }
}
