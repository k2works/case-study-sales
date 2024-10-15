package com.example.sms.infrastructure.datasource.master.employee;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.infrastructure.datasource.master.department.部門マスタ;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEntityMapper {
    public 社員マスタ mapToEntity(Employee employee) {
        社員マスタ employeeEntity = new 社員マスタ();
        employeeEntity.set社員コード(employee.getEmpCode());
        employeeEntity.set社員名(employee.getName());
        employeeEntity.set社員名カナ(employee.getKana());
        employeeEntity.setパスワード(employee.getLoginPassword());
        employeeEntity.set電話番号(employee.getTel());
        employeeEntity.setFax番号(employee.getFax());
        employeeEntity.set部門コード(employee.getDeptCode());
        employeeEntity.set開始日(employee.getStartDate());
        employeeEntity.set職種コード(employee.getOccuCode());
        employeeEntity.set承認権限コード(employee.getApprovalCode());
        if (employee.getDepartment() != null) {
            employeeEntity.set部門(mapToDepartmentEntity(employee.getDepartment()));
        }
        return employeeEntity;
    }

    private 部門マスタ mapToDepartmentEntity(Department department) {
        部門マスタ departmentEntity = new 部門マスタ();
        departmentEntity.set部門コード(department.getDepartmentId().getDeptCode());
        departmentEntity.set開始日(department.getDepartmentId().getStartDate());
        departmentEntity.set部門名(department.getDepartmentName());
        departmentEntity.set組織階層(department.getLayer());
        departmentEntity.set部門パス(department.getPath());
        departmentEntity.set最下層区分(department.getLowerType());
        departmentEntity.set伝票入力可否(department.getSlitYn());
        return departmentEntity;
    }

    public Employee mapToDomainEntity(社員マスタ employeeEntity) {
        Department department = mapToDepartment(employeeEntity.get部門());

        return new Employee(
                employeeEntity.get社員コード(),
                employeeEntity.get社員名(),
                employeeEntity.get社員名カナ(),
                employeeEntity.getパスワード(),
                employeeEntity.get電話番号(),
                employeeEntity.getFax番号(),
                employeeEntity.get部門コード(),
                employeeEntity.get開始日(),
                employeeEntity.get職種コード(),
                employeeEntity.get承認権限コード(),
                department
        );
    }

    private Department mapToDepartment(部門マスタ departmentEntity) {
        if (departmentEntity == null) return null;

        return Department.of(
                DepartmentId.of(departmentEntity.get部門コード(), departmentEntity.get開始日()),
                departmentEntity.get開始日(),
                departmentEntity.get部門名(),
                departmentEntity.get組織階層(),
                departmentEntity.get部門パス(),
                departmentEntity.get最下層区分(),
                departmentEntity.get伝票入力可否()
        );
    }
}
