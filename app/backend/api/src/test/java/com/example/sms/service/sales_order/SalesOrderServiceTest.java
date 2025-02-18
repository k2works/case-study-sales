package com.example.sms.service.sales_order;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
@DisplayName("受注サービス")
class SalesOrderServiceTest {
    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForSalesOrderService();
    }

    @Nested
    @DisplayName("受注")
    class SalesOrderTest {
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
}