package com.example.sms.infrastructure.datasource.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import org.springframework.stereotype.Component;

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
        return new Department(
                new DepartmentId(departmentEntity.get部門コード(), departmentEntity.get開始日()),
                departmentEntity.get終了日(),
                departmentEntity.get部門名(),
                departmentEntity.get組織階層(),
                departmentEntity.get部門パス(),
                departmentEntity.get最下層区分(),
                departmentEntity.get伝票入力可否()
        );
    }
}
