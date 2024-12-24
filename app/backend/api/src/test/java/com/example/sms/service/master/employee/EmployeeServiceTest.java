package com.example.sms.service.master.employee;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeList;
import com.example.sms.service.master.department.DepartmentService;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
@DisplayName("社員サービス")
class EmployeeServiceTest {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TestDataFactory testDataFactory;
    @Autowired
    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForEmployeeService();
    }

    @Test
    @DisplayName("社員一覧を取得できる")
    void shouldRetrieveAllEmployees() {
        EmployeeList result = employeeService.selectAll();
        assertEquals(2, result.asList().size());
    }

    @Test
    @DisplayName("社員を新規登録できる")
    void shouldRegisterNewEmployee() {
        Employee newEmployee = testDataFactory.Employee();

        employeeService.register(newEmployee);

        EmployeeList result = employeeService.selectAll();
        assertEquals(3, result.asList().size());
    }

    @Test
    @DisplayName("社員の登録情報を編集できる")
    void shouldEditEmployeeDetails() {
        Employee employee = testDataFactory.Employee();
        Employee updateEmployee = Employee.of(
                Employee.of(employee.getEmpCode().getValue(), "edited EmployeeName", employee.getEmpName().NameKana(), employee.getTel().getValue(), employee.getFax().getValue(), employee.getOccuCode().getValue()),
                employee.getDepartment(),
                employee.getUser()
        );

        employeeService.save(updateEmployee);

        Employee result = employeeService.find(employee.getEmpCode());
        assertEquals("edited EmployeeName", result.getEmpName().Name());
    }

    @Test
    @DisplayName("社員を削除できる")
    void shouldDeleteEmployee() {
        Employee employee = testDataFactory.Employee();

        employeeService.delete(employee.getEmpCode());

        EmployeeList result = employeeService.selectAll();
        assertEquals(2, result.asList().size());
    }

    @Test
    @DisplayName("社員を検索できる")
    void shouldFindEmployee() {
        Employee employee = testDataFactory.Employee();
        employeeService.register(employee);

        Employee result = employeeService.find(employee.getEmpCode());

        assertEquals(employee, result);
    }

    @Nested
    @DisplayName("社員の検索ができる")
    class EmployeeSearchTests {
        @Test
        @DisplayName("社員をコードで検索できる")
        void case1() {
            Employee employee = testDataFactory.Employee();
            Employee searchEmployee = Employee.of(
                    Employee.of("EMP123", employee.getEmpName().Name(), employee.getEmpName().NameKana(), employee.getTel().getValue(), employee.getFax().getValue(), employee.getOccuCode().getValue()),
                    employee.getDepartment(),
                    employee.getUser()
            );
            employeeService.register(searchEmployee);
            EmployeeCriteria criteria = EmployeeCriteria.builder().employeeCode("EMP123").build();

            PageInfo<Employee> result = employeeService.searchWithPageInfo(criteria);

            assertEquals(1, result.getList().size());
            assertEquals(searchEmployee, result.getList().getFirst());
            assertEquals(1, result.getTotal());
        }
        @Test
        @DisplayName("社員を名前で検索できる")
        void case2() {
            Employee employee = testDataFactory.Employee();
            Employee searchEmployee = Employee.of(
                    Employee.of(employee.getEmpCode().getValue(), "検索 太郎", employee.getEmpName().NameKana(), employee.getTel().getValue(), employee.getFax().getValue(), employee.getOccuCode().getValue()),
                    employee.getDepartment(),
                    employee.getUser()
            );
            employeeService.register(searchEmployee);
            EmployeeCriteria criteria = EmployeeCriteria.builder().employeeName("検索 太郎").build();

            PageInfo<Employee> result = employeeService.searchWithPageInfo(criteria);

            assertEquals(1, result.getList().size());
            assertEquals(searchEmployee, result.getList().getFirst());
            assertEquals(1, result.getTotal());
        }
        @Test
        @DisplayName("社員を名前カナで検索できる")
        void case3() {
            Employee employee = testDataFactory.Employee();
            Employee searchEmployee = Employee.of(
                    Employee.of(employee.getEmpCode().getValue(), employee.getEmpName().Name(), "ケンサク タロウ", employee.getTel().getValue(), employee.getFax().getValue(), employee.getOccuCode().getValue()),
                    employee.getDepartment(),
                    employee.getUser()
            );
            employeeService.register(searchEmployee);
            EmployeeCriteria criteria = EmployeeCriteria.builder().employeeNameKana("ケンサク タロウ").build();

            PageInfo<Employee> result = employeeService.searchWithPageInfo(criteria);

            assertEquals(1, result.getList().size());
            assertEquals(searchEmployee, result.getList().getFirst());
            assertEquals(1, result.getTotal());
        }
        @Test
        @DisplayName("社員を電話番号で検索できる")
        void case4() {
            Employee employee = testDataFactory.Employee();
            Employee searchEmployee = Employee.of(
                    Employee.of(employee.getEmpCode().getValue(), employee.getEmpName().Name(), employee.getEmpName().NameKana(), "03-9999-0000", employee.getFax().getValue(), employee.getOccuCode().getValue()),
                    employee.getDepartment(),
                    employee.getUser()
            );
            employeeService.register(searchEmployee);
            EmployeeCriteria criteria = EmployeeCriteria.builder().phoneNumber("03-9999-0000").build();

            PageInfo<Employee> result = employeeService.searchWithPageInfo(criteria);

            assertEquals(1, result.getList().size());
            assertEquals(searchEmployee, result.getList().getFirst());
            assertEquals(1, result.getTotal());
        }
        @Test
        @DisplayName("社員をFAX番号で検索できる")
        void case5() {
            Employee employee = testDataFactory.Employee();
            Employee searchEmployee = Employee.of(
                    Employee.of(employee.getEmpCode().getValue(), employee.getEmpName().Name(), employee.getEmpName().NameKana(), employee.getTel().getValue(), "03-9999-0000", employee.getOccuCode().getValue()),
                    employee.getDepartment(),
                    employee.getUser()
            );
            employeeService.register(searchEmployee);
            EmployeeCriteria criteria = EmployeeCriteria.builder().faxNumber("03-9999-0000").build();

            PageInfo<Employee> result = employeeService.searchWithPageInfo(criteria);

            assertEquals(1, result.getList().size());
            assertEquals(searchEmployee, result.getList().getFirst());
            assertEquals(1, result.getTotal());
        }
        @Test
        @DisplayName("社員を部署コードで検索できる")
        void case6() {
            Department department = testDataFactory.Department();
            Department searchDepartment = Department.of(DepartmentId.of("12345", LocalDateTime.of(2024,1,1,0,0)), department.getEndDate().getValue(), department.getDepartmentName(), department.getLayer(), department.getPath().getValue(), department.getLowerType().getValue(), department.getSlitYn().getValue());
            Employee employee = testDataFactory.Employee();
            Employee searchEmployee = Employee.of(
                    Employee.of(employee.getEmpCode().getValue(), employee.getEmpName().Name(), employee.getEmpName().NameKana(), employee.getTel().getValue(), employee.getFax().getValue(), employee.getOccuCode().getValue()),
                    searchDepartment,
                    employee.getUser()
            );
            departmentService.register(searchDepartment);
            employeeService.register(searchEmployee);
            EmployeeCriteria criteria = EmployeeCriteria.builder().departmentCode("12345").build();

            PageInfo<Employee> result = employeeService.searchWithPageInfo(criteria);

            assertEquals(1, result.getList().size());
            assertEquals(searchEmployee, result.getList().getFirst());
            assertEquals(1, result.getTotal());
        }
        @Test
        @DisplayName("複合条件で社員を検索できる")
        void case7() {
            Department department = testDataFactory.Department();
            Department searchDepartment = Department.of(DepartmentId.of("12345", LocalDateTime.of(2024,1,1,0,0)), department.getEndDate().getValue(), department.getDepartmentName(), department.getLayer(), department.getPath().getValue(), department.getLowerType().getValue(), department.getSlitYn().getValue());
            Employee employee = testDataFactory.Employee();
            Employee searchEmployee = Employee.of(
                    Employee.of("EMP123", "検索 太郎", "ケンサク タロウ", "03-9999-0000", "03-9999-0000", employee.getOccuCode().getValue()),
                    searchDepartment,
                    employee.getUser()
            );
            departmentService.register(searchDepartment);
            employeeService.register(searchEmployee);
            EmployeeCriteria criteria = EmployeeCriteria.builder().employeeCode("EMP123").employeeName("検索 太郎").employeeNameKana("ケンサク タロウ").phoneNumber("03-9999-0000").faxNumber("03-9999-0000").departmentCode("12345").build();

            PageInfo<Employee> result = employeeService.searchWithPageInfo(criteria);

            assertEquals(1, result.getList().size());
            assertEquals(searchEmployee, result.getList().getFirst());
            assertEquals(1, result.getTotal());
        }
    }
}
