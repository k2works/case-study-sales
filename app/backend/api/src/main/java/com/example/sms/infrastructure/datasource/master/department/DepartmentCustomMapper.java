package com.example.sms.infrastructure.datasource.master.department;

import com.example.sms.infrastructure.datasource.autogen.model.部門マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.部門マスタKey;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartmentCustomMapper {
    DepartmentCustomEntity selectByPrimaryKey(部門マスタKey key);

    @Delete("DELETE FROM 部門マスタ")
    void deleteAll();

    List<DepartmentCustomEntity> selectAll();

    List<DepartmentCustomEntity> selectByDepartmentCode(String departmentCode);
}
