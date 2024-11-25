package com.example.sms.service.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.service.master.employee.EmployeeRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 部門サービス
 */
@Service
@Transactional
public class DepartmentService {
    final DepartmentRepository departmentRepository;
    final EmployeeRepository employeeRepository;

    public DepartmentService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * 部門一覧
     */
    public DepartmentList selectAll() {
        return departmentRepository.selectAll();
    }

    /**
     * 部門一覧（ページング）
     */
    public PageInfo<Department> selectAllWithPageInfo() {
        return departmentRepository.selectAllWithPageInfo();
    }
    /**
     * 部門新規登録
     */
    public void register(Department department) {
        departmentRepository.save(department);
    }

    /**
     * 部門情報編集
     */
    public void save(Department department) {
        departmentRepository.save(department);
    }

    /**
     * 部門情報編集(社員追加・削除)
     */
    public void save(Department department, List<Employee> addFilteredEmployees, List<Employee> deleteFilteredEmployees) {
        departmentRepository.save(department);
        addFilteredEmployees.forEach(employee -> {
            Optional<Employee> emp = employeeRepository.findById(employee.getEmpCode());
            emp.ifPresent(value -> {
                Employee updateEmp = Employee.of(value, department, value.getUser());
                employeeRepository.save(updateEmp);
            });
        });
        deleteFilteredEmployees.forEach(employee -> {
            Optional<Employee> emp = employeeRepository.findById(employee.getEmpCode());
            emp.ifPresent(value -> {
                Employee updateEmp = Employee.of(value, null, value.getUser());
                employeeRepository.save(updateEmp);
            });
        });
    }
    /**
     * 部門削除
     */
    public void delete(DepartmentId departmentId) {
        departmentRepository.deleteById(departmentId);
    }

    /**
     * 部門検索 (部門ID)
     */
    public Department find(DepartmentId departmentId) {
        return departmentRepository.findById(departmentId).orElse(null);
    }

    /**
     * 部門検索（部門コード）
     */
    public DepartmentList findByCode(DepartmentId departmentId) {
        return departmentRepository.findByCode(departmentId.getDeptCode().getValue());
    }

}
