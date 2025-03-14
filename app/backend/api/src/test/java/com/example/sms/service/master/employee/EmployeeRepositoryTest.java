package com.example.sms.service.master.employee;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.system.download.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("社員レポジトリ")
class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private Employee getEmployee() {
        return TestDataFactoryImpl.getEmployee("EMP999", "10000", LocalDateTime.of(2021, 1, 1, 0, 0));
    }

    @Test
    @DisplayName("社員一覧を取得できる")
    void shouldRetrieveAllEmployees() {
        Employee employee = getEmployee();

        repository.save(employee);

        assertEquals(1, repository.selectAll().size());
    }

    @Test
    @DisplayName("社員を登録できる")
    void shouldRegisterEmployee() {
        Employee employee = getEmployee();

        repository.save(employee);

        Employee actual = repository.findById(employee.getEmpCode()).get();
        assertEquals(employee.getEmpCode(), actual.getEmpCode());
        assertEquals(employee.getEmpName().Name(), actual.getEmpName().Name());
        assertEquals(employee.getEmpName().NameKana(), actual.getEmpName().NameKana());
        assertEquals(employee.getLoginPassword(), actual.getLoginPassword());
        assertEquals(employee.getTel(), actual.getTel());
        assertEquals(employee.getFax(), actual.getFax());
        assertEquals(employee.getDepartment().getDepartmentId().getDeptCode(), actual.getDepartment().getDepartmentId().getDeptCode());
        assertEquals(employee.getDepartment().getDepartmentId().getDepartmentStartDate(), actual.getDepartment().getDepartmentId().getDepartmentStartDate());
        assertEquals(employee.getOccuCode(), actual.getOccuCode());
        assertEquals(employee.getApprovalCode(), actual.getApprovalCode());
    }

    @Test
    @DisplayName("社員を更新できる")
    void shouldUpdateEmployee() {
        Employee employee = getEmployee();
        repository.save(employee);

        employee = repository.findById(employee.getEmpCode()).get();
        Employee updateEmployee = Employee.of(
                Employee.of("EMP999", "firstName lastName2", "firstKana lastKana2", "090-1234-5678", "03-1234-5678", ""),
                employee.getDepartment(),
                employee.getUser()
        );
        repository.save(updateEmployee);

        Employee actual = repository.findById(employee.getEmpCode()).get();
        assertEquals(updateEmployee, actual);
    }

    @Test
    @DisplayName("社員を削除できる")
    void shouldDeleteEmployee() {
        Employee employee = getEmployee();

        repository.save(employee);

        repository.deleteById(employee.getEmpCode());

        assertEquals(0, repository.selectAll().size());
    }
}
