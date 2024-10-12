package com.example.sms.infrastructure.datasource.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.service.master.department.DepartmentRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DepartmentDataSource implements DepartmentRepository {
    @Override
    public Optional<Department> findById(DepartmentId departmentId) {
        return Optional.empty();
    }

    @Override
    public DepartmentList selectAll() {
        return null;
    }

    @Override
    public void save(Department department) {

    }

    @Override
    public void update(Department department) {

    }

    @Override
    public void deleteById(DepartmentId departmentId) {

    }

    @Override
    public void deleteAll() {

    }
}
