package com.example.sms.domain.model.sales.sales;

import com.example.sms.domain.model.master.product.*;
import com.example.sms.domain.model.sales.order.TaxRateType;
import com.example.sms.domain.type.money.Money;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("売上")
class SalesTest {
    private SalesLine defaultSalesLine;
    private Product defaultProduct;

    @BeforeEach
    void setUp() {
        // テスト共通のデフォルト商品を作成
        defaultProduct = Product.of(
                "P12345",         // 商品コード
                "テスト商品",      // 商品名
                "商品1",          // 商品略称
                "ショウヒンイチ",  // 商品名カナ
                ProductType.その他, // 商品種別
                900,            // 商品標準価格
                810,            // 売上単価
                90,             // 利益額
                TaxType.外税,     // 税種別
                "カテゴリ9",      // カテゴリ
                MiscellaneousType.適用外, // 雑費区分
                StockManagementTargetType.対象, // 在庫管理対象
                StockAllocationType.引当済, // 在庫引当区分
                "009",           // 倉庫コード
                9                // 入荷リードタイム
        );

        // テスト共通のデフォルト売上明細を作成
        defaultSalesLine = new SalesLineBuilder()
                .withProduct(defaultProduct)
                .build();
    }

    // ビルダークラスを静的内部クラスとして定義
    static class SalesLineBuilder {
        private String salesNumber = "SA12345678";
        private int salesLineNumber = 1;
        private String productCode = "P12345";
        private String productName = "テスト商品";
        private int salesUnitPrice = 1000;
        private int salesQuantity = 2;
        private int shippedQuantity = 1;
        private int discountAmount = 200;
        private LocalDateTime billingDate = LocalDateTime.now();
        private String billingNumber = "B12345";
        private int billingDelayType = 1;
        private LocalDateTime createdAt = LocalDateTime.now();
        private Product product;
        private TaxRateType taxRate = TaxRateType.標準税率;

        public SalesLineBuilder() {
            // デフォルト商品の初期化
            this.product = Product.of(
                    productCode,
                    productName,
                    "商品1",
                    "ショウヒンイチ",
                    ProductType.その他,
                    900,
                    810,
                    90,
                    TaxType.外税,
                    "カテゴリ9",
                    MiscellaneousType.適用外,
                    StockManagementTargetType.対象,
                    StockAllocationType.引当済,
                    "009",
                    9
            );
        }

        public SalesLineBuilder withSalesNumber(String salesNumber) {
            this.salesNumber = salesNumber;
            return this;
        }

        public SalesLineBuilder withSalesLineNumber(int salesLineNumber) {
            this.salesLineNumber = salesLineNumber;
            return this;
        }

        public SalesLineBuilder withProductCode(String productCode) {
            this.productCode = productCode;
            return this;
        }

        public SalesLineBuilder withProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public SalesLineBuilder withSalesUnitPrice(int salesUnitPrice) {
            this.salesUnitPrice = salesUnitPrice;
            return this;
        }

        public SalesLineBuilder withSalesQuantity(int salesQuantity) {
            this.salesQuantity = salesQuantity;
            return this;
        }

        public SalesLineBuilder withShippedQuantity(int shippedQuantity) {
            this.shippedQuantity = shippedQuantity;
            return this;
        }

        public SalesLineBuilder withDiscountAmount(int discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public SalesLineBuilder withProduct(Product product) {
            this.product = product;
            this.productCode = product.getProductCode().getValue();
            this.productName = product.getProductName().getProductFormalName();
            return this;
        }

        public SalesLineBuilder withTaxRate(TaxRateType taxRate) {
            this.taxRate = taxRate;
            return this;
        }

        public SalesLine build() {
            return SalesLine.of(
                    salesNumber,
                    salesLineNumber,
                    productCode,
                    productName,
                    salesUnitPrice,
                    salesQuantity,
                    shippedQuantity,
                    discountAmount,
                    billingDate,
                    billingNumber,
                    billingDelayType,
                    createdAt,
                    product,
                    taxRate
            );
        }
    }

    static class SalesBuilder {
        private String salesNumber = "SA12345678";
        private String orderNumber = "OD12345678";
        private LocalDateTime salesDate = LocalDateTime.now();
        private int salesType = 1;
        private String departmentId = "12345";
        private LocalDateTime shippingDate = LocalDateTime.now();
        private String customerCode = "001";
        private int branchNumber = 1;
        private String employeeCode = "EMP001";
        private int voucherNumber = 1;
        private String originalVoucherNumber = "ORG12345";
        private String remarks = "これは備考です";
        private List<SalesLine> salesLines = new ArrayList<>();

        public SalesBuilder withSalesNumber(String salesNumber) {
            this.salesNumber = salesNumber;
            return this;
        }

        public SalesBuilder withOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
            return this;
        }

        public SalesBuilder withSalesLines(List<SalesLine> salesLines) {
            this.salesLines = new ArrayList<>(salesLines);
            return this;
        }

        public SalesBuilder withSalesLine(SalesLine salesLine) {
            this.salesLines.add(salesLine);
            return this;
        }

        public SalesBuilder withRemarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public Sales build() {
            return Sales.of(
                    salesNumber,
                    orderNumber,
                    salesDate,
                    salesType,
                    departmentId,
                    shippingDate,
                    customerCode,
                    branchNumber,
                    employeeCode,
                    voucherNumber,
                    originalVoucherNumber,
                    remarks,
                    salesLines
            );
        }
    }

    @Test
    @DisplayName("売上を作成できる")
    void shouldCreateSales() {
        // 共通のフィクスチャを利用
        Sales sales = new SalesBuilder()
                .withSalesLine(defaultSalesLine)
                .build();

        assertAll(
                () -> assertEquals("SA12345678", sales.getSalesNumber().getValue()),
                () -> assertEquals("OD12345678", sales.getOrderNumber().getValue()),
                () -> assertEquals("12345", sales.getDepartmentId().getDeptCode().getValue()),
                () -> assertEquals("001", sales.getPartnerCode().getValue()),
                () -> assertEquals("EMP001", sales.getEmployeeCode().getValue()),
                () -> assertEquals(1, sales.getSalesType().getCode()),
                () -> assertEquals(1, sales.getVoucherNumber()),
                () -> assertEquals("ORG12345", sales.getOriginalVoucherNumber()),
                () -> assertEquals(2000, sales.getTotalSalesAmount().getAmount()),
                () -> assertEquals(200, sales.getTotalConsumptionTax().getAmount()),
                () -> assertEquals("これは備考です", sales.getRemarks()),
                () -> assertEquals(1, sales.getSalesLines().size())
        );
    }

    @Nested
    @DisplayName("売上明細のテスト")
    class SalesLineTests {
        @Test
        @DisplayName("売上明細を作成できる")
        void shouldCreateSalesLine() {
            SalesLine line = new SalesLineBuilder()
                    .withSalesUnitPrice(1500)
                    .withSalesQuantity(3)
                    .withShippedQuantity(2)
                    .withDiscountAmount(100)
                    .build();

            assertAll(
                    () -> assertEquals("SA12345678", line.getSalesNumber().getValue()),
                    () -> assertEquals(1, line.getSalesLineNumber()),
                    () -> assertEquals("P12345", line.getProductCode().getValue()),
                    () -> assertEquals("テスト商品", line.getProductName()),
                    () -> assertEquals(1500, line.getSalesUnitPrice().getAmount()),
                    () -> assertEquals(3, line.getSalesQuantity().getAmount()),
                    () -> assertEquals(2, line.getShippedQuantity().getAmount()),
                    () -> assertEquals(100, line.getDiscountAmount().getAmount()),
                    () -> assertNotNull(line.getBillingDate()),
                    () -> assertEquals("B12345", line.getBillingNumber().getValue()),
                    () -> assertEquals(1, line.getBillingDelayType().getCode())
            );
        }

        @Test
        @DisplayName("売上金額を計算できる")
        void shouldCalculateSalesAmount() {
            SalesLine line = new SalesLineBuilder()
                    .withSalesUnitPrice(1500)
                    .withSalesQuantity(3)
                    .withDiscountAmount(100)
                    .build();

            Money salesAmount = line.calcSalesAmount();
            assertEquals(4500, salesAmount.getAmount());
        }

        @Test
        @DisplayName("消費税金額を計算できる")
        void shouldCalculateConsumptionTaxAmount() {
            SalesLine line = new SalesLineBuilder()
                    .withSalesUnitPrice(1000)
                    .withSalesQuantity(1)
                    .withDiscountAmount(0)
                    .build();

            Money consumptionTaxAmount = line.calcConsumptionTaxAmount();
            assertEquals(100, consumptionTaxAmount.getAmount());
        }

        @Test
        @DisplayName("売上明細を完了にできる")
        void shouldCompleteSalesLine() {
            SalesLine line = new SalesLineBuilder()
                    .withSalesUnitPrice(1500)
                    .withSalesQuantity(3)
                    .build();

            SalesLine completedLine = SalesLine.complete(line);
            assertNotNull(completedLine);
            assertEquals(line.getSalesNumber(), completedLine.getSalesNumber());
            assertEquals(line.getSalesLineNumber(), completedLine.getSalesLineNumber());
        }
    }

    @Nested
    @DisplayName("売上金額合計のテスト")
    class TotalSalesAmountTests {
        @Test
        @DisplayName("単一明細の売上金額合計を計算できる")
        void shouldCalculateTotalSalesAmountForSingleLine() {
            Sales sales = new SalesBuilder()
                    .withSalesLine(defaultSalesLine)
                    .build();

            assertEquals(2000, sales.getTotalSalesAmount().getAmount());
        }

        @Test
        @DisplayName("複数明細の売上金額合計を計算できる")
        void shouldCalculateTotalSalesAmountForMultipleLines() {
            SalesLine salesLine1 = defaultSalesLine;

            SalesLine salesLine2 = new SalesLineBuilder()
                    .withSalesLineNumber(2)
                    .withProductCode("P67890")
                    .withProductName("テスト商品2")
                    .withSalesUnitPrice(2000)
                    .withSalesQuantity(3)
                    .withDiscountAmount(300)
                    .build();

            Sales sales = new SalesBuilder()
                    .withSalesLines(Arrays.asList(salesLine1, salesLine2))
                    .build();

            assertEquals(800, sales.getTotalConsumptionTax().getAmount());
        }
    }
}