package com.example.sms.service.master.employee;

import com.example.sms.domain.model.master.employee.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("社員レポジトリ")
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private Employee getEmployee() {
        return Employee.of("EMP999", "name", "kana", "password", "090-1234-5678", "03-1234-5678", "11101", LocalDateTime.of(2021, 1, 1, 0, 0, 0), "", "");
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
        assertEquals(employee.getName(), actual.getName());
        assertEquals(employee.getKana(), actual.getKana());
        assertEquals(employee.getLoginPassword(), actual.getLoginPassword());
        assertEquals(employee.getTel(), actual.getTel());
        assertEquals(employee.getFax(), actual.getFax());
        assertEquals(employee.getDepartmentId().getDeptCode(), actual.getDepartmentId().getDeptCode());
        assertEquals(employee.getDepartmentId().getDepartmentStartDate(), actual.getDepartmentId().getDepartmentStartDate());
        assertEquals(employee.getOccuCode(), actual.getOccuCode());
        assertEquals(employee.getApprovalCode(), actual.getApprovalCode());
    }

    @Test
    @DisplayName("社員を更新できる")
    void shouldUpdateEmployee() {
        Employee employee = getEmployee();
        repository.save(employee);

        employee = repository.findById(employee.getEmpCode()).get();
        Employee updateEmployee = Employee.of("EMP999", "updateName", "updateKana", "password", "090-1234-5678", "03-1234-5678", "11101", LocalDateTime.of(2021, 1, 1, 0, 0, 0), "", "", employee.getDepartment());
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
