package com.example.sms.presentation.api.master.employee;

import com.example.sms.service.master.employee.EmployeeCriteria;

public class EmployeeResourceDTOMapper {

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