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
public class DownloadServiceTest {

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

            assertEquals(3, result);
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
            assertEquals(LocalDateTime.of(2021,1,1,0, 0), result.getFirst().getDepartmentStartDate());
            assertEquals(LocalDateTime.of(9999,12,31,0, 0), result.getFirst().getDepartmentEndDate());
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

            assertEquals(3, result);
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
            assertEquals(LocalDateTime.of(2021,1,1,0, 0), result.getFirst().getStartDate());
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
            assertEquals(TaxType.その他.getCode(), result.getFirst().getTaxCategory());
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
            assertEquals(TaxType.その他.getCode(), result.getFirst().getTaxCategory());
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

            assertEquals(3, result);
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
}
