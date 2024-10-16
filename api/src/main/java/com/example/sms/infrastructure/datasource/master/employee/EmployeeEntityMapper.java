package com.example.sms.infrastructure.datasource.master.employee;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.*;
import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserId;
import com.example.sms.infrastructure.datasource.master.department.部門マスタ;
import com.example.sms.infrastructure.datasource.system.user.Usr;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEntityMapper {
    public 社員マスタ mapToEntity(Employee employee) {
        社員マスタ employeeEntity = new 社員マスタ();
        employeeEntity.set社員コード(employee.getEmpCode().getValue());
        employeeEntity.set社員名(employee.getEmpName().Name());
        employeeEntity.set社員名カナ(employee.getEmpName().NameKana());
        employeeEntity.setパスワード(employee.getLoginPassword());
        employeeEntity.set電話番号(employee.getTel().getValue());
        employeeEntity.setFax番号(employee.getFax().getValue());
        if (employee.getDepartment() != null) {
            employeeEntity.set部門コード(employee.getDepartment().getDepartmentId().getDeptCode().getValue());
            employeeEntity.set開始日(employee.getDepartment().getDepartmentId().getDepartmentStartDate().getValue());
        }
        employeeEntity.set職種コード(employee.getOccuCode().getValue());
        employeeEntity.set承認権限コード(employee.getApprovalCode());
        if (employee.getUser() != null) {
            employeeEntity.setUserId(employee.getUser().getUserId().getValue());
        }
        return employeeEntity;
    }

    public Employee mapToDomainModel(社員マスタ employeeEntity) {
        Department department = mapToDepartment(employeeEntity.get部門());
        DepartmentId departmentId = DepartmentId.of(employeeEntity.get部門コード(), employeeEntity.get開始日());
        EmployeeCode employeeCode = EmployeeCode.of(employeeEntity.get社員コード());
        EmployeeName employeeName = EmployeeName.of(employeeEntity.get社員名(), employeeEntity.get社員名カナ());
        PhoneNumber phoneNumber = PhoneNumber.of(employeeEntity.get電話番号());
        FaxNumber faxNumber = FaxNumber.of(employeeEntity.getFax番号());
        JobCode jobCode = JobCode.of(employeeEntity.get職種コード());
        UserId userId = UserId.of(employeeEntity.getUserId());
        User user = mapToUser(employeeEntity.getUser());

        return new Employee(
                employeeCode,
                employeeName,
                employeeEntity.getパスワード(),
                phoneNumber,
                faxNumber,
                jobCode,
                employeeEntity.get承認権限コード(),
                department,
                user
        );
    }

    private Department mapToDepartment(部門マスタ departmentEntity) {
        if (departmentEntity == null) return null;

        return Department.of(
                DepartmentId.of(departmentEntity.get部門コード(), departmentEntity.get開始日()),
                departmentEntity.get終了日(),
                departmentEntity.get部門名(),
                departmentEntity.get組織階層(),
                departmentEntity.get部門パス(),
                departmentEntity.get最下層区分(),
                departmentEntity.get伝票入力可否()
        );
    }

    private User mapToUser(Usr userEntity) {
        if (userEntity == null) return null;

        return User.of(
                userEntity.getUserId(),
                userEntity.getPassword(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                RoleName.valueOf(userEntity.getRoleName())
        );
    }
}
