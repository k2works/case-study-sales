package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.employee.EmployeeList;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.infrastructure.datasource.system.download.EmployeeDownloadCSV;

import java.util.List;

public interface EmployeeCSVRepository {
    List<EmployeeDownloadCSV> convert(EmployeeList employeeList);

    int countBy(DownloadCondition condition);

    EmployeeList selectBy(DownloadCondition condition);
}
