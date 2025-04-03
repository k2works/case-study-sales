package com.example.sms.service.shipping;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales_order.*;
import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.model.shipping.ShippingList;
import com.example.sms.domain.type.money.Money;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private ShippingRepository repository;

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

    private Shipping convertToShipping(SalesOrder salesOrder, SalesOrderLine salesOrderLine) {
        // Calculate SalesAmount and ConsumptionTaxAmount
        Money salesUnitPrice = salesOrderLine.getSalesUnitPrice();
        Money discountAmount = salesOrderLine.getDiscountAmount();
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

    private ShippingList convertToShippingList(List<Shipping> shippings) {
        return new ShippingList(shippings);
    }

    @Nested
    @DisplayName("出荷")
    class ShippingTest {
        @Test
        @DisplayName("出荷一覧を取得できる")
        void shouldRetrieveAllShippings() {
            // This test is currently empty because we need to implement the repository first
            // Once the repository is implemented, we can add the test logic
        }

        @Test
        @DisplayName("出荷を登録できる")
        void shouldRegisterShipping() {
            // This test is currently empty because we need to implement the repository first
            // Once the repository is implemented, we can add the test logic
        }

        @Test
        @DisplayName("出荷を更新できる")
        void shouldUpdateShipping() {
            // This test is currently empty because we need to implement the repository first
            // Once the repository is implemented, we can add the test logic
        }

        @Test
        @DisplayName("出荷を削除できる")
        void shouldDeleteShipping() {
            // This test is currently empty because we need to implement the repository first
            // Once the repository is implemented, we can add the test logic
        }
    }

    @Nested
    @DisplayName("出荷明細")
    class ShippingLineTest {
        @Test
        @DisplayName("出荷明細一覧を取得できる")
        void shouldRetrieveAllShippingLines() {
            // This test is currently empty because we need to implement the repository first
            // Once the repository is implemented, we can add the test logic
        }

        @Test
        @DisplayName("出荷明細を登録できる")
        void shouldRegisterShippingLine() {
            // This test is currently empty because we need to implement the repository first
            // Once the repository is implemented, we can add the test logic
        }

        @Test
        @DisplayName("出荷明細を更新できる")
        void shouldUpdateShippingLine() {
            // This test is currently empty because we need to implement the repository first
            // Once the repository is implemented, we can add the test logic
        }

        @Test
        @DisplayName("出荷明細を削除できる")
        void shouldDeleteShippingLine() {
            // This test is currently empty because we need to implement the repository first
            // Once the repository is implemented, we can add the test logic
        }
    }
}