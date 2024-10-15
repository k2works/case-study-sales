package com.example.sms.service.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.department.DepartmentList;

import java.util.Optional;

public interface DepartmentRepository {
    Optional<Department> findById(DepartmentId departmentId);

    DepartmentList selectAll();

    void save(Department department);

    void update(Department department);

    void deleteById(DepartmentId departmentId);

    void deleteAll();
}