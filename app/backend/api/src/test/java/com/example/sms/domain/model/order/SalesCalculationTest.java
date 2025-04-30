package com.example.sms.domain.model.order;

import com.example.sms.domain.model.master.product.*;
import com.example.sms.domain.model.sales.order.SalesCalculation;
import com.example.sms.domain.model.sales.order.TaxRateType;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("売上金額計算テスト")
class SalesCalculationTest {

    @Nested
    @DisplayName("正常値テスト")
    class Normal {
        @DisplayName("外税で正しい売上金額計算ができること")
        @Test
        void should_calculate_valid_sales_calculation_by_outer_tax() {
            Product product = Product.of(
                    "99999999", // 商品コード
                    "商品1",    // 商品名
                    "商品1",    // 商品名カナ
                    "ショウヒンイチ", // 商品英語名
                    ProductType.その他, // 商品種別
                    900, // 商品標準価格
                    810, // 売上単価
                    90,  // 利益額
                    TaxType.外税, // 税種別
                    "カテゴリ9", // カテゴリ
                    MiscellaneousType.適用外, // 雑費区分
                    StockManagementTargetType.対象, // 在庫管理対象
                    StockAllocationType.引当済, // 在庫引当区分
                    "009", // 倉庫コード
                    9    // 入荷リードタイム
            );
            Money salesUnitPrice = Money.of(100000);
            Quantity salesQuantity = Quantity.of(1);
            TaxRateType taxRateType = TaxRateType.標準税率;
            
            SalesCalculation salesCalculation = SalesCalculation.of(salesUnitPrice, salesQuantity, product, taxRateType);
            
            assertEquals(Money.of(100000), salesCalculation.getSalesAmount().getValue());
            assertEquals(Money.of(10000), salesCalculation.getConsumptionTaxAmount().getValue());
            assertEquals(Money.of(110000), salesCalculation.getSalesAmount().getValue()
                    .plusMoney(salesCalculation.getConsumptionTaxAmount().getValue()));
        }

        @DisplayName("内税で正しい売上金額計算ができること")
        @Test
        void should_calculate_valid_sales_calculation_by_inner_tax() {
            Product product = Product.of(
                    "99999999", // 商品コード
                    "商品1",    // 商品名
                    "商品1",    // 商品名カナ
                    "ショウヒンイチ", // 商品英語名
                    ProductType.その他, // 商品種別
                    900, // 商品標準価格
                    810, // 売上単価
                    90,  // 利益額
                    TaxType.内税, // 税種別
                    "カテゴリ9", // カテゴリ
                    MiscellaneousType.適用外, // 雑費区分
                    StockManagementTargetType.対象, // 在庫管理対象
                    StockAllocationType.引当済, // 在庫引当区分
                    "009", // 倉庫コード
                    9    // 入荷リードタイム
            );
            Money salesUnitPrice = Money.of(100000);
            Quantity salesQuantity = Quantity.of(1);
            TaxRateType taxRateType = TaxRateType.軽減税率;
            
            SalesCalculation salesCalculation = SalesCalculation.of(salesUnitPrice, salesQuantity, product, taxRateType);
            
            assertEquals(Money.of(92593), salesCalculation.getSalesAmount().getValue());
            assertEquals(Money.of(7407), salesCalculation.getConsumptionTaxAmount().getValue());
            assertEquals(Money.of(100000), salesCalculation.getSalesAmount().getValue()
                    .plusMoney(salesCalculation.getConsumptionTaxAmount().getValue()));
        }

        @DisplayName("内税で正しい売上金額計算ができること")
        @Test
        void should_calculate_valid_sales_calculation_by_inner_tax2() {
            Product product = Product.of(
                    "99999999", // 商品コード
                    "商品1",    // 商品名
                    "商品1",    // 商品名カナ
                    "ショウヒンイチ", // 商品英語名
                    ProductType.その他, // 商品種別
                    900, // 商品標準価格
                    810, // 売上単価
                    90,  // 利益額
                    TaxType.内税, // 税種別
                    "カテゴリ9", // カテゴリ
                    MiscellaneousType.適用外, // 雑費区分
                    StockManagementTargetType.対象, // 在庫管理対象
                    StockAllocationType.引当済, // 在庫引当区分
                    "009", // 倉庫コード
                    9    // 入荷リードタイム
            );
            Money salesUnitPrice = Money.of(11000);
            Quantity salesQuantity = Quantity.of(2);
            TaxRateType taxRateType = TaxRateType.標準税率;

            SalesCalculation salesCalculation = SalesCalculation.of(salesUnitPrice, salesQuantity, product, taxRateType);

            assertEquals(Money.of(20000), salesCalculation.getSalesAmount().getValue());
            assertEquals(Money.of(2000), salesCalculation.getConsumptionTaxAmount().getValue());
            assertEquals(Money.of(22000), salesCalculation.getSalesAmount().getValue()
                    .plusMoney(salesCalculation.getConsumptionTaxAmount().getValue()));
        }

        @DisplayName("非課税で正しい売上金額計算ができること")
        @Test
        void should_calculate_valid_sales_calculation_by_tax_free() {
            Product product = Product.of(
                    "99999999", // 商品コード
                    "商品1",    // 商品名
                    "商品1",    // 商品名カナ
                    "ショウヒンイチ", // 商品英語名
                    ProductType.その他, // 商品種別
                    900, // 商品標準価格
                    810, // 売上単価
                    90,  // 利益額
                    TaxType.非課税, // 税種別
                    "カテゴリ9", // カテゴリ
                    MiscellaneousType.適用外, // 雑費区分
                    StockManagementTargetType.対象, // 在庫管理対象
                    StockAllocationType.引当済, // 在庫引当区分
                    "009", // 倉庫コード
                    9    // 入荷リードタイム
            );
            Money salesUnitPrice = Money.of(100000);
            Quantity salesQuantity = Quantity.of(1);
            TaxRateType taxRateType = TaxRateType.標準税率;
            
            SalesCalculation salesCalculation = SalesCalculation.of(salesUnitPrice, salesQuantity, product, taxRateType);
            
            assertEquals(Money.of(100000), salesCalculation.getSalesAmount().getValue());
            assertEquals(Money.of(0), salesCalculation.getConsumptionTaxAmount().getValue());
            assertEquals(Money.of(100000), salesCalculation.getSalesAmount().getValue()
                    .plusMoney(salesCalculation.getConsumptionTaxAmount().getValue()));
        }

        @DisplayName("数量が複数の場合も正しい売上金額計算ができること")
        @TestFactory
        Stream<DynamicTest> should_calculate_valid_sales_calculation_with_multiple_quantity() {
            Product productOuterTax = Product.of(
                    "99999999", "商品1", "商品1", "ショウヒンイチ", ProductType.その他,
                    900, 810, 90, TaxType.外税, "カテゴリ9",
                    MiscellaneousType.適用外, StockManagementTargetType.対象,
                    StockAllocationType.引当済, "009", 9
            );
            
            Product productInnerTax = Product.of(
                    "99999999", "商品1", "商品1", "ショウヒンイチ", ProductType.その他,
                    900, 810, 90, TaxType.内税, "カテゴリ9",
                    MiscellaneousType.適用外, StockManagementTargetType.対象,
                    StockAllocationType.引当済, "009", 9
            );
            
            List<Object[]> validInputs = List.of(
                    new Object[]{
                            Money.of(200000), Money.of(20000), Money.of(100000), Quantity.of(2),
                            productOuterTax, TaxRateType.標準税率
                    },
                    new Object[]{
                            Money.of(185186), Money.of(14814), Money.of(100000), Quantity.of(2),
                            productInnerTax, TaxRateType.軽減税率
                    }
            );
            
            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Valid: ExpectedSalesAmount=" + input[0] + ", ExpectedTaxAmount=" + input[1] + 
                            ", SalesUnitPrice=" + input[2] + ", Quantity=" + input[3] +
                            ", Product=" + ((Product)input[4]).getTaxType() + ", TaxRateType=" + input[5],
                            () -> {
                                SalesCalculation calc = SalesCalculation.of(
                                        (Money) input[2],
                                        (Quantity) input[3],
                                        (Product) input[4],
                                        (TaxRateType) input[5]
                                );
                                assertEquals((Money) input[0], calc.getSalesAmount().getValue());
                                assertEquals((Money) input[1], calc.getConsumptionTaxAmount().getValue());
                                assertEquals(
                                        ((Money) input[0]).plusMoney((Money) input[1]),
                                        calc.getSalesAmount().getValue().plusMoney(calc.getConsumptionTaxAmount().getValue())
                                );
                            }
                    ));
        }
    }

    @Nested
    @DisplayName("境界値テスト")
    class Boundary {
        @DisplayName("境界値での売上金額計算ができること")
        @TestFactory
        Stream<DynamicTest> should_calculate_on_boundary() {
            Product product = Product.of(
                    "99999999", "商品1", "商品1", "ショウヒンイチ", ProductType.その他,
                    900, 810, 90, TaxType.外税, "カテゴリ9",
                    MiscellaneousType.適用外, StockManagementTargetType.対象,
                    StockAllocationType.引当済, "009", 9
            );
            
            List<Object[]> validInputs = List.of(
                    new Object[]{Money.of(0), Quantity.of(1), TaxRateType.標準税率},  // 販売単価0円
                    new Object[]{Money.of(1), Quantity.of(1), TaxRateType.標準税率},  // 最小販売単価
                    new Object[]{Money.of(100000), Quantity.of(1), TaxRateType.軽減税率}  // 高額販売単価
            );
            
            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Boundary Valid: SalesUnitPrice=" + input[0] + ", Quantity=" + input[1] + ", TaxRateType=" + input[2],
                            () -> assertDoesNotThrow(() -> SalesCalculation.of(
                                    (Money) input[0],
                                    (Quantity) input[1],
                                    product,
                                    (TaxRateType) input[2]
                            ))
                    ));
        }
    }

    @Nested
    @DisplayName("異常値テスト")
    class Abnormal {
        @DisplayName("無効な値が拒否されること")
        @TestFactory
        Stream<DynamicTest> should_reject_invalid_values() {
            Product product = Product.of(
                    "99999999", "商品1", "商品1", "ショウヒンイチ", ProductType.その他,
                    900, 810, 90, TaxType.外税, "カテゴリ9",
                    MiscellaneousType.適用外, StockManagementTargetType.対象,
                    StockAllocationType.引当済, "009", 9
            );
            
            List<Object[]> invalidInputs = List.of(
                    new Object[]{null, Quantity.of(1), product, TaxRateType.標準税率},  // 販売単価がnull
                    new Object[]{Money.of(100), null, product, TaxRateType.標準税率},  // 数量がnull
                    new Object[]{Money.of(100), Quantity.of(1), product, null}  // 税率タイプがnull
            );
            
            return invalidInputs.stream()
                    .map(input -> dynamicTest(
                            "Invalid: SalesUnitPrice=" + input[0] + ", Quantity=" + input[1] + 
                            ", Product=" + input[2] + ", TaxRateType=" + input[3],
                            () -> assertThrows(NullPointerException.class, () -> SalesCalculation.of(
                                    (Money) input[0],
                                    (Quantity) input[1],
                                    (Product) input[2],
                                    (TaxRateType) input[3]
                            ))
                    ));
        }
    }
}
