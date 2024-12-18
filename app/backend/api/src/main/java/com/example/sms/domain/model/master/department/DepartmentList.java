package com.example.sms.domain.model.master.department;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 部門一覧
 */
public class DepartmentList {
    List<Department> value;

    public DepartmentList(List<Department> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public DepartmentList add(Department department) {
        List<Department> newValue = new ArrayList<>(value);
        newValue.add(department);
        return new DepartmentList(newValue);
    }

    public List<Department> asList() {
        return value;
    }
}
