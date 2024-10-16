package com.example.sms.infrastructure.datasource.master.employee;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.*;
import com.example.sms.infrastructure.datasource.master.department.部門マスタ;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEntityMapper {
    public 社員マスタ mapToEntity(Employee employee) {
        社員マスタ employeeEntity = new 社員マスタ();
        employeeEntity.set社員コード(employee.getEmpCode().getValue());
        employeeEntity.set社員名(employee.getEmpName().Name());
        employeeEntity.set社員名カナ(employee.getEmpName().NameKana());
        employeeEntity.setパスワード(employee.getLoginPassword());
        employeeEntity.set電話番号(employee.getTel().getValue());
        employeeEntity.setFax番号(employee.getFax().getValue());
        employeeEntity.set部門コード(employee.getDepartmentId().getDeptCode().getValue());
        employeeEntity.set開始日(employee.getDepartmentId().getDepartmentStartDate().getValue());
        employeeEntity.set職種コード(employee.getOccuCode());
        employeeEntity.set承認権限コード(employee.getApprovalCode());
        if (employee.getDepartment() != null) {
            employeeEntity.set部門(mapToDepartmentEntity(employee.getDepartment()));
        }
        return employeeEntity;
    }

    private 部門マスタ mapToDepartmentEntity(Department department) {
        部門マスタ departmentEntity = new 部門マスタ();
        departmentEntity.set部門コード(department.getDepartmentId().getDeptCode().getValue());
        departmentEntity.set開始日(department.getDepartmentId().getDepartmentStartDate().getValue());
        departmentEntity.set部門名(department.getDepartmentName());
        departmentEntity.set組織階層(department.getLayer());
        departmentEntity.set部門パス(department.getPath().getValue());
        departmentEntity.set最下層区分(department.getLowerType().getValue());
        departmentEntity.set伝票入力可否(department.getSlitYn().getValue());
        return departmentEntity;
    }

    public Employee mapToDomainModel(社員マスタ employeeEntity) {
        Department department = mapToDepartment(employeeEntity.get部門());
        DepartmentId departmentId = DepartmentId.of(employeeEntity.get部門コード(), employeeEntity.get開始日());
        EmployeeCode employeeCode = EmployeeCode.of(employeeEntity.get社員コード());
        EmployeeName employeeName = EmployeeName.of(employeeEntity.get社員名(), employeeEntity.get社員名カナ());
        PhoneNumber phoneNumber = PhoneNumber.of(employeeEntity.get電話番号());
        FaxNumber faxNumber = FaxNumber.of(employeeEntity.getFax番号());

        return new Employee(
                employeeCode,
                employeeName,
                employeeEntity.getパスワード(),
                phoneNumber,
                faxNumber,
                departmentId,
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
