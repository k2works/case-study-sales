package com.example.sms.infrastructure.datasource.master.employee;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

@Data
public class EmployeeDownloadCSV {

    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "社員コード", required = true)
    private String employeeCode;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "社員名", required = true)
    private String employeeName;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "社員名カナ", required = false)
    private String employeeNameKana;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "パスワード", required = false)
    private String password;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "電話番号", required = false)
    private String phoneNumber;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "FAX番号", required = false)
    private String faxNumber;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "部門コード", required = false)
    private String departmentCode;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "開始日", required = false)
    private LocalDateTime startDate;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "職種コード", required = true)
    private String occuCode;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "承認権限コード", required = false)
    private String approvalCode;

    @CsvBindByPosition(position = 10)
    @CsvBindByName(column = "ユーザーID", required = true)
    private String userId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public EmployeeDownloadCSV(String employeeCode, String employeeName, String employeeNameKana, String loginPassword, String phoneNumber, String faxNumber, String occuCode, String approvalCode, String departmentCode, LocalDateTime startDate, String userId) {
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.employeeNameKana = employeeNameKana;
        this.password = loginPassword;
        this.phoneNumber = phoneNumber;
        this.faxNumber = faxNumber;
        this.occuCode = occuCode;
        this.approvalCode = approvalCode;
        this.departmentCode = departmentCode;
        this.startDate = startDate;
        this.userId = userId;
    }
}