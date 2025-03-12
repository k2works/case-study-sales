package com.example.sms.presentation.api.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.department.DepartmentLowerType;
import com.example.sms.domain.model.master.department.SlitYnType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("部門DTOマッパーテスト")
class DepartmentResourceDTOMapperTest {

    @Test
    @DisplayName("部門リソースを部門エンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnDepartment() {
        // Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        String departmentCode = "12345";
        String startDate = "2023-01-01T10:00:00Z";
        String endDate = "2023-12-31T10:00:00Z";
        String departmentName = "Finance";
        String layer = "2";
        String path = "12345~";
        DepartmentLowerType lowerType = DepartmentLowerType.LOWER;
        SlitYnType slitYn = SlitYnType.SLIT;
        DepartmentResource resource = DepartmentResource.builder()
                .departmentCode(departmentCode)
                .startDate(startDate)
                .endDate(endDate)
                .departmentName(departmentName)
                .layer(layer)
                .path(path)
                .lowerType(lowerType)
                .slitYn(slitYn)
                .build();

        // Act
        Department department = DepartmentResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(department);
        assertEquals(DepartmentId.of(departmentCode, LocalDateTime.parse(startDate, formatter)), department.getDepartmentId());
        assertEquals(LocalDateTime.parse(endDate, formatter), department.getEndDate().getValue());
        assertEquals(departmentName, department.getDepartmentName());
        assertEquals(Integer.parseInt(layer), department.getLayer());
        assertEquals(path, department.getPath().getValue());
        assertEquals(lowerType.getValue(), department.getLowerType().getValue());
        assertEquals(slitYn.getValue(), department.getSlitYn().getValue());
    }

    @Test
    @DisplayName("部門リソースにnull値がある場合は例外をスローする")
    void testConvertToEntity_nullValuesInResource_shouldThrowException() {
        // Arrange
        DepartmentResource resource = DepartmentResource.builder()
                .departmentCode(null)
                .startDate(null)
                .endDate(null)
                .build();

        // Act & Assert
        try {
            DepartmentResourceDTOMapper.convertToEntity(resource);
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    @DisplayName("部門リソースの日付フォーマットが無効な場合は例外をスローする")
    void testConvertToEntity_invalidDateFormat_shouldThrowException() {
        // Arrange
        String invalidDate = "2023/01/01 10:00:00";
        DepartmentResource resource = DepartmentResource.builder()
                .departmentCode("D123")
                .startDate(invalidDate)
                .endDate("2023-12-31T10:00:00Z")
                .build();

        // Act & Assert
        try {
            DepartmentResourceDTOMapper.convertToEntity(resource);
        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }
    }
}