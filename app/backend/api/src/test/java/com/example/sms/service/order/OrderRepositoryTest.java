package com.example.sms.service.order;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.order.Order;
import com.example.sms.domain.model.order.OrderLine;
import com.example.sms.domain.model.order.OrderList;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("受注レポジトリ")
class OrderRepositoryTest {
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
    private SalesOrderRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private Order getSalesOrder(String orderNumber) {
        return TestDataFactoryImpl.getSalesOrder(orderNumber);
    }

    private OrderLine getSalesOrderLine(String orderNumber, int lineNumber) {
        return TestDataFactoryImpl.getSalesOrderLine(orderNumber, lineNumber);
    }

    @Nested
    @DisplayName("受注")
    class OrderTest {
        @Test
        @DisplayName("受注一覧を取得できる")
        void shouldRetrieveAllSalesOrders() {
            IntStream.range(0, 10).forEach(i -> {
                Order order = getSalesOrder(String.format("OD%08d", i));
                repository.save(order);
            });
            assertEquals(10, repository.selectAll().size());
        }

        @Test
        @DisplayName("受注を登録できる")
        void shouldRegisterSalesOrder() {
            Order order = getSalesOrder("OD00000001");
            repository.save(order);
            Order actual = repository.findById(order.getOrderNumber().getValue()).get();
            assertEquals(order, actual);
        }

        @Test
        @DisplayName("受注を更新できる")
        void shouldUpdateSalesOrder() {
            Order order = getSalesOrder("OD00000001");
            repository.save(order);
            order = repository.findById(order.getOrderNumber().getValue()).get();
            Order updatedOrder = Order.of(
                    order.getOrderNumber().getValue(),
                    order.getOrderDate().getValue().plusDays(1),
                    "20000",
                    order.getDepartmentStartDate().plusDays(1),
                    "002",
                    order.getCustomerCode().getBranchNumber(),
                    "EMP002",
                    order.getDesiredDeliveryDate().getValue().plusDays(3),
                    "002",
                    "002",
                    100000, 8000,
                    "更新後備考",
                    order.getOrderLines());
            repository.save(updatedOrder);

            Order actual = repository.findById(order.getOrderNumber().getValue()).get();
            assertEquals(updatedOrder, actual);
        }

        @Test
        @DisplayName("受注を削除できる")
        void shouldDeleteSalesOrder() {
            Order order = getSalesOrder("OD00000001");
            repository.save(order);
            repository.delete(order);
            assertEquals(0, repository.selectAll().size());
        }
    }

    @Nested
    @DisplayName("受注明細")
    class OrderLineTest {
        @Test
        @DisplayName("受注明細一覧を取得できる")
        void shouldRetrieveAllSalesOrderLines() {
            Order order = getSalesOrder("OD00000001");
            List<OrderLine> lines = IntStream.range(1, 11)
                    .mapToObj(i -> getSalesOrderLine(order.getOrderNumber().getValue(), i))
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
            repository.save(newOrder);
            OrderList actual = repository.selectAll();
            assertEquals(10, actual.asList().getFirst().getOrderLines().size());
        }

        @Test
        @DisplayName("受注明細を登録できる")
        void shouldRegisterSalesOrderLine() {
            Order order = getSalesOrder("OD00000001");
            OrderLine line = getSalesOrderLine(order.getOrderNumber().getValue(), 1);
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
                    List.of(line));
            repository.save(newOrder);
            Order actual = repository.findById(order.getOrderNumber().getValue()).get();
            assertEquals(1, actual.getOrderLines().size());
            assertEquals(line, actual.getOrderLines().getFirst());
        }

        @Test
        @DisplayName("受注明細を更新できる")
        void shouldUpdateSalesOrderLine() {
            Order order = getSalesOrder("OD00000001");
            OrderLine line = getSalesOrderLine(order.getOrderNumber().getValue(), 1);
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
                    List.of(line));
            repository.save(newOrder);

            newOrder = repository.findById(order.getOrderNumber().getValue()).get();
            OrderLine updatedLine = OrderLine.of(
                    order.getOrderNumber().getValue(), 1, "99999999", "更新後商品名",
                    3000, 5, 8, 3, 1, 0, 1, 200, LocalDateTime.of(2021, 1, 3, 0, 0) );
            Order updatedOrder = Order.of(
                    newOrder.getOrderNumber().getValue(),
                    newOrder.getOrderDate().getValue(),
                    newOrder.getDepartmentCode().getValue(),
                    newOrder.getDepartmentStartDate(),
                    newOrder.getCustomerCode().getCode().getValue(),
                    newOrder.getCustomerCode().getBranchNumber(),
                    newOrder.getEmployeeCode().getValue(),
                    newOrder.getDesiredDeliveryDate().getValue(),
                    newOrder.getCustomerOrderNumber(),
                    newOrder.getWarehouseCode(),
                    newOrder.getTotalOrderAmount().getAmount(),
                    newOrder.getTotalConsumptionTax().getAmount(),
                    newOrder.getRemarks(),
                    List.of(updatedLine));
            repository.save(updatedOrder);

            Order actual = repository.findById(order.getOrderNumber().getValue()).get();
            assertEquals(1, actual.getOrderLines().size());
            assertEquals(updatedLine, actual.getOrderLines().getFirst());
        }

        @Test
        @DisplayName("受注明細を削除できる")
        void shouldDeleteSalesOrderLine() {
            Order order = getSalesOrder("OD00000001");
            OrderLine line = getSalesOrderLine(order.getOrderNumber().getValue(), 1);
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
                    List.of(line));
            repository.save(newOrder);

            Optional<Order> actual = repository.findById(order.getOrderNumber().getValue());
            assertEquals(1, actual.get().getOrderLines().size());
        }
    }
}