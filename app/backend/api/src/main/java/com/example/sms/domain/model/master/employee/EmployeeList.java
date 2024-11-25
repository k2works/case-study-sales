package com.example.sms.domain.model.master.employee;

import java.util.List;

/**
 * 社員一覧
 */
public class EmployeeList {
    List<Employee> value;

    public EmployeeList(List<Employee> value) {
        this.value = value;
    }

    public int size() {
        return value.size();
    }

    public List<Employee> asList() {
        return value;
    }
}
