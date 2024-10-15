package com.example.sms.infrastructure.datasource.master.employee;

import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeList;
import com.example.sms.service.master.employee.EmployeeRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class EmployeeDataSource implements EmployeeRepository {
    final 社員マスタMapper employeeMapper;
    final EmployeeEntityMapper employeeEntityMapper;

    public EmployeeDataSource(社員マスタMapper employeeMapper, EmployeeEntityMapper employeeEntityMapper) {
        this.employeeMapper = employeeMapper;
        this.employeeEntityMapper = employeeEntityMapper;
    }

    @Override
    public Optional<Employee> findById(String empCode) {
        return Optional.empty();
    }

    @Override
    public EmployeeList selectAll() {
        return null;
    }

    @Override
    public void save(Employee employee) {

    }

    @Override
    public void deleteById(String empCode) {

    }

    @Override
    public void deleteAll() {

    }
}
