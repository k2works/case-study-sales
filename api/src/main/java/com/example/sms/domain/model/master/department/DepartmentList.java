package com.example.sms.domain.model.master.department;

import java.util.List;

/**
 * 部門一覧
 */
public class DepartmentList {
    List<Department> value;

    public DepartmentList(List<Department> value) {
        this.value = value;
    }

    public int size() {
        return value.size();
    }

    public List<Department> asList() {
        return value;
    }
}
