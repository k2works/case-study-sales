package com.example.sms.infrastructure.datasource.master.department;

import com.example.sms.domain.model.master.department.*;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.infrastructure.datasource.master.employee.社員マスタ;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartmentEntityMapper {
    public 部門マスタ mapToEntity(Department department) {
        部門マスタ departmentEntity = new 部門マスタ();
        departmentEntity.set部門コード(department.getDepartmentId().getDeptCode().getValue());
        departmentEntity.set開始日(department.getDepartmentId().getDepartmentStartDate().getValue());
        departmentEntity.set終了日(department.getEndDate().getValue());
        departmentEntity.set部門名(department.getDepartmentName());
        departmentEntity.set組織階層(department.getLayer());
        departmentEntity.set部門パス(department.getPath().getValue());
        departmentEntity.set最下層区分(department.getLowerType().getValue());
        departmentEntity.set伝票入力可否(department.getSlitYn().getValue());
        return departmentEntity;
    }

    public Department mapToDomainModel(部門マスタ departmentEntity) {
        List<Employee> employees = departmentEntity.get社員().stream()
                .map(this::mapToEmployee)
                .collect(Collectors.toList());

        return new Department(
                DepartmentId.of(departmentEntity.get部門コード(), departmentEntity.get開始日()),
                DepartmentEndDate.of(departmentEntity.get終了日()),
                departmentEntity.get部門名(),
                departmentEntity.get組織階層(),
                DepartmentPath.of(departmentEntity.get部門パス()),
                DepartmentLowerType.of(departmentEntity.get最下層区分()),
                SlitYnType.of(departmentEntity.get伝票入力可否()),
                employees
        );
    }

    private Employee mapToEmployee(社員マスタ employeeEntity) {
        return Employee.of(
                employeeEntity.get社員コード(),
                employeeEntity.get社員名(),
                employeeEntity.get社員名カナ(),
                employeeEntity.get電話番号(),
                employeeEntity.getFax番号(),
                employeeEntity.get職種コード()
        );
    }
}
