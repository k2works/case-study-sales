package com.example.sms.domain.model.master.employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 社員一覧
 */
public class EmployeeList {
    List<Employee> value;

    public EmployeeList(List<Employee> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public EmployeeList add(Employee employee) {
        List<Employee> newValue = new ArrayList<>(value);
        newValue.add(employee);
        return new EmployeeList(newValue);
    }

    public List<Employee> asList() {
        return value;
    }
}
