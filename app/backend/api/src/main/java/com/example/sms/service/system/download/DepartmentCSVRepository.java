package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.infrastructure.datasource.system.download.DepartmentDownloadCSV;

import java.util.List;

public interface DepartmentCSVRepository {
    List<DepartmentDownloadCSV> convert(DepartmentList departmentList);

    int countBy(DownloadCondition condition);

    DepartmentList selectBy(DownloadCondition condition);
}
