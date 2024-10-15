package com.example.sms.infrastructure.datasource.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.infrastructure.datasource.master.employee.社員マスタ;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartmentEntityMapper {
    public 部門マスタ mapToEntity(Department department) {
        部門マスタ departmentEntity = new 部門マスタ();
        departmentEntity.set部門コード(department.getDepartmentId().getDeptCode());
        departmentEntity.set開始日(department.getDepartmentId().getStartDate());
        departmentEntity.set終了日(department.getEndDate());
        departmentEntity.set部門名(department.getDepartmentName());
        departmentEntity.set組織階層(department.getLayer());
        departmentEntity.set部門パス(department.getPath());
        departmentEntity.set最下層区分(department.getLowerType());
        departmentEntity.set伝票入力可否(department.getSlitYn());
        return departmentEntity;
    }

    public Department mapToDomainEntity(部門マスタ departmentEntity) {
        List<Employee> employees = departmentEntity.get社員().stream()
                .map(this::mapToEmployee)
                .collect(Collectors.toList());

        return new Department(
                new DepartmentId(departmentEntity.get部門コード(), departmentEntity.get開始日()),
                departmentEntity.get終了日(),
                departmentEntity.get部門名(),
                departmentEntity.get組織階層(),
                departmentEntity.get部門パス(),
                departmentEntity.get最下層区分(),
                departmentEntity.get伝票入力可否(),
                employees
        );
    }

    private Employee mapToEmployee(社員マスタ employeeEntity) {
        return Employee.of(
                employeeEntity.get社員コード(),
                employeeEntity.get社員名(),
                employeeEntity.get社員名カナ(),
                employeeEntity.getパスワード(),
                employeeEntity.get電話番号(),
                employeeEntity.getFax番号(),
                employeeEntity.get部門コード(),
                employeeEntity.get開始日(),
                employeeEntity.get職種コード(),
                employeeEntity.get承認権限コード()
        );
    }
}
