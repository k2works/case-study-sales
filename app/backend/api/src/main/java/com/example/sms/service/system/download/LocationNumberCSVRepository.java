package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.locationnumber.LocationNumberList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.LocationNumberDownloadCSV;

import java.util.List;

public interface LocationNumberCSVRepository {
    List<LocationNumberDownloadCSV> convert(LocationNumberList locationNumberList);

    int countBy(DownloadCriteria condition);

    LocationNumberList selectBy(DownloadCriteria condition);
}