package com.example.sms.infrastructure.datasource.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentEndDate;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.department.DepartmentPath;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.department.DepartmentLowerType;
import com.example.sms.domain.model.master.department.SlitYnType;
import com.example.sms.infrastructure.datasource.autogen.model.社員マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.部門マスタ;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

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

    public Department mapToDomainModel(DepartmentCustomEntity departmentEntity) {
        Function<社員マスタ, Employee> mapToEmployee = e -> Employee.of(
                e.get社員コード(),
                e.get社員名(),
                e.get社員名カナ(),
                e.get電話番号(),
                e.getFax番号(),
                e.get職種コード()
        );

        List<Employee> employees = departmentEntity.get社員().stream()
                .map(mapToEmployee)
                .toList();

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
}
