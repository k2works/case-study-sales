package com.example.sms.infrastructure.datasource.sales_order;

import com.example.sms.infrastructure.datasource.autogen.model.*;
import com.example.sms.infrastructure.datasource.master.department.DepartmentCustomEntity;
import com.example.sms.infrastructure.datasource.master.employee.EmployeeCustomEntity;
import com.example.sms.infrastructure.datasource.master.partner.customer.CustomerCustomEntity;
import com.example.sms.infrastructure.datasource.sales_order.sales_order_line.SalesOrderLineCustomEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SalesOrderCustomEntity extends 受注データ {
    private DepartmentCustomEntity 部門マスタ;
    private CustomerCustomEntity 顧客マスタ;
    private EmployeeCustomEntity 社員マスタ;
    private List<SalesOrderLineCustomEntity> 受注データ明細;
}
