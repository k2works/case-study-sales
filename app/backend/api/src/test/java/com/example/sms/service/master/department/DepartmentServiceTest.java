package com.example.sms.service.master.department;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.department.DepartmentList;
import com.github.pagehelper.PageInfo;
import io.cucumber.java8.De;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
            Department newDepartment = testDataFactory.Department();

            departmentService.register(newDepartment);

            DepartmentList result = departmentService.selectAll();
            assertEquals(3, result.asList().size());
            assertEquals(2, result.asList().getFirst().getEmployees().size());
        }

        @Test
        @DisplayName("部門の登録情報を編集できる")
        void editDepartmentDetails() {
            Department department = testDataFactory.Department();

            Department updateDepartment = Department.of(department.getDepartmentId(), department.getEndDate().getValue(), "editedDepartmentName", department.getLayer(), department.getPath().getValue(), department.getLowerType().getValue(), department.getSlitYn().getValue());
            departmentService.save(updateDepartment);

            Department result = departmentService.find(department.getDepartmentId());
            assertEquals("editedDepartmentName", result.getDepartmentName());
        }

        @Test
        @DisplayName("部門を削除できる")
        void deleteDepartment() {
            Department department = testDataFactory.Department();

            departmentService.delete(department.getDepartmentId());

            DepartmentList result = departmentService.selectAll();
            assertEquals(2, result.asList().size());
        }

        @Test
        @DisplayName("部門を検索できる")
        void findDepartment() {
            Department department = testDataFactory.Department();

            departmentService.register(department);

            Department result = departmentService.find(department.getDepartmentId());
            assertEquals(department, result);
        }

        @Nested
        @DisplayName("部門の検索ができる")
        class SearchDepartmentTests {
            @Test
            @DisplayName("部門コードで検索できる")
            void case1() {
                String departmentId = "30000";
                Department department = Department.of(DepartmentId.of(departmentId, LocalDateTime.of(2021,1,1,0,0,0)), LocalDateTime.of(9999, 12, 31, 0, 0), "部門", 0, departmentId + "~", 0, 1);
                departmentService.register(department);
                DepartmentCriteria criteria = DepartmentCriteria.builder().departmentCode(departmentId).build();

                PageInfo<Department> result = departmentService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(department, result.getList().getFirst());
                assertEquals(1, result.getTotal());
            }
            @Test
            @DisplayName("部門名で検索できる")
            void case2() {
                String departmentId = "30000";
                String departmentName = "部門";
                Department department =Department.of(DepartmentId.of(departmentId, LocalDateTime.of(2021,1,1,0,0,0)), LocalDateTime.of(9999, 12, 31, 0, 0), departmentName, 0, departmentId + "~", 0, 1);
                departmentService.register(department);
                DepartmentCriteria criteria = DepartmentCriteria.builder().departmentName(departmentName).build();

                PageInfo<Department> result = departmentService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(department, result.getList().getFirst());
                assertEquals(1, result.getTotal());
            }
            @Test
            @DisplayName("開始日で検索できる")
            void case3() {
                String departmentId = "30000";
                LocalDateTime startDate = LocalDateTime.of(2024,1,1,0,0,0);
                Department department = Department.of(DepartmentId.of(departmentId, startDate), LocalDateTime.of(9999, 12, 31, 0, 0), "部門", 0, departmentId + "~", 0, 1);
                departmentService.register(department);
                DepartmentCriteria criteria = DepartmentCriteria.builder().startDate(startDate).build();

                PageInfo<Department> result = departmentService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(department, result.getList().getFirst());
                assertEquals(1, result.getTotal());
            }
            @Test
            @DisplayName("終了日で検索できる")
            void case4() {
                String departmentId = "30000";
                LocalDateTime endDate = LocalDateTime.of(2025, 12, 31, 0, 0);
                Department department = Department.of(DepartmentId.of(departmentId, LocalDateTime.of(2021,1,1,0,0,0)), endDate, "部門", 0, departmentId + "~", 0, 1);
                departmentService.register(department);
                DepartmentCriteria criteria = DepartmentCriteria.builder().endDate(endDate).build();

                PageInfo<Department> result = departmentService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(department, result.getList().getFirst());
                assertEquals(1, result.getTotal());
            }
            @Test
            @DisplayName("複数条件で検索できる")
            void case5() {
                String departmentId = "30000";
                String departmentName = "部門";
                LocalDateTime startDate = LocalDateTime.of(2021,1,1,0,0,0);
                LocalDateTime endDate = LocalDateTime.of(9999, 12, 31, 0, 0);
                Department department = Department.of(DepartmentId.of(departmentId, startDate), endDate, departmentName, 0, departmentId + "~", 0, 1);
                departmentService.register(department);
                DepartmentCriteria criteria = DepartmentCriteria.builder().departmentCode(departmentId).departmentName(departmentName).startDate(startDate).endDate(endDate).build();

                PageInfo<Department> result = departmentService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(department, result.getList().getFirst());
                assertEquals(1, result.getTotal());
            }
            @Test
            @DisplayName("不正な期間を指定した場合、エラーが発生する")
            void case6() {
                assertThrows(IllegalArgumentException.class, () -> {
                    DepartmentCriteria criteria = DepartmentCriteria.builder().
                            startDate(LocalDateTime.of(2021, 1, 1, 0, 0, 0)).
                            endDate(LocalDateTime.of(2020, 12, 31, 0, 0, 0)).
                            build();
                });
            }
        }
    }
}
