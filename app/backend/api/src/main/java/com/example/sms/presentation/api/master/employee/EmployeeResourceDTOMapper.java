package com.example.sms.presentation.api.master.employee;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.service.master.employee.EmployeeCriteria;

public class EmployeeResourceDTOMapper {

    public static Employee convertEntity(EmployeeResource resource, Department department, User user) {
        Employee employee = Employee.of(
                resource.getEmpCode(),
                resource.getEmpFirstName() + " " + resource.getEmpLastName(),
                resource.getEmpFirstName() + " " + resource.getEmpLastName(),
                resource.getTel(),
                resource.getFax(),
                resource.getOccuCode());

        return Employee.of(employee, department, user);
    }

    public static EmployeeCriteria convertToCriteria(EmployeeCriteriaResource resource) {
        return EmployeeCriteria.builder()
                .employeeCode(resource.getEmployeeCode())
                .employeeFirstName(resource.getEmployeeFirstName())
                .employeeLastName(resource.getEmployeeLastName())
                .employeeFirstNameKana(resource.getEmployeeFirstNameKana())
                .employeeLastNameKana(resource.getEmployeeLastNameKana())
                .phoneNumber(resource.getPhoneNumber())
                .faxNumber(resource.getFaxNumber())
                .departmentCode(resource.getDepartmentCode())
                .build();
    }
}