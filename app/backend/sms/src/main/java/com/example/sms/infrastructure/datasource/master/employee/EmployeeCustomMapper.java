package com.example.sms.infrastructure.datasource.master.employee;

import com.example.sms.service.master.employee.EmployeeCriteria;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeeCustomMapper {
    EmployeeCustomEntity selectByPrimaryKey(String empCode);

    List<EmployeeCustomEntity> selectAll();

    @Delete("DELETE FROM 社員マスタ")
    void deleteAll();

    List<EmployeeCustomEntity> selectByDepartmentCode(String departmentCode);

    List<EmployeeCustomEntity> selectByCriteria(EmployeeCriteria criteria);
}
