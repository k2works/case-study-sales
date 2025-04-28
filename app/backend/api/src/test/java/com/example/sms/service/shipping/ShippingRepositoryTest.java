package com.example.sms.service.shipping;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.order.*;
import com.example.sms.domain.model.order.Order;
import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.type.money.Money;
import com.example.sms.service.order.SalesOrderRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("出荷レポジトリ")
class ShippingRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                    .withUsername("root")
                    .withPassword("password")
                    .withDatabaseName("postgres");

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private ShippingRepository shippingRepository;

    @BeforeEach
    void setUp() {
        salesOrderRepository.deleteAll();
    }

    private Order getSalesOrder(String orderNumber) {
        return TestDataFactoryImpl.getSalesOrder(orderNumber);
    }

    private OrderLine getSalesOrderLine(String orderNumber, int lineNumber) {
        return TestDataFactoryImpl.getSalesOrderLine(orderNumber, lineNumber);
    }

    private Shipping convertToShipping(Order order, OrderLine orderLine) {
        Money salesUnitPrice = orderLine.getSalesUnitPrice();
        SalesAmount salesAmount = SalesAmount.of(salesUnitPrice, orderLine.getOrderQuantity());
        ConsumptionTaxAmount consumptionTaxAmount = ConsumptionTaxAmount.of(salesAmount, orderLine.getTaxRate());

        return Shipping.of(
                order.getOrderNumber(),
                order.getOrderDate(),
                order.getDepartmentCode(),
                order.getDepartmentStartDate(),
                order.getCustomerCode(),
                order.getEmployeeCode(),
                order.getDesiredDeliveryDate(),
                order.getCustomerOrderNumber(),
                order.getWarehouseCode(),
                order.getTotalOrderAmount(),
                order.getTotalConsumptionTax(),
                order.getRemarks(),
                orderLine.getOrderLineNumber(),
                orderLine.getProductCode(),
                orderLine.getProductName(),
                orderLine.getSalesUnitPrice(),
                orderLine.getOrderQuantity(),
                orderLine.getTaxRate(),
                orderLine.getAllocationQuantity(),
                orderLine.getShipmentInstructionQuantity(),
                orderLine.getShippedQuantity(),
                orderLine.getDiscountAmount(),
                orderLine.getDeliveryDate(),
                orderLine.getProduct(),
                salesAmount,
                consumptionTaxAmount,
                order.getDepartment(),
                order.getCustomer(),
                order.getEmployee()
        );
    }

    @Nested
    @DisplayName("出荷")
    class ShippingTest {
        @Test
        @DisplayName("出荷一覧を取得できる")
        void shouldRetrieveAllShippings() {
            IntStream.range(0, 10).forEach(i -> {
                Order order = getSalesOrder(String.format("OD%08d", i));
                List<OrderLine> lines = IntStream.range(0, 3)
                        .mapToObj(j -> getSalesOrderLine(order.getOrderNumber().getValue(), j))
                        .toList();
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
                        lines);
                salesOrderRepository.save(newOrder);
            });

            assertEquals(30, shippingRepository.selectAll().size());
        }

        @Test
        @DisplayName("出荷を登録できる")
        void shouldRegisterShipping() {
            Order order = getSalesOrder(String.format("OD%08d", 1));
            List<OrderLine> lines = IntStream.range(0, 3)
                    .mapToObj(j -> getSalesOrderLine(order.getOrderNumber().getValue(), j))
                    .toList();
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
                    lines);

            Shipping shipping = convertToShipping(newOrder, lines.getFirst());
            shippingRepository.save(shipping);

            Shipping actual = shippingRepository.findById(order.getOrderNumber().getValue(), 0).orElse(new Shipping());
            assertEquals(shipping, actual);
        }

        @Test
        @DisplayName("出荷を更新できる")
        void shouldUpdateShipping() {
            Order order = getSalesOrder(String.format("OD%08d", 1));
            List<OrderLine> lines = IntStream.range(0, 3)
                    .mapToObj(j -> getSalesOrderLine(order.getOrderNumber().getValue(), j))
                    .toList();
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
                    lines);
            salesOrderRepository.save(newOrder);

            Shipping shipping = convertToShipping(newOrder, lines.getFirst());
            shippingRepository.save(shipping);

            Shipping actual = shippingRepository.findById(order.getOrderNumber().getValue(), 0).orElse(new Shipping());
            assertEquals(shipping, actual);
        }
    }
}