package com.example.sms.infrastructure.datasource.master.employee;

import com.example.sms.domain.model.master.employee.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEntityMapper {
    public 社員マスタ mapToEntity(Employee employee) {
        社員マスタ employeeEntity = new 社員マスタ();
        employeeEntity.set社員コード(employee.getEmpCode());
        employeeEntity.set社員名(employee.getName());
        employeeEntity.set社員名カナ(employee.getKana());
        employeeEntity.setパスワード(employee.getLoginPassword());
        employeeEntity.set電話番号(employee.getTel());
        employeeEntity.setFax番号(employee.getFax());
        employeeEntity.set部門コード(employee.getDeptCode());
        employeeEntity.set開始日(employee.getStartDate());
        employeeEntity.set職種コード(employee.getOccuCode());
        employeeEntity.set承認権限コード(employee.getApprovalCode());
        return employeeEntity;
    }

    public Employee mapToDomainEntity(社員マスタ employeeEntity) {
        return new Employee(
                employeeEntity.get社員コード(),
                employeeEntity.get社員名(),
                employeeEntity.get社員名カナ(),
                employeeEntity.getパスワード(),
                employeeEntity.get電話番号(),
                employeeEntity.getFax番号(),
                employeeEntity.get部門コード(),
                employeeEntity.get開始日(),
                employeeEntity.get職種コード(),
                employeeEntity.get承認権限コード()
        );
    }
}
