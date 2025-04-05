package com.example.sms.service.shipping;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales_order.CompletionFlag;
import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;
import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.model.shipping.ShippingList;
import com.example.sms.domain.model.shipping.rule.ShippingRuleCheckList;
import com.example.sms.service.sales_order.SalesOrderService;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("出荷サービス")
class ShippingServiceTest {

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private TestDataFactory testDataFactory;
    @Autowired
    private SalesOrderService salesOrderService;

    @Nested
    @DisplayName("出荷")
    class ShippingTest {

        @BeforeEach
        void setUp() {
            testDataFactory.setUpForSalesOrderService();
        }

        @Test
        @DisplayName("出荷一覧を取得できる")
        void shouldRetrieveAllShippings() {
            ShippingList result = shippingService.selectAll();
            assertNotNull(result);
            assertFalse(result.asList().isEmpty());
            assertNotNull(result.asList().getFirst().getDepartment());
            assertNotNull(result.asList().getFirst().getCustomer());
            assertNotNull(result.asList().getFirst().getEmployee());
            assertNotNull(result.asList().getFirst().getProduct());
        }

        @Test
        @DisplayName("注文番号で検索できる")
        void shouldFindShippingById() {
            ShippingList allShippings = shippingService.selectAll();
            if (!allShippings.asList().isEmpty()) {
                Shipping shipping = allShippings.asList().getFirst();

                Shipping foundShipping = shippingService.findById(shipping.getOrderNumber().getValue()).orElse(null);

                assertNotNull(foundShipping);
                assertEquals(shipping.getOrderNumber().getValue(), foundShipping.getOrderNumber().getValue());
                assertEquals(shipping.getProductCode().getValue(), foundShipping.getProductCode().getValue());
                assertEquals(shipping.getProductName(), foundShipping.getProductName());
            }
        }

        @Test
        @DisplayName("出荷一覧をページング付きで取得できる")
        void shouldRetrieveAllShippingsWithPageInfo() {
            PageInfo<Shipping> result = shippingService.selectAllWithPageInfo();

            assertNotNull(result);
            assertNotNull(result.getList());
            assertFalse(result.getList().isEmpty());
        }

        @Test
        @DisplayName("条件付きで出荷を検索できる (ページング)")
        void shouldSearchShippingsWithPageInfo() {
            ShippingList allShippings = shippingService.selectAll();
            if (!allShippings.asList().isEmpty()) {
                Shipping shipping = allShippings.asList().getFirst();

                ShippingCriteria criteria = ShippingCriteria.builder()
                        .productCode(shipping.getProductCode().getValue())
                        .build();

                PageInfo<Shipping> result = shippingService.searchWithPageInfo(criteria);

                assertNotNull(result);
                assertNotNull(result.getList());
                assertFalse(result.getList().isEmpty());
                assertTrue(result.getList().stream().anyMatch(s -> 
                        s.getProductCode().getValue().equals(shipping.getProductCode().getValue())));
            }
        }

        @Test
        @DisplayName("出荷情報を保存できる")
        void shouldSaveShipping() {
            ShippingList allShippings = shippingService.selectAll();
            if (!allShippings.asList().isEmpty()) {
                Shipping shipping = allShippings.asList().getFirst();

                shippingService.save(shipping);

                Shipping savedShipping = shippingService.findById(shipping.getOrderNumber().getValue()).orElse(null);

                assertNotNull(savedShipping);
                assertEquals(shipping.getOrderNumber().getValue(), savedShipping.getOrderNumber().getValue());
                assertEquals(shipping.getProductCode().getValue(), savedShipping.getProductCode().getValue());
            }
        }

        @Test
        @DisplayName("出荷指示を行う")
        void shouldOrderShipping() {
            ShippingList allShippings = shippingService.selectAll();
            if (!allShippings.asList().isEmpty()) {
                Shipping shipping = allShippings.asList().getFirst();
                shippingService.orderShipping(allShippings);

                Shipping orderedShipping = shippingService.findById(shipping.getOrderNumber().getValue()).orElse(null);
                SalesOrderList salesOrderList = salesOrderService.selectAll();

                assertNotNull(orderedShipping);
                assertEquals(CompletionFlag.完了, orderedShipping.getCompletionFlag());
                for (SalesOrder salesOrder : salesOrderList.asList()) {
                    for (SalesOrderLine salesOrderLine : salesOrder.getSalesOrderLines()) {
                        if (salesOrderLine.getProductCode().getValue().equals(shipping.getProductCode().getValue())) {
                            assertEquals(CompletionFlag.完了, salesOrderLine.getCompletionFlag());
                        }
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("出荷ルール")
    class ShippingRuleTest {

        @BeforeEach
        void setUp() {
            testDataFactory.setUpForShippingRuleCheckService();
        }

        @Test
        @DisplayName("出荷数量が受注数量を超えている場合")
        void shouldCheckShippingRules() {
            SalesOrder salesOrder = TestDataFactoryImpl.getSalesOrder("1000000009");
            SalesOrderLine salesOrderLine = TestDataFactoryImpl.getSalesOrderLine("1000000009", 1);
            SalesOrderLine newSalesOrderLine = SalesOrderLine.of(
                    salesOrderLine.getOrderNumber().getValue(),
                    salesOrderLine.getOrderLineNumber(),
                    salesOrderLine.getProductCode().getValue(),
                    salesOrderLine.getProductName(),
                    salesOrderLine.getSalesUnitPrice().getAmount(),
                    1,
                    salesOrderLine.getTaxRate().getRate(),
                    salesOrderLine.getAllocationQuantity().getAmount(),
                    salesOrderLine.getShipmentInstructionQuantity().getAmount(),
                    10,
                    CompletionFlag.未完了.getValue(),
                    salesOrderLine.getDiscountAmount().getAmount(),
                    LocalDateTime.now().plus(10, ChronoUnit.DAYS)
            );
            SalesOrder newSalesOrder = SalesOrder.of(
                    salesOrder.getOrderNumber().getValue(),
                    salesOrder.getOrderDate().getValue(),
                    salesOrder.getDepartmentCode().getValue(),
                    salesOrder.getDepartmentStartDate(),
                    salesOrder.getCustomerCode().getCode().getValue(),
                    salesOrder.getCustomerCode().getBranchNumber(),
                    salesOrder.getEmployeeCode().getValue(),
                    salesOrder.getDesiredDeliveryDate().getValue(),
                    salesOrder.getCustomerOrderNumber(),
                    salesOrder.getWarehouseCode(),
                    salesOrder.getTotalOrderAmount().getAmount(),
                    salesOrder.getTotalConsumptionTax().getAmount(),
                    salesOrder.getRemarks(),
                    List.of(newSalesOrderLine)
            );
            salesOrderService.save(newSalesOrder);

            ShippingRuleCheckList checkList = shippingService.checkRule();
            assertNotNull(checkList);
            assertTrue(checkList.asList().getFirst().containsValue("出荷数量が受注数量を超えています。"));
        }

        @Test
        @DisplayName("納期を超過している場合")
        void shouldCheckShippingOverdue() {
            SalesOrder salesOrder = TestDataFactoryImpl.getSalesOrder("1000000009");
            SalesOrderLine salesOrderLine = TestDataFactoryImpl.getSalesOrderLine("1000000009", 1);
            SalesOrderLine newSalesOrderLine = SalesOrderLine.of(
                    salesOrderLine.getOrderNumber().getValue(),
                    salesOrderLine.getOrderLineNumber(),
                    salesOrderLine.getProductCode().getValue(),
                    salesOrderLine.getProductName(),
                    salesOrderLine.getSalesUnitPrice().getAmount(),
                    salesOrderLine.getOrderQuantity().getAmount(),
                    salesOrderLine.getTaxRate().getRate(),
                    salesOrderLine.getAllocationQuantity().getAmount(),
                    salesOrderLine.getShipmentInstructionQuantity().getAmount(),
                    salesOrderLine.getShippedQuantity().getAmount(),
                    CompletionFlag.未完了.getValue(),
                    salesOrderLine.getDiscountAmount().getAmount(),
                    LocalDateTime.now().minus(10, ChronoUnit.DAYS)
            );
            SalesOrder newSalesOrder = SalesOrder.of(
                    salesOrder.getOrderNumber().getValue(),
                    salesOrder.getOrderDate().getValue(),
                    salesOrder.getDepartmentCode().getValue(),
                    salesOrder.getDepartmentStartDate(),
                    salesOrder.getCustomerCode().getCode().getValue(),
                    salesOrder.getCustomerCode().getBranchNumber(),
                    salesOrder.getEmployeeCode().getValue(),
                    salesOrder.getDesiredDeliveryDate().getValue(),
                    salesOrder.getCustomerOrderNumber(),
                    salesOrder.getWarehouseCode(),
                    salesOrder.getTotalOrderAmount().getAmount(),
                    salesOrder.getTotalConsumptionTax().getAmount(),
                    salesOrder.getRemarks(),
                    List.of(newSalesOrderLine)
            );
            salesOrderService.save(newSalesOrder);

            ShippingRuleCheckList checkList = shippingService.checkRule();
            assertNotNull(checkList);
            assertTrue(checkList.asList().getFirst().containsValue("納期を超過しています。"));
        }
    }
}
