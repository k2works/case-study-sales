package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.employee.EmployeeList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.EmployeeDownloadCSV;

import java.util.List;

public interface EmployeeCSVRepository {
    List<EmployeeDownloadCSV> convert(EmployeeList employeeList);

    int countBy(DownloadCriteria condition);

    EmployeeList selectBy(DownloadCriteria condition);
}
