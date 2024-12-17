package com.example.sms.service.system.download;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.system.download.Department;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.domain.model.system.download.Employee;
import com.example.sms.domain.model.system.download.ProductCategory;
import com.example.sms.infrastructure.datasource.master.department.DepartmentDownloadCSV;
import com.example.sms.infrastructure.datasource.master.employee.EmployeeDownloadCSV;
import com.example.sms.infrastructure.datasource.master.product.ProductCategoryDownloadCSV;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    class DepartmentDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCondition condition = Department.of();
            int result = downloadService.count(condition);

            assertEquals(3, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCondition condition = Department.of();
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
    class EmployeeDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCondition condition = Employee.of();
            int result = downloadService.count(condition);

            assertEquals(3, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCondition condition = Employee.of();
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
    class ProductCategoryDownload {
        @Test
        @DisplayName("件数取得")
        void testCount() {
            DownloadCondition condition = ProductCategory.of();
            int result = downloadService.count(condition);

            assertEquals(3, result);
        }

        @Test
        @DisplayName("データダウンロード変換")
        void testDownload() {
            DownloadCondition condition = ProductCategory.of();
            List<?> rawResult = downloadService.convert(condition);

            List<ProductCategoryDownloadCSV> result = rawResult.stream()
                    .filter(ProductCategoryDownloadCSV.class::isInstance)
                    .map(ProductCategoryDownloadCSV.class::cast)
                    .toList();

            assertFalse(result.isEmpty());
            assertEquals("00000001", result.getFirst().getProductCategoryCode());
            assertEquals("カテゴリ3", result.getFirst().getProductCategoryName());
            assertEquals(1, result.getFirst().getProductCategoryLevel());
            assertEquals("2", result.getFirst().getProductCategoryPath());
            assertEquals(3, result.getFirst().getIsBottomLayer());
        }
    }
}