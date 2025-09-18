package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.warehouse.WarehouseList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.WarehouseDownloadCSV;

import java.util.List;

public interface WarehouseCSVRepository {
    List<WarehouseDownloadCSV> convert(WarehouseList warehouseList);

    int countBy(DownloadCriteria condition);

    WarehouseList selectBy(DownloadCriteria condition);
}