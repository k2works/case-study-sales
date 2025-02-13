package com.example.sms.service.master.employee;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        Employee newEmployee = TestDataFactoryImpl.getEmployee("EMP999", "10000", LocalDateTime.of(2021, 1, 1, 0, 0));

        employeeService.register(newEmployee);

        EmployeeList result = employeeService.selectAll();
        assertEquals(3, result.asList().size());
    }

    @Test
    @DisplayName("社員の登録情報を編集できる")
    void shouldEditEmployeeDetails() {
        Employee employee = TestDataFactoryImpl.getEmployee("EMP999", "10000", LocalDateTime.of(2021, 1, 1, 0, 0));
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
        Employee employee = TestDataFactoryImpl.getEmployee("EMP999", "10000", LocalDateTime.of(2021, 1, 1, 0, 0));

        employeeService.delete(employee.getEmpCode());

        EmployeeList result = employeeService.selectAll();
        assertEquals(2, result.asList().size());
    }

    @Test
    @DisplayName("社員を検索できる")
    void shouldFindEmployee() {
        Employee employee = TestDataFactoryImpl.getEmployee("EMP999", "10000", LocalDateTime.of(2021, 1, 1, 0, 0));

        employeeService.register(employee);

        Employee result = employeeService.find(employee.getEmpCode());

        assertEquals(employee, result);
    }
}
