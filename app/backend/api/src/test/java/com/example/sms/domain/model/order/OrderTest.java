package com.example.sms.domain.model.order;

import com.example.sms.domain.type.money.Money;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("受注")
class OrderTest {

    // SalesOrderLineのビルダークラス
    private static class SalesOrderLineBuilder {
        private String orderNumber = "OD12345678";
        private int orderLineNumber = 1;
        private String productCode = "P12345";
        private String productName = "テスト商品";
        private int salesUnitPrice = 1000;
        private int orderQuantity = 2;
        private int taxRate = 10;
        private int allocationQuantity = 2;
        private int shipmentInstructionQuantity = 2;
        private int shippedQuantity = 2;
        private int completionFlag = 0;
        private int discountAmount = 200;
        private LocalDateTime deliveryDate = LocalDateTime.now();

        public SalesOrderLineBuilder withOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
            return this;
        }

        public SalesOrderLineBuilder withOrderLineNumber(int orderLineNumber) {
            this.orderLineNumber = orderLineNumber;
            return this;
        }

        public SalesOrderLineBuilder withProductCode(String productCode) {
            this.productCode = productCode;
            return this;
        }

        public SalesOrderLineBuilder withProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public SalesOrderLineBuilder withSalesUnitPrice(int salesUnitPrice) {
            this.salesUnitPrice = salesUnitPrice;
            return this;
        }

        public SalesOrderLineBuilder withOrderQuantity(int orderQuantity) {
            this.orderQuantity = orderQuantity;
            return this;
        }

        public SalesOrderLineBuilder withTaxRate(int taxRate) {
            this.taxRate = taxRate;
            return this;
        }

        public SalesOrderLineBuilder withAllocationQuantity(int allocationQuantity) {
            this.allocationQuantity = allocationQuantity;
            return this;
        }

        public SalesOrderLineBuilder withShipmentInstructionQuantity(int shipmentInstructionQuantity) {
            this.shipmentInstructionQuantity = shipmentInstructionQuantity;
            return this;
        }

        public SalesOrderLineBuilder withShippedQuantity(int shippedQuantity) {
            this.shippedQuantity = shippedQuantity;
            return this;
        }

        public SalesOrderLineBuilder withCompletionFlag(int completionFlag) {
            this.completionFlag = completionFlag;
            return this;
        }

        public SalesOrderLineBuilder withDiscountAmount(int discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public SalesOrderLineBuilder withDeliveryDate(LocalDateTime deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        public OrderLine build() {
            return OrderLine.of(
                    orderNumber,
                    orderLineNumber,
                    productCode,
                    productName,
                    salesUnitPrice,
                    orderQuantity,
                    taxRate,
                    allocationQuantity,
                    shipmentInstructionQuantity,
                    shippedQuantity,
                    completionFlag,
                    discountAmount,
                    deliveryDate
            );
        }
    }

    // SalesOrderのビルダークラス
    private static class SalesOrderBuilder {
        private String orderNumber = "OD12345678";
        private LocalDateTime orderDate = LocalDateTime.now();
        private String departmentCode = "12345";
        private LocalDateTime desiredDeliveryDate = LocalDateTime.now();
        private String customerCode = "001";
        private int branchNumber = 1;
        private String employeeCode = "EMP001";
        private LocalDateTime deliveryDate = LocalDateTime.now();
        private String customerOrderNumber = "CUSTORDER123";
        private String warehouseCode = "WH001";
        private int totalOrderAmount = 0;
        private int totalConsumptionTax = 0;
        private String remarks = "これは備考です";
        private List<OrderLine> orderLines = new ArrayList<>();

        public SalesOrderBuilder withOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
            return this;
        }

        public SalesOrderBuilder withOrderDate(LocalDateTime orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public SalesOrderBuilder withDepartmentCode(String departmentCode) {
            this.departmentCode = departmentCode;
            return this;
        }

        public SalesOrderBuilder withDesiredDeliveryDate(LocalDateTime desiredDeliveryDate) {
            this.desiredDeliveryDate = desiredDeliveryDate;
            return this;
        }

        public SalesOrderBuilder withCustomerCode(String customerCode) {
            this.customerCode = customerCode;
            return this;
        }

        public SalesOrderBuilder withBranchNumber(int branchNumber) {
            this.branchNumber = branchNumber;
            return this;
        }

        public SalesOrderBuilder withEmployeeCode(String employeeCode) {
            this.employeeCode = employeeCode;
            return this;
        }

        public SalesOrderBuilder withDeliveryDate(LocalDateTime deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        public SalesOrderBuilder withCustomerOrderNumber(String customerOrderNumber) {
            this.customerOrderNumber = customerOrderNumber;
            return this;
        }

        public SalesOrderBuilder withWarehouseCode(String warehouseCode) {
            this.warehouseCode = warehouseCode;
            return this;
        }

        public SalesOrderBuilder withTotalOrderAmount(int totalOrderAmount) {
            this.totalOrderAmount = totalOrderAmount;
            return this;
        }

        public SalesOrderBuilder withTotalConsumptionTax(int totalConsumptionTax) {
            this.totalConsumptionTax = totalConsumptionTax;
            return this;
        }

        public SalesOrderBuilder withRemarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public SalesOrderBuilder withSalesOrderLines(List<OrderLine> orderLines) {
            this.orderLines = orderLines;
            return this;
        }

        public SalesOrderBuilder addSalesOrderLine(OrderLine orderLine) {
            this.orderLines.add(orderLine);
            return this;
        }

        public Order build() {
            return Order.of(
                    orderNumber,
                    orderDate,
                    departmentCode,
                    desiredDeliveryDate,
                    customerCode,
                    branchNumber,
                    employeeCode,
                    deliveryDate,
                    customerOrderNumber,
                    warehouseCode,
                    totalOrderAmount,
                    totalConsumptionTax,
                    remarks,
                    orderLines
            );
        }
    }

    @Test
    @DisplayName("受注を作成できる")
    void shouldCreateSalesOrder() {
        OrderLine orderLine = new SalesOrderLineBuilder()
                .withSalesUnitPrice(1000)
                .withOrderQuantity(2)
                .build();

        Order order = new SalesOrderBuilder()
                .withTotalOrderAmount(2000)
                .withTotalConsumptionTax(200)
                .addSalesOrderLine(orderLine)
                .build();

        assertAll(
                () -> assertEquals("OD12345678", order.getOrderNumber().getValue()),
                () -> assertEquals("12345", order.getDepartmentCode().getValue()),
                () -> assertEquals("001", order.getCustomerCode().getCode().getValue()),
                () -> assertEquals(1, order.getCustomerCode().getBranchNumber()),
                () -> assertEquals("EMP001", order.getEmployeeCode().getValue()),
                () -> assertEquals("CUSTORDER123", order.getCustomerOrderNumber()),
                () -> assertEquals("WH001", order.getWarehouseCode()),
                () -> assertEquals(2000, order.getTotalOrderAmount().getAmount()),
                () -> assertEquals(200, order.getTotalConsumptionTax().getAmount()),
                () -> assertEquals("これは備考です", order.getRemarks()),
                () -> assertEquals(1, order.getOrderLines().size())
        );
    }

    @Test
    @DisplayName("受注日より前に納品希望日を設定できない")
    void shouldNotCreateSalesOrder() {
        assertThrows(IllegalArgumentException.class, () ->
                new SalesOrderBuilder()
                        .withDeliveryDate(LocalDateTime.now().minusDays(1))
                        .build()
        );
    }

    @Nested
    @DisplayName("受注番号")
    class OrderNumberTest {
        @Test
        @DisplayName("注文番号は10桁の数字で作成できる")
        void shouldCreateEmployeeCode() {
            assertDoesNotThrow(() -> OrderNumber.of("OD12345678"));
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.of("ORD1234567"));
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.of("0123456789"));
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.of("12345678901"));
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.of("O12345678901"));
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.of("ORD"));
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.of("1234"));
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.of("123a"));
        }
    }

    @Nested
    @DisplayName("受注日")
    class OrderDateTest {
        @Test
        @DisplayName("受注日を作成できる")
        void shouldCreateOrderDate() {
            OrderDate orderDate = OrderDate.of(LocalDateTime.now());
            assertNotNull(orderDate);
            assertThrows(NullPointerException.class, () -> OrderDate.of(null));
        }
    }

    @Nested
    @DisplayName("希望納期")
    class DesiredDeliveryDateTest {
        @Test
        @DisplayName("希望納期を作成できる")
        void shouldCreateDesiredDeliveryDate() {
            DesiredDeliveryDate desiredDeliveryDate = DesiredDeliveryDate.of(LocalDateTime.now());
            assertNotNull(desiredDeliveryDate);
            assertDoesNotThrow(() -> DesiredDeliveryDate.of(null));
        }
    }

    @Nested
    @DisplayName("受注明細")
    class OrderLineTest {

        @Test
        @DisplayName("受注明細を作成できる")
        void shouldCreateSalesOrderLine() {
            OrderLine line = new SalesOrderLineBuilder()
                    .withSalesUnitPrice(1500)
                    .withOrderQuantity(3)
                    .withTaxRate(8)
                    .withAllocationQuantity(1)
                    .withShipmentInstructionQuantity(0)
                    .withShippedQuantity(0)
                    .withDiscountAmount(100)
                    .build();

            assertAll(
                    () -> assertEquals("OD12345678", line.getOrderNumber().getValue()),
                    () -> assertEquals(1, line.getOrderLineNumber()),
                    () -> assertEquals("P12345", line.getProductCode().getValue()),
                    () -> assertEquals("テスト商品", line.getProductName()),
                    () -> assertEquals(1500, line.getSalesUnitPrice().getAmount()),
                    () -> assertEquals(3, line.getOrderQuantity().getAmount()),
                    () -> assertEquals(8, line.getTaxRate().getRate()),
                    () -> assertEquals(1, line.getAllocationQuantity().getAmount()),
                    () -> assertEquals(0, line.getShipmentInstructionQuantity().getAmount()),
                    () -> assertEquals(0, line.getShippedQuantity().getAmount()),
                    () -> assertEquals(0, line.getCompletionFlag().getValue()),
                    () -> assertEquals(100, line.getDiscountAmount().getAmount())
            );
        }

        @Test
        @DisplayName("受注金額を計算できる")
        void shouldCalculateSalesAmount() {
            OrderLine line = new SalesOrderLineBuilder()
                    .withSalesUnitPrice(1500)
                    .withOrderQuantity(3)
                    .withTaxRate(8)
                    .withAllocationQuantity(1)
                    .withShipmentInstructionQuantity(0)
                    .withShippedQuantity(0)
                    .withDiscountAmount(100)
                    .build();

            Money salesAmount = line.calcSalesAmount();

            assertEquals(4500, salesAmount.getAmount());
        }

        @Test
        @DisplayName("標準税率の消費税金額を計算できる")
        void shouldCalculateConsumptionTaxWithStandardRate() {
            OrderLine line = new SalesOrderLineBuilder()
                    .withSalesUnitPrice(1000)
                    .withOrderQuantity(1)
                    .withTaxRate(TaxRateType.標準税率.getRate())
                    .withAllocationQuantity(1)
                    .withShipmentInstructionQuantity(0)
                    .withShippedQuantity(0)
                    .withDiscountAmount(100)
                    .build();

            Money consumptionTaxAmount = line.calcConsumptionTaxAmount();

            assertEquals(100, consumptionTaxAmount.getAmount());
        }

        @Test
        @DisplayName("軽減税率の消費税金額を計算できる")
        void shouldCalculateConsumptionTaxWithReducedRate() {
            OrderLine line = new SalesOrderLineBuilder()
                    .withSalesUnitPrice(1000)
                    .withOrderQuantity(1)
                    .withTaxRate(TaxRateType.軽減税率.getRate())
                    .withAllocationQuantity(1)
                    .withShipmentInstructionQuantity(0)
                    .withShippedQuantity(0)
                    .withDiscountAmount(100)
                    .build();

            Money consumptionTaxAmount = line.calcConsumptionTaxAmount();

            assertEquals(80, consumptionTaxAmount.getAmount());
        }
    }

    @Test
    @DisplayName("受注明細を完了にできる")
    void shouldCompleteSalesOrderLine() {
        OrderLine line = new SalesOrderLineBuilder()
                .withSalesUnitPrice(1500)
                .withOrderQuantity(3)
                .withTaxRate(8)
                .withAllocationQuantity(1)
                .withShipmentInstructionQuantity(0)
                .withShippedQuantity(0)
                .withDiscountAmount(100)
                .build();

        OrderLine completedLine = OrderLine.complete(line);

        assertEquals(CompletionFlag.完了, completedLine.getCompletionFlag());
    }

    @Nested
    @DisplayName("納期")
    class DeliveryDateTest {
        @Test
        @DisplayName("納期を作成できる")
        void shouldCreateDeliveryDate() {
            DeliveryDate deliveryDate = DeliveryDate.of(LocalDateTime.now());
            assertNotNull(deliveryDate);
            assertThrows(NullPointerException.class, () -> DeliveryDate.of(null));
            assertThrows(IllegalArgumentException.class, () -> new DeliveryDate(LocalDateTime.now().plusYears(1).plusDays(1)));
        }
    }

    @Nested
    @DisplayName("消費税率")
    class TaxRateTypeTest {
        @Test
        @DisplayName("消費税率を作成できる")
        void shouldCreateTaxRateType() {
            assertNotNull(TaxRateType.of(8));
            assertNotNull(TaxRateType.of(10));
            assertNotNull(TaxRateType.of(0));
            assertThrows(IllegalArgumentException.class, () -> TaxRateType.of(100));
            assertThrows(IllegalArgumentException.class, () -> TaxRateType.of(-100));
        }
    }

    @Nested
    @DisplayName("受注金額合計")
    class TotalOrderAmountTest {
        @Test
        @DisplayName("単一受注明細の金額合計を計算できる")
        void shouldCalculateTotalOrderAmountWithSingleLine() {
            OrderLine orderLine = new SalesOrderLineBuilder().build();

            Order order = new SalesOrderBuilder()
                    .addSalesOrderLine(orderLine)
                    .build();

            assertEquals(2000, order.getTotalOrderAmount().getAmount());
        }

        @Test
        @DisplayName("複数受注明細の金額合計を計算できる")
        void shouldCalculateTotalOrderAmountWithMultipleLines() {
            OrderLine orderLine1 = new SalesOrderLineBuilder().build();

            OrderLine orderLine2 = new SalesOrderLineBuilder()
                    .withSalesUnitPrice(2000)
                    .withOrderQuantity(3)
                    .build();

            Order order = new SalesOrderBuilder()
                    .addSalesOrderLine(orderLine1)
                    .addSalesOrderLine(orderLine2)
                    .build();

            assertEquals(8000, order.getTotalOrderAmount().getAmount());
        }
    }

    @Nested
    @DisplayName("消費税合計")
    class TotalConsumptionTaxTest {
        @Test
        @DisplayName("標準税率の消費税合計を計算できる")
        void shouldCalculateTotalConsumptionTaxWithStandardRate() {
            OrderLine orderLine = new SalesOrderLineBuilder()
                    .withTaxRate(TaxRateType.標準税率.getRate())
                    .build();

            Order order = new SalesOrderBuilder()
                    .addSalesOrderLine(orderLine)
                    .build();

            assertEquals(200, order.getTotalConsumptionTax().getAmount());
        }

        @Test
        @DisplayName("複数税率の消費税合計を計算できる")
        void shouldCalculateTotalConsumptionTaxWithMixedRates() {
            OrderLine orderLine1 = new SalesOrderLineBuilder()
                    .withSalesUnitPrice(1000)
                    .withOrderQuantity(2)
                    .withTaxRate(TaxRateType.標準税率.getRate())
                    .build();

            OrderLine orderLine2 = new SalesOrderLineBuilder()
                    .withSalesUnitPrice(100)
                    .withOrderQuantity(10)
                    .withTaxRate(TaxRateType.軽減税率.getRate())
                    .build();

            Order order = new SalesOrderBuilder()
                    .addSalesOrderLine(orderLine1)
                    .addSalesOrderLine(orderLine2)
                    .build();

            assertEquals(280, order.getTotalConsumptionTax().getAmount());
        }
    }
}