package com.example.sms.presentation.api.master.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Schema(description = "社員")
public class EmployeeResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private String empCode;       // 社員コード
    @NotNull
    private String empName;       // 社員名
    @NotNull
    private String empNameKana;   // 社員名カナ
    private String tel;           // 電話番号
    private String fax;           // fax番号
    private String occuCode;      // 職種コード
    private String departmentCode;
    private String departmentStartDate;
    private String userId;
    private boolean addFlag;
    private boolean deleteFlag;
}
