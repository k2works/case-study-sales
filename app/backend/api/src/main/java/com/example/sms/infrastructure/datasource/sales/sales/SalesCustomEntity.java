package com.example.sms.infrastructure.datasource.sales.sales;

import com.example.sms.infrastructure.datasource.autogen.model.売上データ;
import com.example.sms.infrastructure.datasource.master.department.DepartmentCustomEntity;
import com.example.sms.infrastructure.datasource.master.employee.EmployeeCustomEntity;
import com.example.sms.infrastructure.datasource.sales.sales.sales_line.SalesLineCustomEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SalesCustomEntity extends 売上データ {
    private DepartmentCustomEntity 部門マスタ;
    private EmployeeCustomEntity 社員マスタ;
    private List<SalesLineCustomEntity> 売上データ明細;
}
