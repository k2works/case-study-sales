package com.example.sms.service.master.employee;

import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeList;

import java.util.Optional;

public interface EmployeeRepository {
    Optional<Employee> findById(String empCode);

    EmployeeList selectAll();

    void save(Employee employee);

    void deleteById(String empCode);

    void deleteAll();
}
