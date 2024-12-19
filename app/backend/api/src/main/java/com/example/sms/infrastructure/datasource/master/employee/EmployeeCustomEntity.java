package com.example.sms.infrastructure.datasource.master.employee;

import com.example.sms.infrastructure.datasource.autogen.model.Usr;
import com.example.sms.infrastructure.datasource.autogen.model.社員マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.部門マスタ;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeCustomEntity extends 社員マスタ {
    private 部門マスタ 部門;

    private Usr user;
}
