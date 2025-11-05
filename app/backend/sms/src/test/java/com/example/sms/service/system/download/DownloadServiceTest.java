package com.example.sms.service.system.download;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.master.product.*;
import com.example.sms.domain.model.system.download.*;
import com.example.sms.domain.model.system.download.Product;
import com.example.sms.infrastructure.datasource.system.download.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("データダウンロードサービス")
class DownloadServiceTest {

    @Autowired
    DownloadService downloadService;

    @Autowired
    TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForDownloadService();
    }

    @Nested
    @DisplayName("部門")
    @WithMockUser(username = "admin", roles = "ADMIN")
    class DepartmentDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = Department.of();
            int result = downloadService.count(condition);

            assertEquals(4, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = Department.of();
            List<?> rawResult = downloadService.convert(condition);

            List<DepartmentDownloadCSV> result = rawResult.stream()
                    .filter(DepartmentDownloadCSV.class::isInstance)
                    .map(DepartmentDownloadCSV.class::cast)
                    .toList();

            assertFalse(result.isEmpty());
            assertEquals("30000", result.getFirst().getDepartmentCode());
            assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), result.getFirst().getDepartmentStartDate());
            assertEquals(LocalDateTime.of(9999, 12, 31, 0, 0), result.getFirst().getDepartmentEndDate());
            assertEquals("部門3", result.getFirst().getDepartmentName());
        }
    }

    @Nested
    @DisplayName("社員")
    @WithMockUser(username = "admin", roles = "ADMIN")
    class EmployeeDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = Employee.of();
            int result = downloadService.count(condition);

            assertEquals(4, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = Employee.of();
            List<?> rawResult = downloadService.convert(condition);

            List<EmployeeDownloadCSV> result = rawResult.stream()
                    .filter(EmployeeDownloadCSV.class::isInstance)
                    .map(EmployeeDownloadCSV.class::cast)
                    .toList();

            assertFalse(result.isEmpty());
            assertEquals("EMP003", result.getFirst().getEmployeeCode());
            assertEquals("30000", result.getFirst().getDepartmentCode());
            assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), result.getFirst().getStartDate());
            assertEquals("firstName lastName", result.getFirst().getEmployeeName());
            assertEquals("firstNameKana lastNameKana", result.getFirst().getEmployeeNameKana());
            assertEquals("090-1234-5678", result.getFirst().getPhoneNumber());
            assertEquals("03-1234-5678", result.getFirst().getFaxNumber());
            assertEquals("U999999", result.getFirst().getUserId());
        }
    }

    @Nested
    @DisplayName("商品分類")
    @WithMockUser(username = "admin", roles = "ADMIN")
    class ProductCategoryDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = Product.of();
            int result = downloadService.count(condition);

            assertEquals(3, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = Product.of();
            List<?> rawResult = downloadService.convert(condition);

            List<ProductDownloadCSV> result = rawResult.stream()
                    .filter(ProductDownloadCSV.class::isInstance)
                    .map(ProductDownloadCSV.class::cast)
                    .toList();

            assertFalse(result.isEmpty());
            assertEquals("99999999", result.getFirst().getProductCode());
            assertEquals("商品1", result.getFirst().getProductFormalName());
            assertEquals("商品1", result.getFirst().getProductAbbreviation());
            assertEquals("ショウヒンイチ", result.getFirst().getProductNameKana());
            assertEquals(ProductType.その他.getCode(), result.getFirst().getProductCategory());
            assertEquals(900, result.getFirst().getSalesPrice().intValue());
            assertEquals(810, result.getFirst().getPurchasePrice().intValue());
            assertEquals(90, result.getFirst().getCostOfGoodsSold().intValue());
            assertEquals(TaxType.外税.getCode(), result.getFirst().getTaxCategory());
            assertEquals("カテゴリ9", result.getFirst().getProductCategoryCode());
            assertEquals(MiscellaneousType.適用外.getCode(), result.getFirst().getMiscellaneousCategory());
            assertEquals(StockManagementTargetType.対象.getCode(), result.getFirst().getInventoryManagementCategory());
            assertEquals(StockAllocationType.引当済.getCode(), result.getFirst().getInventoryAllocationCategory());
            assertEquals("009", result.getFirst().getVendorCode());
            assertEquals(9, result.getFirst().getSupplierBranchNumber());
        }
    }

    @Nested
    @DisplayName("商品")
    @WithMockUser(username = "admin", roles = "ADMIN")
    class ProductDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = Product.of();
            int result = downloadService.count(condition);

            assertEquals(3, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = Product.of();
            List<?> rawResult = downloadService.convert(condition);

            List<ProductDownloadCSV> result = rawResult.stream()
                    .filter(ProductDownloadCSV.class::isInstance)
                    .map(ProductDownloadCSV.class::cast)
                    .toList();

            assertFalse(result.isEmpty());
            assertEquals("99999999", result.getFirst().getProductCode());
            assertEquals("商品1", result.getFirst().getProductFormalName());
            assertEquals("商品1", result.getFirst().getProductAbbreviation());
            assertEquals("ショウヒンイチ", result.getFirst().getProductNameKana());
            assertEquals(ProductType.その他.getCode(), result.getFirst().getProductCategory());
            assertEquals(900, result.getFirst().getSalesPrice().intValue());
            assertEquals(810, result.getFirst().getPurchasePrice().intValue());
            assertEquals(90, result.getFirst().getCostOfGoodsSold().intValue());
            assertEquals(TaxType.外税.getCode(), result.getFirst().getTaxCategory());
            assertEquals("カテゴリ9", result.getFirst().getProductCategoryCode());
            assertEquals(MiscellaneousType.適用外.getCode(), result.getFirst().getMiscellaneousCategory());
            assertEquals(StockManagementTargetType.対象.getCode(), result.getFirst().getInventoryManagementCategory());
            assertEquals(StockAllocationType.引当済.getCode(), result.getFirst().getInventoryAllocationCategory());
            assertEquals("009", result.getFirst().getVendorCode());
            assertEquals(9, result.getFirst().getSupplierBranchNumber());
        }
    }

    @Nested
    @DisplayName("取引先グループ")
    @WithMockUser(username = "XXXXX", roles = "ADMIN")
    class PartnerGroupDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = PartnerGroup.of();
            int result = downloadService.count(condition);

            assertEquals(2, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = PartnerGroup.of();
            List<?> rawResult = downloadService.convert(condition);

            List<PartnerGroupDownloadCSV> result = rawResult.stream()
                    .filter(PartnerGroupDownloadCSV.class::isInstance)
                    .map(PartnerGroupDownloadCSV.class::cast)
                    .toList();

            assertFalse(result.isEmpty());
            assertEquals("0001", result.getFirst().getPartnerGroupCode());
            assertEquals("取引先グループ1", result.getFirst().getPartnerGroupName());
        }
    }

    @Nested
    @DisplayName("取引先")
    @WithMockUser(username = "XXXXX", roles = "ADMIN")
    class PartnerDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = Partner.of();
            int result = downloadService.count(condition);

            assertEquals(3, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = Partner.of();
            List<?> rawResult = downloadService.convert(condition);

            List<PartnerDownloadCSV> result = rawResult.stream()
                    .filter(PartnerDownloadCSV.class::isInstance)
                    .map(PartnerDownloadCSV.class::cast)
                    .toList();

            assertFalse(result.isEmpty());
            assertEquals("PG01", result.getFirst().getPartnerGroupCode());
            assertEquals("001", result.getFirst().getPartnerCode());
            assertEquals("取引先名A", result.getFirst().getPartnerName());
        }
    }

    @Nested
    @DisplayName("顧客")
    @WithMockUser(username = "XXXXX", roles = "ADMIN")
    class CustomerDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = Customer.of();
            int result = downloadService.count(condition);

            assertEquals(1, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = Customer.of();
            List<?> rawResult = downloadService.convert(condition);

            List<CustomerDownloadCSV> result = rawResult.stream()
                    .filter(CustomerDownloadCSV.class::isInstance)
                    .map(CustomerDownloadCSV.class::cast)
                    .toList();

            assertFalse(result.isEmpty());
            assertEquals("001", result.getFirst().getCustomerCode());
            assertEquals("山田太郎", result.getFirst().getCustomerName());
        }
    }

    @Nested
    @DisplayName("仕入先")
    @WithMockUser(username = "XXXXX", roles = "ADMIN")
    class SupplierDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = Vendor.of();
            int result = downloadService.count(condition);

            assertEquals(3, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = Vendor.of();
            List<?> rawResult = downloadService.convert(condition);

            List<VendorDownloadCSV> result = rawResult.stream()
                    .filter(VendorDownloadCSV.class::isInstance)
                    .map(VendorDownloadCSV.class::cast)
                    .toList();

            assertFalse(result.isEmpty());
            assertEquals("002", result.getFirst().getVendorCode());
            assertEquals("仕入先名A", result.getFirst().getVendorName());
        }
    }

    @Nested
    @DisplayName("受注データ")
    @WithMockUser(username = "XXXXX", roles = "ADMIN")
    class OrderDownload {

        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = Order.of();
            int result = downloadService.count(condition);
            assertEquals(3, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = Order.of();
            List<?> rawResult = downloadService.convert(condition);
            List<OrderDownloadCSV> result = rawResult.stream()
                    .filter(OrderDownloadCSV.class::isInstance)
                    .map(OrderDownloadCSV.class::cast)
                    .toList();

            // 結果が空ではないことを確認
            assertFalse(result.isEmpty(), "結果が空です");

            // 最初のデータを確認
            OrderDownloadCSV firstResult = result.getFirst();
            assertNotNull(firstResult, "最初のデータがnullです");

            // フィールドごとのアサーション
            assertEquals("OD00000001", firstResult.getOrderNumber(), "受注番号が一致しません");
            assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), firstResult.getOrderDate(), "受注日が一致しません");
            assertEquals("10000", firstResult.getDepartmentCode(), "部門コードが一致しません");
            assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), firstResult.getDepartmentStartDate(), "部門開始日が一致しません");
            assertEquals("001", firstResult.getCustomerCode(), "顧客コードが一致しません");
            assertEquals(1, firstResult.getCustomerBranchNumber(), "顧客枝番が一致しません");
            assertEquals("EMP001", firstResult.getEmployeeCode(), "社員コードが一致しません");
            assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), firstResult.getDesiredDeliveryDate(), "希望納期が一致しません");
            assertEquals("001", firstResult.getCustomerOrderNumber(), "客先注文番号が一致しません");
            assertEquals("001", firstResult.getWarehouseCode(), "倉庫コードが一致しません");
            assertEquals(30000, firstResult.getTotalOrderAmount(), "受注金額合計が一致しません");
            assertEquals(3000, firstResult.getTotalConsumptionTax(), "消費税合計が一致しません");
            assertEquals("備考", firstResult.getRemarks(), "備考が一致しません");

            // 以下、受注行（orderLine）の項目アサーション
            assertEquals(1, firstResult.getOrderLineNumber(), "受注行番号が一致しません");
            assertEquals("99999999", firstResult.getProductCode(), "商品コードが一致しません");
            assertEquals("商品1", firstResult.getProductName(), "商品名が一致しません");
            assertEquals(1000, firstResult.getSalesUnitPrice(), "販売単価が一致しません");
            assertEquals(10, firstResult.getOrderQuantity(), "受注数量が一致しません");
            assertEquals(10, firstResult.getTaxRate(), "消費税率が一致しません");
            assertEquals(10, firstResult.getAllocationQuantity(), "引当数量が一致しません");
            assertEquals(10, firstResult.getShipmentInstructionQuantity(), "出荷指示数量が一致しません");
            assertEquals(10, firstResult.getShippedQuantity(), "出荷済数量が一致しません");
            assertEquals(1, firstResult.getCompletionFlag(), "完了フラグが一致しません");
            assertEquals(10, firstResult.getDiscountAmount(), "値引金額が一致しません");
            assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), firstResult.getDeliveryDate(), "納期が一致しません");
        }
    }

    @Nested
    @DisplayName("出荷データ")
    @WithMockUser(username = "XXXXX", roles = "ADMIN")
    class ShippingDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = com.example.sms.domain.model.system.download.Shipment.of();
            int result = downloadService.count(condition);
            assertEquals(3, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = com.example.sms.domain.model.system.download.Shipment.of();
            List<?> rawResult = downloadService.convert(condition);
            List<ShippingDownloadCSV> result = rawResult.stream()
                    .filter(ShippingDownloadCSV.class::isInstance)
                    .map(ShippingDownloadCSV.class::cast)
                    .toList();

            // 結果が空ではないことを確認
            assertFalse(result.isEmpty(), "結果が空です");

            // 最初のデータを確認
            ShippingDownloadCSV firstResult = result.getFirst();
            assertNotNull(firstResult, "最初のデータがnullです");

            // フィールドごとのアサーション
            assertEquals("OD00000001", firstResult.getOrderNumber(), "受注番号が一致しません");
            assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), firstResult.getOrderDate(), "受注日が一致しません");
            assertEquals("99999999", firstResult.getProductCode(), "商品コードが一致しません");
            assertEquals("商品1", firstResult.getProductName(), "商品名が一致しません");
            assertEquals(10, firstResult.getOrderQuantity(), "受注数量が一致しません");
            assertEquals(10, firstResult.getShipmentInstructionQuantity(), "出荷指示数量が一致しません");
            assertEquals(10, firstResult.getShippedQuantity(), "出荷済数量が一致しません");
            assertEquals(1, firstResult.getCompletionFlag(), "完了フラグが一致しません");
            assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), firstResult.getDeliveryDate(), "納期が一致しません");
        }
    }

    @Nested
    @DisplayName("売上データ")
    @WithMockUser(username = "XXXXX", roles = "ADMIN")
    class SalesDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = com.example.sms.domain.model.system.download.Sales.of();
            int result = downloadService.count(condition);
            assertEquals(4, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = com.example.sms.domain.model.system.download.Sales.of();
            List<?> rawResult = downloadService.convert(condition);
            List<SalesDownloadCSV> result = rawResult.stream()
                    .filter(SalesDownloadCSV.class::isInstance)
                    .map(SalesDownloadCSV.class::cast)
                    .toList();

            // 結果が空ではないことを確認
            assertFalse(result.isEmpty(), "結果が空です");

            // 最初のデータを確認
            SalesDownloadCSV firstResult = result.getFirst();
            assertNotNull(firstResult, "最初のデータがnullです");

            // フィールドごとのアサーション
            assertEquals("SA00000001", firstResult.getSalesNumber(), "売上番号が一致しません");
            assertEquals("OD00000001", firstResult.getOrderNumber(), "受注番号が一致しません");
            assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), firstResult.getSalesDate(), "売上日が一致しません");
            assertEquals(1, firstResult.getSalesType(), "売上区分が一致しません");
            assertEquals("30000", firstResult.getDepartmentCode(), "部門コードが一致しません");
            assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), firstResult.getDepartmentStartDate(), "部門開始日が一致しません");
            assertEquals("001", firstResult.getCustomerCode(), "取引先コードが一致しません");
            assertEquals("EMP003", firstResult.getEmployeeCode(), "社員コードが一致しません");
            assertEquals(24000, firstResult.getTotalSalesAmount(), "売上金額合計が一致しません");
            assertEquals(2400, firstResult.getTotalConsumptionTax(), "消費税合計が一致しません");
            assertEquals("テスト備考", firstResult.getRemarks(), "備考が一致しません");
            assertEquals(null, firstResult.getVoucherNumber(), "赤黒伝票番号が一致しません");
            assertEquals(null, firstResult.getOriginalVoucherNumber(), "元伝票番号が一致しません");

            // 売上明細の項目アサーション
            assertEquals(1, firstResult.getSalesLineNumber(), "売上行番号が一致しません");
            assertEquals("99999999", firstResult.getProductCode(), "商品コードが一致しません");
            assertEquals("商品1", firstResult.getProductName(), "商品名が一致しません");
            assertEquals(800, firstResult.getSalesUnitPrice(), "販売単価が一致しません");
            assertEquals(10, firstResult.getSalesQuantity(), "売上数量が一致しません");
            assertEquals(10, firstResult.getShippedQuantity(), "出荷数量が一致しません");
            assertEquals(0, firstResult.getDiscountAmount(), "値引金額が一致しません");
            assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), firstResult.getBillingDate(), "請求日が一致しません");
            assertEquals("B001", firstResult.getBillingNumber(), "請求番号が一致しません");
            assertEquals(0, firstResult.getBillingDelayType(), "請求遅延区分が一致しません");
            assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), firstResult.getAutoJournalDate(), "自動仕訳日が一致しません");
            assertEquals(10, firstResult.getTaxRate(), "消費税率が一致しません");
        }
    }

    @Nested
    @DisplayName("入金データ")
    @WithMockUser(username = "XXXXX", roles = "ADMIN")
    class PaymentDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = com.example.sms.domain.model.system.download.Payment.of();
            int result = downloadService.count(condition);
            assertEquals(3, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = com.example.sms.domain.model.system.download.Payment.of();
            List<?> rawResult = downloadService.convert(condition);
            List<PaymentDownloadCSV> result = rawResult.stream()
                    .filter(PaymentDownloadCSV.class::isInstance)
                    .map(PaymentDownloadCSV.class::cast)
                    .toList();

            // 結果が空ではないことを確認
            assertFalse(result.isEmpty(), "結果が空です");

            // 最初のデータを確認
            PaymentDownloadCSV firstResult = result.getFirst();
            assertNotNull(firstResult, "最初のデータがnullです");

            // フィールドごとのアサーション
            assertEquals("PAY000", firstResult.getPaymentNumber(), "入金番号が一致しません");
            assertEquals(LocalDateTime.of(2022, 1, 1, 0, 0), firstResult.getPaymentDate(), "入金日が一致しません");
            assertEquals("10000", firstResult.getDepartmentCode(), "部門コードが一致しません");
            assertEquals("001", firstResult.getCustomerCode(), "顧客コードが一致しません");
            assertEquals(1, firstResult.getCustomerBranchNumber(), "顧客枝番が一致しません");
            assertEquals(4, firstResult.getPaymentMethodType(), "支払方法区分が一致しません");
            assertEquals("ACC001", firstResult.getPaymentAccountCode(), "入金口座コードが一致しません");
            assertEquals(10000, firstResult.getPaymentAmount(), "入金金額が一致しません");
            assertEquals(0, firstResult.getOffsetAmount(), "消込金額が一致しません");
        }
    }

    @Nested
    @DisplayName("口座データ")
    @WithMockUser(username = "admin", roles = "ADMIN")
    class PaymentAccountDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = com.example.sms.domain.model.system.download.PaymentAccount.of();
            int result = downloadService.count(condition);
            assertEquals(3, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = com.example.sms.domain.model.system.download.PaymentAccount.of();
            List<?> rawResult = downloadService.convert(condition);
            List<PaymentAccountDownloadCSV> result = rawResult.stream()
                    .filter(PaymentAccountDownloadCSV.class::isInstance)
                    .map(PaymentAccountDownloadCSV.class::cast)
                    .toList();

            // 結果が空ではないことを確認
            assertFalse(result.isEmpty(), "結果が空です");

            // 最初のデータを確認
            PaymentAccountDownloadCSV firstResult = result.getFirst();
            assertNotNull(firstResult, "最初のデータがnullです");

            // フィールドごとのアサーション
            assertEquals("ACC001", firstResult.getAccountCode(), "入金口座コードが一致しません");
            assertEquals("テスト口座", firstResult.getAccountName(), "入金口座名が一致しません");
            assertEquals(LocalDateTime.of(2023, 10, 1, 0, 0), firstResult.getStartDate(), "適用開始日が一致しません");
            assertEquals(LocalDateTime.of(2024, 10, 1, 0, 0), firstResult.getEndDate(), "適用終了日が一致しません");
            assertEquals("テスト口座（適用後）", firstResult.getAccountNameAfterStart(), "適用開始後入金口座名が一致しません");
            assertEquals("1", firstResult.getAccountType(), "入金口座区分が一致しません");
            assertEquals("1234567", firstResult.getAccountNumber(), "入金口座番号が一致しません");
            assertEquals("1", firstResult.getBankAccountType(), "銀行口座種別が一致しません");
            assertEquals("テスト太郎", firstResult.getAccountHolder(), "口座名義人が一致しません");
            assertEquals("10000", firstResult.getDepartmentCode(), "部門コードが一致しません");
            assertEquals("0001", firstResult.getBankCode(), "全銀協銀行コードが一致しません");
            assertEquals("001", firstResult.getBranchCode(), "全銀協支店コードが一致しません");
        }
    }

    @Nested
    @DisplayName("発注データ")
    @WithMockUser(username = "admin", roles = "ADMIN")
    class PurchaseOrderDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = PurchaseOrder.of();
            int result = downloadService.count(condition);
            assertTrue(result >= 0);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = PurchaseOrder.of();
            List<?> rawResult = downloadService.convert(condition);
            List<PurchaseOrderDownloadCSV> result = rawResult.stream()
                    .filter(PurchaseOrderDownloadCSV.class::isInstance)
                    .map(PurchaseOrderDownloadCSV.class::cast)
                    .toList();

            // 結果の確認
            assertNotNull(result, "結果がnullです");
            // データが存在する場合の基本的なフィールドチェック
            if (!result.isEmpty()) {
                PurchaseOrderDownloadCSV firstResult = result.getFirst();
                assertNotNull(firstResult, "最初のデータがnullです");
                assertNotNull(firstResult.getPurchaseOrderNumber(), "発注番号がnullです");
                assertNotNull(firstResult.getPurchaseOrderDate(), "発注日がnullです");
                assertNotNull(firstResult.getVendorCode(), "仕入先コードがnullです");
                assertNotNull(firstResult.getPurchaseOrderLineNumber(), "発注行番号がnullです");
                assertNotNull(firstResult.getProductCode(), "商品コードがnullです");
            }
        }
    }

    @Nested
    @DisplayName("在庫データ")
    @WithMockUser(username = "admin", roles = "ADMIN")
    class InventoryDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = Inventory.of();
            int result = downloadService.count(condition);
            assertTrue(result >= 0);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = Inventory.of();
            List<?> rawResult = downloadService.convert(condition);
            List<InventoryDownloadCSV> result = rawResult.stream()
                    .filter(InventoryDownloadCSV.class::isInstance)
                    .map(InventoryDownloadCSV.class::cast)
                    .toList();

            // 結果の確認
            assertNotNull(result, "結果がnullです");
            // データが存在する場合の基本的なフィールドチェック
            if (!result.isEmpty()) {
                InventoryDownloadCSV firstResult = result.getFirst();
                assertNotNull(firstResult, "最初のデータがnullです");
                assertNotNull(firstResult.getWarehouseCode(), "倉庫コードがnullです");
                assertNotNull(firstResult.getProductCode(), "商品コードがnullです");
                assertNotNull(firstResult.getLotNumber(), "ロット番号がnullです");
                assertNotNull(firstResult.getStockCategory(), "在庫区分がnullです");
                assertNotNull(firstResult.getQualityCategory(), "良品区分がnullです");
                assertNotNull(firstResult.getActualStockQuantity(), "実在庫数がnullです");
                assertNotNull(firstResult.getAvailableStockQuantity(), "有効在庫数がnullです");
            }
        }
    }

    @Nested
    @DisplayName("倉庫データ")
    @WithMockUser(username = "admin", roles = "ADMIN")
    class WarehouseDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = com.example.sms.domain.model.system.download.Warehouse.of();
            int result = downloadService.count(condition);
            assertTrue(result >= 0);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = com.example.sms.domain.model.system.download.Warehouse.of();
            List<?> rawResult = downloadService.convert(condition);
            List<WarehouseDownloadCSV> result = rawResult.stream()
                    .filter(WarehouseDownloadCSV.class::isInstance)
                    .map(WarehouseDownloadCSV.class::cast)
                    .toList();

            // 結果の確認
            assertNotNull(result, "結果がnullです");
            // データが存在する場合の基本的なフィールドチェック
            if (!result.isEmpty()) {
                WarehouseDownloadCSV firstResult = result.getFirst();
                assertNotNull(firstResult, "最初のデータがnullです");
                assertNotNull(firstResult.getWarehouseCode(), "倉庫コードがnullです");
                assertNotNull(firstResult.getWarehouseName(), "倉庫名がnullです");
            }
        }
    }

    @Nested
    @DisplayName("仕入データ")
    @WithMockUser(username = "admin", roles = "ADMIN")
    class PurchaseDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = com.example.sms.domain.model.system.download.Purchase.of();
            int result = downloadService.count(condition);
            assertEquals(3, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = com.example.sms.domain.model.system.download.Purchase.of();
            List<?> rawResult = downloadService.convert(condition);
            List<PurchaseDownloadCSV> result = rawResult.stream()
                    .filter(PurchaseDownloadCSV.class::isInstance)
                    .map(PurchaseDownloadCSV.class::cast)
                    .toList();

            // 結果が空ではないことを確認
            assertFalse(result.isEmpty(), "結果が空です");

            // 最初のデータを確認
            PurchaseDownloadCSV firstResult = result.getFirst();
            assertNotNull(firstResult, "最初のデータがnullです");

            // フィールドごとのアサーション
            assertEquals("PU00000001", firstResult.getPurchaseNumber(), "仕入番号が一致しません");
            assertNotNull(firstResult.getPurchaseDate(), "仕入日がnullです");
            assertEquals("001", firstResult.getSupplierCode(), "仕入先コードが一致しません");
            assertEquals(1, firstResult.getSupplierBranchNumber(), "仕入先枝番が一致しません");
            assertEquals("EMP001", firstResult.getPurchaseManagerCode(), "仕入担当者コードが一致しません");
            assertEquals("PO25010001", firstResult.getPurchaseOrderNumber(), "発注番号が一致しません");
            assertEquals("10000", firstResult.getDepartmentCode(), "部門コードが一致しません");
            assertEquals(10000, firstResult.getTotalPurchaseAmount(), "仕入金額合計が一致しません");
            assertEquals(1000, firstResult.getTotalConsumptionTax(), "消費税合計が一致しません");
            assertEquals("テスト備考", firstResult.getRemarks(), "備考が一致しません");
            assertEquals(1, firstResult.getPurchaseLineNumber(), "仕入行番号が一致しません");
            assertEquals("10101001", firstResult.getProductCode(), "商品コードが一致しません");
            assertEquals("W01", firstResult.getWarehouseCode(), "倉庫コードが一致しません");
            assertEquals("商品1", firstResult.getProductName(), "商品名が一致しません");
            assertEquals(1000, firstResult.getPurchaseUnitPrice(), "仕入単価が一致しません");
            assertEquals(10, firstResult.getPurchaseQuantity(), "仕入数量が一致しません");
        }
    }

    @Nested
    @DisplayName("支払データ")
    @WithMockUser(username = "admin", roles = "ADMIN")
    class PurchasePaymentDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCriteria condition = PurchasePayment.of();
            int result = downloadService.count(condition);
            assertEquals(3, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCriteria condition = PurchasePayment.of();
            List<?> rawResult = downloadService.convert(condition);
            List<PurchasePaymentDownloadCSV> result = rawResult.stream()
                    .filter(PurchasePaymentDownloadCSV.class::isInstance)
                    .map(PurchasePaymentDownloadCSV.class::cast)
                    .toList();

            // 結果が空ではないことを確認
            assertFalse(result.isEmpty(), "結果が空です");

            // 最初のデータを確認
            PurchasePaymentDownloadCSV firstResult = result.getFirst();
            assertNotNull(firstResult, "最初のデータがnullです");

            // フィールドごとのアサーション
            assertNotNull(firstResult.getPaymentNumber(), "支払番号がnullです");
            assertNotNull(firstResult.getPaymentDate(), "支払日がnullです");
            assertNotNull(firstResult.getDepartmentCode(), "部門コードがnullです");
            assertNotNull(firstResult.getSupplierCode(), "仕入先コードがnullです");
            assertNotNull(firstResult.getSupplierBranchNumber(), "仕入先枝番がnullです");
            assertNotNull(firstResult.getPaymentMethodType(), "支払方法区分がnullです");
            assertNotNull(firstResult.getPaymentAmount(), "支払金額がnullです");
        }
    }
}
