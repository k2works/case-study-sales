package com.example.sms.service.master.department;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
@DisplayName("部門サービス")
public class DepartmentServiceTest {
    @Autowired
    DepartmentService departmentService;

    @Autowired
    TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForDepartmentService();
    }

    @Nested
    @DisplayName("部門管理")
    class DepartmentManagementTests {
        @Test
        @DisplayName("部門一覧を取得できる")
        void getAllDepartments() {
            DepartmentList result = departmentService.selectAll();

            assertEquals(2, result.asList().size());
        }

        @Test
        @DisplayName("部門を新規登録できる")
        void registerNewDepartment() {
            Department newDepartment = TestDataFactoryImpl.getDepartment("30000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門3");

            departmentService.register(newDepartment);

            DepartmentList result = departmentService.selectAll();
            assertEquals(3, result.asList().size());
            assertEquals(2, result.asList().getFirst().getEmployees().size());
        }

        @Test
        @DisplayName("部門の登録情報を編集できる")
        void editDepartmentDetails() {
            Department department = TestDataFactoryImpl.getDepartment("30000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門3");

            Department updateDepartment = Department.of(department.getDepartmentId(), department.getEndDate().getValue(), "editedDepartmentName", department.getLayer(), department.getPath().getValue(), department.getLowerType().getValue(), department.getSlitYn().getValue());
            departmentService.save(updateDepartment);

            Department result = departmentService.find(department.getDepartmentId());
            assertEquals("editedDepartmentName", result.getDepartmentName());
        }

        @Test
        @DisplayName("部門を削除できる")
        void deleteDepartment() {
            Department department = TestDataFactoryImpl.getDepartment("30000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門3");

            departmentService.delete(department.getDepartmentId());

            DepartmentList result = departmentService.selectAll();
            assertEquals(2, result.asList().size());
        }

        @Test
        @DisplayName("部門を検索できる")
        void findDepartment() {
            Department department = TestDataFactoryImpl.getDepartment("30000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門3");

            departmentService.register(department);

            Department result = departmentService.find(department.getDepartmentId());
            assertEquals(department, result);
        }
    }
}
