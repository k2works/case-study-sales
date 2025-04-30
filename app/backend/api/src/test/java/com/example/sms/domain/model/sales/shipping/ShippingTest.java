package com.example.sms.domain.model.sales.shipping;

import com.example.sms.domain.model.master.department.DepartmentCode;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.model.sales.order.*;
import com.example.sms.domain.model.sales.shipping.Shipping;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("出荷")
class ShippingTest {

    private static @NotNull Shipping getShipping() {
        return Shipping.of(
                OrderNumber.of("OD12345678"),
                OrderDate.of(LocalDateTime.now()),
                DepartmentCode.of("12345"),
                LocalDateTime.now(),
                CustomerCode.of("001", 1),
                EmployeeCode.of("EMP001"),
                DesiredDeliveryDate.of(LocalDateTime.now().plusDays(1)),
                "CUSTORDER123",
                "WH001",
                Money.of(2000),
                Money.of(200),
                "これは備考です",
                1,
                ProductCode.of("P12345"),
                "テスト商品",
                Money.of(1000),
                Quantity.of(2),
                TaxRateType.標準税率,
                Quantity.of(2),
                Quantity.of(2),
                Quantity.of(2),
                Money.of(200),
                DeliveryDate.of(LocalDateTime.now().plusDays(1)),
                null,
                SalesAmount.of(Money.of(1000), Quantity.of(2)),
                ConsumptionTaxAmount.of(SalesAmount.of(Money.of(1000), Quantity.of(2)), TaxRateType.標準税率),
                null,
                null,
                null
        );
    }

    @Test
    @DisplayName("出荷を作成できる")
    void shouldCreateShipping() {
        Shipping shipping = getShipping();

        assertAll(
                () -> assertEquals("OD12345678", shipping.getOrderNumber().getValue()),
                () -> assertEquals("12345", shipping.getDepartmentCode().getValue()),
                () -> assertEquals("001", shipping.getCustomerCode().getCode().getValue()),
                () -> assertEquals(1, shipping.getCustomerCode().getBranchNumber()),
                () -> assertEquals("EMP001", shipping.getEmployeeCode().getValue()),
                () -> assertEquals("CUSTORDER123", shipping.getCustomerOrderNumber()),
                () -> assertEquals("WH001", shipping.getWarehouseCode()),
                () -> assertEquals(2000, shipping.getTotalOrderAmount().getAmount()),
                () -> assertEquals(200, shipping.getTotalConsumptionTax().getAmount()),
                () -> assertEquals("これは備考です", shipping.getRemarks()),
                () -> assertEquals(1, shipping.getOrderLineNumber()),
                () -> assertEquals("P12345", shipping.getProductCode().getValue()),
                () -> assertEquals("テスト商品", shipping.getProductName()),
                () -> assertEquals(1000, shipping.getSalesUnitPrice().getAmount()),
                () -> assertEquals(2, shipping.getOrderQuantity().getAmount()),
                () -> assertEquals(10, shipping.getTaxRate().getRate()),
                () -> assertEquals(2, shipping.getAllocationQuantity().getAmount()),
                () -> assertEquals(2, shipping.getShipmentInstructionQuantity().getAmount()),
                () -> assertEquals(2, shipping.getShippedQuantity().getAmount()),
                () -> assertEquals(1, shipping.getCompletionFlag().getValue()),
                () -> assertEquals(200, shipping.getDiscountAmount().getAmount()),
                () -> assertEquals(2000, shipping.getSalesAmount().getValue().getAmount()),
                () -> assertEquals(200, shipping.getConsumptionTaxAmount().getValue().getAmount())
        );
    }

    @Nested
    @DisplayName("受注番号")
    class OrderNumberTest {
        @Test
        @DisplayName("注文番号は10桁の数字で作成できる")
        void shouldCreateOrderNumber() {
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
    @DisplayName("完了フラグ")
    class CompletionFlagTest {
        @Test
        @DisplayName("完了フラグを作成できる")
        void shouldCreateCompletionFlag() {
            assertEquals(0, CompletionFlag.未完了.getValue());
            assertEquals(1, CompletionFlag.完了.getValue());
        }

        @Test
        @DisplayName("受注数量と出荷済み数量が同じ場合は完了にする")
        void shouldSetCompletionFlagWhenOrderQuantityEqualsShippedQuantity() {
            Shipping shipping = getShipping();

            Shipping actual = Shipping.of(
                    shipping.getOrderNumber(),
                    shipping.getOrderDate(),
                    shipping.getDepartmentCode(),
                    shipping.getDepartmentStartDate(),
                    shipping.getCustomerCode(),
                    shipping.getEmployeeCode(),
                    shipping.getDesiredDeliveryDate(),
                    shipping.getCustomerOrderNumber(),
                    shipping.getWarehouseCode(),
                    shipping.getTotalOrderAmount(),
                    shipping.getTotalConsumptionTax(),
                    shipping.getRemarks(),
                    shipping.getOrderLineNumber(),
                    shipping.getProductCode(),
                    shipping.getProductName(),
                    shipping.getSalesUnitPrice(),
                    Quantity.of(2),
                    shipping.getTaxRate(),
                    shipping.getAllocationQuantity(),
                    Quantity.of(2),
                    Quantity.of(2),
                    shipping.getDiscountAmount(),
                    shipping.getDeliveryDate(),
                    shipping.getProduct(),
                    shipping.getSalesAmount(),
                    shipping.getConsumptionTaxAmount(),
                    shipping.getDepartment(),
                    shipping.getCustomer(),
                    shipping.getEmployee()
            );

            assertEquals(CompletionFlag.完了, actual.getCompletionFlag());
        }

        @Test
        @DisplayName("受注数量と出荷済み数量が異なる場合は完了にしない")
        void shouldNotSetCompletionFlagWhenOrderQuantityNotEqualsShippedQuantity() {
            Shipping shipping = getShipping();

            Shipping actual = Shipping.of(
                    shipping.getOrderNumber(),
                    shipping.getOrderDate(),
                    shipping.getDepartmentCode(),
                    shipping.getDepartmentStartDate(),
                    shipping.getCustomerCode(),
                    shipping.getEmployeeCode(),
                    shipping.getDesiredDeliveryDate(),
                    shipping.getCustomerOrderNumber(),
                    shipping.getWarehouseCode(),
                    shipping.getTotalOrderAmount(),
                    shipping.getTotalConsumptionTax(),
                    shipping.getRemarks(),
                    shipping.getOrderLineNumber(),
                    shipping.getProductCode(),
                    shipping.getProductName(),
                    shipping.getSalesUnitPrice(),
                    Quantity.of(2),
                    shipping.getTaxRate(),
                    shipping.getAllocationQuantity(),
                    Quantity.of(2),
                    Quantity.of(1),
                    shipping.getDiscountAmount(),
                    shipping.getDeliveryDate(),
                    shipping.getProduct(),
                    shipping.getSalesAmount(),
                    shipping.getConsumptionTaxAmount(),
                    shipping.getDepartment(),
                    shipping.getCustomer(),
                    shipping.getEmployee()
            );

            assertEquals(CompletionFlag.未完了, actual.getCompletionFlag());
        }
    }

    @Nested
    @DisplayName("販売価格")
    class SalesAmountTest {
        @Test
        @DisplayName("販売価格を作成できる")
        void shouldCreateSalesAmount() {
            SalesAmount salesAmount = SalesAmount.of(Money.of(1000), Quantity.of(2));
            assertEquals(2000, salesAmount.getValue().getAmount());
        }
    }

    @Nested
    @DisplayName("消費税額")
    class ConsumptionTaxAmountTest {
        @Test
        @DisplayName("消費税額を作成できる")
        void shouldCreateConsumptionTaxAmount() {
            SalesAmount salesAmount = SalesAmount.of(Money.of(1000), Quantity.of(2));
            ConsumptionTaxAmount consumptionTaxAmount = ConsumptionTaxAmount.of(salesAmount, TaxRateType.標準税率);
            assertEquals(200, consumptionTaxAmount.getValue().getAmount());
        }
    }

    @Nested
    @DisplayName("数量")
    class QuantityTest {
        @Test
        @DisplayName("数量を作成できる")
        void shouldCreateQuantity() {
            Quantity quantity = Quantity.of(10);
            assertEquals(10, quantity.getAmount());
            assertThrows(IllegalArgumentException.class, () -> Quantity.of(-1));
        }

        @Test
        @DisplayName("受注数量より出荷指示数量が多い場合はエラー")
        void shouldThrowExceptionWhenShipmentInstructionQuantityIsGreaterThanOrderQuantity() {
            Shipping shipping = getShipping();

            assertThrows(IllegalArgumentException.class, () -> Shipping.of(
                    shipping.getOrderNumber(),
                    shipping.getOrderDate(),
                    shipping.getDepartmentCode(),
                    shipping.getDepartmentStartDate(),
                    shipping.getCustomerCode(),
                    shipping.getEmployeeCode(),
                    shipping.getDesiredDeliveryDate(),
                    shipping.getCustomerOrderNumber(),
                    shipping.getWarehouseCode(),
                    shipping.getTotalOrderAmount(),
                    shipping.getTotalConsumptionTax(),
                    shipping.getRemarks(),
                    shipping.getOrderLineNumber(),
                    shipping.getProductCode(),
                    shipping.getProductName(),
                    shipping.getSalesUnitPrice(),
                    Quantity.of(2),
                    shipping.getTaxRate(),
                    shipping.getAllocationQuantity(),
                    Quantity.of(3),
                    shipping.getShippedQuantity(),
                    shipping.getDiscountAmount(),
                    shipping.getDeliveryDate(),
                    shipping.getProduct(),
                    shipping.getSalesAmount(),
                    shipping.getConsumptionTaxAmount(),
                    shipping.getDepartment(),
                    shipping.getCustomer(),
                    shipping.getEmployee()
            ));


        }
        @Test
        @DisplayName("受注数量より出荷済数量が多い場合はエラー")
        void shouldThrowExceptionWhenShippedQuantityIsGreaterThanOrderQuantity() {
            Shipping shipping = getShipping();

            assertThrows(IllegalArgumentException.class, () -> Shipping.of(
                    shipping.getOrderNumber(),
                    shipping.getOrderDate(),
                    shipping.getDepartmentCode(),
                    shipping.getDepartmentStartDate(),
                    shipping.getCustomerCode(),
                    shipping.getEmployeeCode(),
                    shipping.getDesiredDeliveryDate(),
                    shipping.getCustomerOrderNumber(),
                    shipping.getWarehouseCode(),
                    shipping.getTotalOrderAmount(),
                    shipping.getTotalConsumptionTax(),
                    shipping.getRemarks(),
                    shipping.getOrderLineNumber(),
                    shipping.getProductCode(),
                    shipping.getProductName(),
                    shipping.getSalesUnitPrice(),
                    Quantity.of(2),
                    shipping.getTaxRate(),
                    shipping.getAllocationQuantity(),
                    shipping.getShipmentInstructionQuantity(),
                    Quantity.of(3),
                    shipping.getDiscountAmount(),
                    shipping.getDeliveryDate(),
                    shipping.getProduct(),
                    shipping.getSalesAmount(),
                    shipping.getConsumptionTaxAmount(),
                    shipping.getDepartment(),
                    shipping.getCustomer(),
                    shipping.getEmployee()
            ));
        }
        @Test
        @DisplayName("出荷指示数量より出荷済数量が多い場合はエラー")
        void shouldThrowExceptionWhenShippedQuantityIsGreaterThanShipmentInstructionQuantity() {
            Shipping shipping = getShipping();

            assertThrows(IllegalArgumentException.class, () -> Shipping.of(
                    shipping.getOrderNumber(),
                    shipping.getOrderDate(),
                    shipping.getDepartmentCode(),
                    shipping.getDepartmentStartDate(),
                    shipping.getCustomerCode(),
                    shipping.getEmployeeCode(),
                    shipping.getDesiredDeliveryDate(),
                    shipping.getCustomerOrderNumber(),
                    shipping.getWarehouseCode(),
                    shipping.getTotalOrderAmount(),
                    shipping.getTotalConsumptionTax(),
                    shipping.getRemarks(),
                    shipping.getOrderLineNumber(),
                    shipping.getProductCode(),
                    shipping.getProductName(),
                    shipping.getSalesUnitPrice(),
                    Quantity.of(2),
                    shipping.getTaxRate(),
                    shipping.getAllocationQuantity(),
                    Quantity.of(1),
                    Quantity.of(2),
                    shipping.getDiscountAmount(),
                    shipping.getDeliveryDate(),
                    shipping.getProduct(),
                    shipping.getSalesAmount(),
                    shipping.getConsumptionTaxAmount(),
                    shipping.getDepartment(),
                    shipping.getCustomer(),
                    shipping.getEmployee()
            ));
        }
    }
}
