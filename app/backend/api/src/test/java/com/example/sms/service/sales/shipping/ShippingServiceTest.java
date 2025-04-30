package com.example.sms.service.sales.shipping;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales.order.CompletionFlag;
import com.example.sms.domain.model.sales.order.Order;
import com.example.sms.domain.model.sales.order.OrderLine;
import com.example.sms.domain.model.sales.order.OrderList;
import com.example.sms.domain.model.sales.shipping.Shipping;
import com.example.sms.domain.model.sales.shipping.ShippingList;
import com.example.sms.domain.model.sales.shipping.rule.ShippingRuleCheckList;
import com.example.sms.service.sales.order.SalesOrderService;
import com.example.sms.service.sales.shipping.ShippingCriteria;
import com.example.sms.service.sales.shipping.ShippingService;
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
            testDataFactory.setUpForShippingService();
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

                Shipping foundShipping = shippingService.findById(shipping.getOrderNumber().getValue(), shipping.getCustomerOrderNumber()).orElse(null);

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

                Shipping savedShipping = shippingService.findById(shipping.getOrderNumber().getValue(), String.valueOf(shipping.getOrderLineNumber())).orElse(null);

                assertNotNull(savedShipping);
                assertEquals(shipping.getOrderNumber().getValue(), savedShipping.getOrderNumber().getValue());
                assertEquals(shipping.getProductCode().getValue(), savedShipping.getProductCode().getValue());
            }
        }

        @Nested
        @DisplayName("出荷指示")
        class ShippingInstructionTest {

            @Test
            @DisplayName("出荷指示を行う")
            void shouldOrderShipping() {
                ShippingList allShippings = shippingService.selectAll();
                if (!allShippings.asList().isEmpty()) {
                    Shipping shipping = allShippings.asList().getFirst();
                    shippingService.orderShipping(allShippings);

                    Shipping orderedShipping = shippingService.findById(shipping.getOrderNumber().getValue(), shipping.getCustomerOrderNumber()).orElse(null);
                    OrderList orderList = salesOrderService.selectAll();

                    assertNotNull(orderedShipping);
                    assertEquals(CompletionFlag.完了, orderedShipping.getCompletionFlag());
                    for (Order order : orderList.asList()) {
                        for (OrderLine orderLine : order.getOrderLines()) {
                            if (orderLine.getProductCode().getValue().equals(shipping.getProductCode().getValue())) {
                                assertEquals(CompletionFlag.完了, orderLine.getCompletionFlag());
                            }
                        }
                    }
                }
            }
            @Test
            @DisplayName("受注明細の件数は出荷件数と一致する")
            void shouldMatchShippingCountWithOrderDetails() {
                ShippingList allShippings = shippingService.selectAll();
                if (!allShippings.asList().isEmpty()) {
                    Shipping shipping = allShippings.asList().getFirst();
                    shippingService.orderShipping(allShippings);

                    Order order = salesOrderService.find(shipping.getOrderNumber().getValue());
                    assertNotNull(order);
                    assertEquals(allShippings.asList().stream().filter(s -> s.getOrderNumber().getValue().equals(shipping.getOrderNumber().getValue())).count(), order.getOrderLines().size());
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
        @DisplayName("納期を超過している場合")
        void shouldCheckShippingOverdue() {
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
                    CompletionFlag.未完了.getValue(),
                    orderLine.getDiscountAmount().getAmount(),
                    LocalDateTime.now().minus(10, ChronoUnit.DAYS)
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

            ShippingRuleCheckList checkList = shippingService.checkRule();
            assertNotNull(checkList);
            assertTrue(checkList.asList().getFirst().containsValue("納期を超過しています。"));
        }
    }
}
