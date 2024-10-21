package com.example.sms.service.master.employee;

import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.employee.EmployeeList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 社員サービス
 */
@Service
@Transactional
public class EmployeeService {
    final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * 社員一覧
     */
    public EmployeeList selectAll() {
        return employeeRepository.selectAll();
    }

    /**
     * 社員新規登録
     */
    public void register(Employee employee) {
        employeeRepository.save(employee);
    }

    /**
     * 社員情報編集
     */
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    /**
     * 社員削除
     */
    public void delete(EmployeeCode empCode) {
        employeeRepository.deleteById(empCode);
    }

    /**
     * 社員検索
     */
    public Employee find(EmployeeCode empCode) {
        return employeeRepository.findById(empCode).orElse(null);
    }
}
