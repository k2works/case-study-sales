package com.example.sms.infrastructure.datasource.master.department;

import com.example.sms.infrastructure.datasource.autogen.model.部門マスタ;
import com.example.sms.infrastructure.datasource.master.employee.社員マスタ;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DepartmentCustomEntity extends 部門マスタ {
    private List<社員マスタ> 社員;
}
