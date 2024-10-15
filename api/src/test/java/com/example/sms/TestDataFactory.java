package com.example.sms;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.system.user.User;

public interface TestDataFactory {
    void setUpForAuthApiService();

    User User();

    Department Department();

    Employee Employee();

    void setUpForUserManagementService();

    void setUpForDepartmentService();

    void setUpForEmployeeService();
}
