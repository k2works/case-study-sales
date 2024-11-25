package com.example.sms.domain.model.master.department;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("部門")
class DepartmentTest {

    @Test
    @DisplayName("部門を作成できる")
    void shouldCreateDepartment() {
        DepartmentId departmentId = DepartmentId.of("10000", LocalDateTime.now());
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        String departmentName = "Test Department";
        int layer = 5;
        String path = "10000~";
        int lowerType = 1;
        int slitYn = 0;
        Department department = Department.of(departmentId, endDate, departmentName, layer, path, lowerType, slitYn);

        assertNotNull(department, "Department creation resulted in Null");
        assertEquals(departmentId, department.getDepartmentId(), "Mismatch in departmentId");
        assertEquals(endDate, department.getEndDate().getValue(), "Mismatch in endDate");
        assertEquals(departmentName, department.getDepartmentName(), "Mismatch in departmentName");
        assertEquals(layer, department.getLayer(), "Mismatch in layer");
        assertEquals(path, department.getPath().getValue(), "Mismatch in path");
        assertEquals(lowerType, department.getLowerType().getValue(), "Mismatch in lowerType");
        assertEquals(slitYn, department.getSlitYn().getValue(), "Mismatch in slitYn");
        assertTrue(department.getEmployees().isEmpty(), "Employees list is not empty");
    }

    @Nested
    @DisplayName("部門ID")
    class DepartmentIdTest {
        @Test
        @DisplayName("部門IDは必須")
        void shouldThrowExceptionWhenDepartmentIdIsNull() {
            assertThrows(IllegalArgumentException.class, () -> Department.of(null, LocalDateTime.now(), "Test Department", 5, "10000~", 1, 0));
        }

        @Test
        @DisplayName("部門IDは部門コードと開始日が必須")
        void shouldThrowExceptionWhenDepartmentIdDoesNotHaveStartDate() {
            assertThrows(IllegalArgumentException.class, () -> DepartmentId.of("10000", null));
            assertThrows(IllegalArgumentException.class, () -> DepartmentId.of(null, LocalDateTime.now()));
            assertThrows(IllegalArgumentException.class, () -> DepartmentId.of(null, null));
        }

        @Test
        @DisplayName("部門コードは5桁の数字である必要がある")
        void shouldThrowExceptionWhenDepartmentCodeIsNotFiveDigits() {
            assertThrows(IllegalArgumentException.class, () -> DepartmentId.of("1000", LocalDateTime.now()));
            assertThrows(IllegalArgumentException.class, () -> DepartmentId.of("100000", LocalDateTime.now()));
            assertThrows(IllegalArgumentException.class, () -> DepartmentId.of("1000a", LocalDateTime.now()));
        }
    }

    @Test
    @DisplayName("終了日は必須")
    void shouldThrowExceptionWhenEndDateIsNull() {
        DepartmentId departmentId = DepartmentId.of("10000", LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> Department.of(departmentId, null, "Test Department", 5, "10000~", 1, 0));
    }

    @Test
    @DisplayName("終了日は開始日より後である必要がある")
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        DepartmentId departmentId = DepartmentId.of("10000", LocalDateTime.now());
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);
        assertThrows(IllegalArgumentException.class, () -> Department.of(departmentId, endDate, "Test Department", 5, "10000~", 1, 0));
    }

    @Nested
    @DisplayName("部門パス")
    class DepartmentPathTest {
        @Test
        @DisplayName("部門パスは必須")
        void shouldThrowExceptionWhenPathIsNull() {
            DepartmentId departmentId = DepartmentId.of("10000", LocalDateTime.now());
            assertThrows(IllegalArgumentException.class, () -> Department.of(departmentId, LocalDateTime.now(), "Test Department", 5, null, 1, 0));
        }

        @Test
        @DisplayName("部門パスは5桁の数字と~で構成されている必要がある")
        void shouldThrowExceptionWhenPathIsInvalid() {
            DepartmentId departmentId = DepartmentId.of("10000", LocalDateTime.now());
            assertDoesNotThrow(() -> Department.of(departmentId, LocalDateTime.now(), "Test Department", 5, "10000~", 1, 0));
            assertDoesNotThrow(() -> Department.of(departmentId, LocalDateTime.now(), "Test Department", 5, "10000~10000~", 1, 0));
            assertDoesNotThrow(() -> Department.of(departmentId, LocalDateTime.now(), "Test Department", 5, "10000~10000~10000~", 1, 0));
            assertDoesNotThrow(() -> Department.of(departmentId, LocalDateTime.now(), "Test Department", 5, "10000~10000~10000~10000~", 1, 0));
            assertDoesNotThrow(() -> Department.of(departmentId, LocalDateTime.now(), "Test Department", 5, "10000~10000~10000~10000~10000~", 1, 0));
        }

        @Test
        @DisplayName("部門パスは5桁の数字と~で構成されている必要がある")
        void shouldThrowExceptionWhenPathIsInvalid2() {
            DepartmentId departmentId = DepartmentId.of("10000", LocalDateTime.now());
            assertThrows(IllegalArgumentException.class, () -> Department.of(departmentId, LocalDateTime.now(), "Test Department", 5, "10000", 1, 0));
            assertThrows(IllegalArgumentException.class, () -> Department.of(departmentId, LocalDateTime.now(), "Test Department", 5, "1000~", 1, 0));
            assertThrows(IllegalArgumentException.class, () -> Department.of(departmentId, LocalDateTime.now(), "Test Department", 5, "100000~10000", 1, 0));
            assertThrows(IllegalArgumentException.class, () -> Department.of(departmentId, LocalDateTime.now(), "Test Department", 5, "1000a~", 1, 0));
        }
    }
}
