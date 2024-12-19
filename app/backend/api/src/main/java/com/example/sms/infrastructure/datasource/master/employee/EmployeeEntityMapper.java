package com.example.sms.infrastructure.datasource.master.employee;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.type.user.RoleName;
import com.example.sms.infrastructure.datasource.autogen.model.Usr;
import com.example.sms.infrastructure.datasource.autogen.model.社員マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.部門マスタ;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEntityMapper {
    public 社員マスタ mapToEntity(Employee employee) {
        社員マスタ employeeEntity = new 社員マスタ();
        employeeEntity.set社員コード(employee.getEmpCode().getValue());
        employeeEntity.set社員名(employee.getEmpName().Name());
        employeeEntity.set社員名カナ(employee.getEmpName().NameKana());
        employeeEntity.setパスワード(employee.getLoginPassword());
        if (employee.getTel() != null) {
            employeeEntity.set電話番号(employee.getTel().getValue());
        }
        if (employee.getFax() != null) {
            employeeEntity.setFax番号(employee.getFax().getValue());
        }
        if (employee.getDepartment() != null) {
            employeeEntity.set部門コード(employee.getDepartment().getDepartmentId().getDeptCode().getValue());
            employeeEntity.set開始日(employee.getDepartment().getDepartmentId().getDepartmentStartDate().getValue());
        }
        if (employee.getOccuCode() != null) {
            employeeEntity.set職種コード(employee.getOccuCode().getValue());
        }
        employeeEntity.set承認権限コード(employee.getApprovalCode());
        if (employee.getUser() != null) {
            employeeEntity.setUserId(employee.getUser().getUserId().getValue());
        }
        return employeeEntity;
    }

    public Employee mapToDomainModel(EmployeeCustomEntity employeeEntity) {
        Department department = mapToDepartment(employeeEntity.get部門());
        User user = mapToUser(employeeEntity.getUser());

        return Employee.of(
                employeeEntity.get社員コード(),
                employeeEntity.get社員名(),
                employeeEntity.get社員名カナ(),
                employeeEntity.getパスワード(),
                employeeEntity.get電話番号(),
                employeeEntity.getFax番号(),
                employeeEntity.get職種コード(),
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
