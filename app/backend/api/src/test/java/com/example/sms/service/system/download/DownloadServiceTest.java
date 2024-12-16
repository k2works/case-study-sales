package com.example.sms.service.system.download;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.system.download.Department;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.infrastructure.datasource.master.department.DepartmentDownloadCSV;
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
}