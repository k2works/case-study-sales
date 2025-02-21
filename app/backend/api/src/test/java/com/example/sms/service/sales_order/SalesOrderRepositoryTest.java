package com.example.sms.service.sales_order;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;
import com.example.sms.domain.model.sales_order.SalesOrderList;
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
class SalesOrderRepositoryTest {
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

    private SalesOrder getSalesOrder(String orderNumber) {
        return TestDataFactoryImpl.getSalesOrder(orderNumber);
    }

    private SalesOrderLine getSalesOrderLine(String orderNumber, int lineNumber) {
        return TestDataFactoryImpl.getSalesOrderLine(orderNumber, lineNumber);
    }

    @Nested
    @DisplayName("受注")
    class SalesOrderTest {
        @Test
        @DisplayName("受注一覧を取得できる")
        void shouldRetrieveAllSalesOrders() {
            IntStream.range(0, 10).forEach(i -> {
                SalesOrder order = getSalesOrder(String.format("1%09d", i));
                repository.save(order);
            });
            assertEquals(10, repository.selectAll().size());
        }

        @Test
        @DisplayName("受注を登録できる")
        void shouldRegisterSalesOrder() {
            SalesOrder order = getSalesOrder("1000000001");
            repository.save(order);
            SalesOrder actual = repository.findById(order.getOrderNumber().getValue()).get();
            assertEquals(order, actual);
        }

        @Test
        @DisplayName("受注を更新できる")
        void shouldUpdateSalesOrder() {
            SalesOrder order = getSalesOrder("1000000001");
            repository.save(order);
            order = repository.findById(order.getOrderNumber().getValue()).get();
            SalesOrder updatedOrder = SalesOrder.of(
                    order.getOrderNumber().getValue(),
                    order.getOrderDate().plusDays(1),
                    "20000",
                    order.getDepartmentStartDate().plusDays(1),
                    "002",
                    order.getCustomerBranchNumber(),
                    "EMP002",
                    order.getDesiredDeliveryDate().plusDays(3),
                    "002",
                    "002",
                    100000, 8000,
                    "更新後備考",
                    order.getSalesOrderLines());
            repository.save(updatedOrder);

            SalesOrder actual = repository.findById(order.getOrderNumber().getValue()).get();
            assertEquals(updatedOrder, actual);
        }

        @Test
        @DisplayName("受注を削除できる")
        void shouldDeleteSalesOrder() {
            SalesOrder order = getSalesOrder("1000000001");
            repository.save(order);
            repository.delete(order);
            assertEquals(0, repository.selectAll().size());
        }
    }

    @Nested
    @DisplayName("受注明細")
    class SalesOrderLineTest {
        @Test
        @DisplayName("受注明細一覧を取得できる")
        void shouldRetrieveAllSalesOrderLines() {
            SalesOrder order = getSalesOrder("1000000001");
            List<SalesOrderLine> lines = IntStream.range(1, 11)
                    .mapToObj(i -> getSalesOrderLine(order.getOrderNumber().getValue(), i))
                    .toList();
            SalesOrder newOrder = SalesOrder.of(
                    order.getOrderNumber().getValue(),
                    order.getOrderDate(),
                    order.getDepartmentCode(),
                    order.getDepartmentStartDate(),
                    order.getCustomerCode(),
                    order.getCustomerBranchNumber(),
                    order.getEmployeeCode(),
                    order.getDesiredDeliveryDate(),
                    order.getCustomerOrderNumber(),
                    order.getWarehouseCode(),
                    order.getTotalOrderAmount(),
                    order.getTotalConsumptionTax(),
                    order.getRemarks(),
                    lines);
            repository.save(newOrder);
            SalesOrderList actual = repository.selectAll();
            assertEquals(10, actual.asList().getFirst().getSalesOrderLines().size());
        }

        @Test
        @DisplayName("受注明細を登録できる")
        void shouldRegisterSalesOrderLine() {
            SalesOrder order = getSalesOrder("1000000001");
            SalesOrderLine line = getSalesOrderLine(order.getOrderNumber().getValue(), 1);
            SalesOrder newOrder = SalesOrder.of(
                    order.getOrderNumber().getValue(),
                    order.getOrderDate(),
                    order.getDepartmentCode(),
                    order.getDepartmentStartDate(),
                    order.getCustomerCode(),
                    order.getCustomerBranchNumber(),
                    order.getEmployeeCode(),
                    order.getDesiredDeliveryDate(),
                    order.getCustomerOrderNumber(),
                    order.getWarehouseCode(),
                    order.getTotalOrderAmount(),
                    order.getTotalConsumptionTax(),
                    order.getRemarks(),
                    List.of(line));
            repository.save(newOrder);
            SalesOrder actual = repository.findById(order.getOrderNumber().getValue()).get();
            assertEquals(1, actual.getSalesOrderLines().size());
            assertEquals(line, actual.getSalesOrderLines().get(0));
        }

        @Test
        @DisplayName("受注明細を更新できる")
        void shouldUpdateSalesOrderLine() {
            SalesOrder order = getSalesOrder("1000000001");
            SalesOrderLine line = getSalesOrderLine(order.getOrderNumber().getValue(), 1);
            SalesOrder newOrder = SalesOrder.of(
                    order.getOrderNumber().getValue(),
                    order.getOrderDate(),
                    order.getDepartmentCode(),
                    order.getDepartmentStartDate(),
                    order.getCustomerCode(),
                    order.getCustomerBranchNumber(),
                    order.getEmployeeCode(),
                    order.getDesiredDeliveryDate(),
                    order.getCustomerOrderNumber(),
                    order.getWarehouseCode(),
                    order.getTotalOrderAmount(), order.getTotalConsumptionTax(),
                    order.getRemarks(),
                    List.of(line));
            repository.save(newOrder);

            newOrder = repository.findById(order.getOrderNumber().getValue()).get();
            SalesOrderLine updatedLine = SalesOrderLine.of(
                    order.getOrderNumber().getValue(), 1, "更新後商品コード", "更新後商品名",
                    3000, 5, 8, 3, 1, 0, 1, 200, LocalDateTime.of(2021, 1, 3, 0, 0) );
            SalesOrder updatedOrder = SalesOrder.of(
                    newOrder.getOrderNumber().getValue(),
                    newOrder.getOrderDate(),
                    newOrder.getDepartmentCode(),
                    newOrder.getDepartmentStartDate(),
                    newOrder.getCustomerCode(),
                    newOrder.getCustomerBranchNumber(),
                    newOrder.getEmployeeCode(),
                    newOrder.getDesiredDeliveryDate(),
                    newOrder.getCustomerOrderNumber(),
                    newOrder.getWarehouseCode(),
                    newOrder.getTotalOrderAmount(),
                    newOrder.getTotalConsumptionTax(),
                    newOrder.getRemarks(),
                    List.of(updatedLine));
            repository.save(updatedOrder);

            SalesOrder actual = repository.findById(order.getOrderNumber().getValue()).get();
            assertEquals(1, actual.getSalesOrderLines().size());
            assertEquals(updatedLine, actual.getSalesOrderLines().get(0));
        }

        @Test
        @DisplayName("受注明細を削除できる")
        void shouldDeleteSalesOrderLine() {
            SalesOrder order = getSalesOrder("1000000001");
            SalesOrderLine line = getSalesOrderLine(order.getOrderNumber().getValue(), 1);
            SalesOrder newOrder = SalesOrder.of(
                    order.getOrderNumber().getValue(),
                    order.getOrderDate(),
                    order.getDepartmentCode(),
                    order.getDepartmentStartDate(),
                    order.getCustomerCode(),
                    order.getCustomerBranchNumber(),
                    order.getEmployeeCode(),
                    order.getDesiredDeliveryDate(),
                    order.getCustomerOrderNumber(),
                    order.getWarehouseCode(),
                    order.getTotalOrderAmount(),
                    order.getTotalConsumptionTax(),
                    order.getRemarks(),
                    List.of(line));
            repository.save(newOrder);

            Optional<SalesOrder> actual = repository.findById(order.getOrderNumber().getValue());
            assertEquals(1, actual.get().getSalesOrderLines().size());
        }
    }
}