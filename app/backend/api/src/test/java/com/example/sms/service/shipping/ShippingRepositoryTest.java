package com.example.sms.service.shipping;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales_order.*;
import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.type.money.Money;
import com.example.sms.service.sales_order.SalesOrderRepository;
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

    private SalesOrder getSalesOrder(String orderNumber) {
        return TestDataFactoryImpl.getSalesOrder(orderNumber);
    }

    private SalesOrderLine getSalesOrderLine(String orderNumber, int lineNumber) {
        return TestDataFactoryImpl.getSalesOrderLine(orderNumber, lineNumber);
    }

    private Shipping convertToShipping(SalesOrder salesOrder, SalesOrderLine salesOrderLine) {
        Money salesUnitPrice = salesOrderLine.getSalesUnitPrice();
        SalesAmount salesAmount = SalesAmount.of(salesUnitPrice, salesOrderLine.getOrderQuantity());
        ConsumptionTaxAmount consumptionTaxAmount = ConsumptionTaxAmount.of(salesAmount, salesOrderLine.getTaxRate());

        return Shipping.of(
                salesOrder.getOrderNumber(),
                salesOrder.getOrderDate(),
                salesOrder.getDepartmentCode(),
                salesOrder.getDepartmentStartDate(),
                salesOrder.getCustomerCode(),
                salesOrder.getEmployeeCode(),
                salesOrder.getDesiredDeliveryDate(),
                salesOrder.getCustomerOrderNumber(),
                salesOrder.getWarehouseCode(),
                salesOrder.getTotalOrderAmount(),
                salesOrder.getTotalConsumptionTax(),
                salesOrder.getRemarks(),
                salesOrderLine.getOrderLineNumber(),
                salesOrderLine.getProductCode(),
                salesOrderLine.getProductName(),
                salesOrderLine.getSalesUnitPrice(),
                salesOrderLine.getOrderQuantity(),
                salesOrderLine.getTaxRate(),
                salesOrderLine.getAllocationQuantity(),
                salesOrderLine.getShipmentInstructionQuantity(),
                salesOrderLine.getShippedQuantity(),
                salesOrderLine.getCompletionFlag(),
                salesOrderLine.getDiscountAmount(),
                salesOrderLine.getDeliveryDate(),
                salesOrderLine.getProduct(),
                salesAmount,
                consumptionTaxAmount,
                salesOrder.getDepartment(),
                salesOrder.getCustomer(),
                salesOrder.getEmployee()
        );
    }

    @Nested
    @DisplayName("出荷")
    class ShippingTest {
        @Test
        @DisplayName("出荷一覧を取得できる")
        void shouldRetrieveAllShippings() {
            IntStream.range(0, 10).forEach(i -> {
                SalesOrder order = getSalesOrder(String.format("1%09d", i));
                List<SalesOrderLine> lines = IntStream.range(0, 3)
                        .mapToObj(j -> getSalesOrderLine(order.getOrderNumber().getValue(), j))
                        .toList();
                SalesOrder newOrder = SalesOrder.of(
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
            SalesOrder order = getSalesOrder(String.format("1%09d", 1));
            List<SalesOrderLine> lines = IntStream.range(0, 3)
                    .mapToObj(j -> getSalesOrderLine(order.getOrderNumber().getValue(), j))
                    .toList();
            SalesOrder newOrder = SalesOrder.of(
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
            SalesOrder order = getSalesOrder(String.format("1%09d", 1));
            List<SalesOrderLine> lines = IntStream.range(0, 3)
                    .mapToObj(j -> getSalesOrderLine(order.getOrderNumber().getValue(), j))
                    .toList();
            SalesOrder newOrder = SalesOrder.of(
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