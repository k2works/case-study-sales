package com.example.sms.infrastructure.datasource.master.department;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

@Data
public class DepartmentDownloadCSV {
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "部門コード", required = true)
    private String departmentCode;
    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "部門名", required = true)
    private LocalDateTime departmentStartDate;
    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "開始日", required = true)
    private LocalDateTime departmentEndDate;
    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "部門名", required = true)
    private String departmentName;
    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "組織階層", required = true)
    private Integer layer;
    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "部門パス", required = true)
    private String path;
    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "最下層区分", required = true)
    private Integer lowerType;
    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "伝票入力可否", required = true)
    private Integer slitYn;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public DepartmentDownloadCSV(String departmentCode, LocalDateTime departmentStartDate, LocalDateTime departmentEndDate, String departmentName, String path, int layer, int slitYn, int lowerType) {
        this.departmentCode = departmentCode;
        this.departmentStartDate = departmentStartDate;
        this.departmentEndDate = departmentEndDate;
        this.departmentName = departmentName;
        this.path = path;
        this.layer = layer;
        this.slitYn = slitYn;
        this.lowerType = lowerType;
    }
}
