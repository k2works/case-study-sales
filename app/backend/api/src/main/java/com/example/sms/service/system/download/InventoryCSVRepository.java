package com.example.sms.service.system.download;

import com.example.sms.domain.model.inventory.InventoryList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.InventoryDownloadCSV;

import java.util.List;

public interface InventoryCSVRepository {
    List<InventoryDownloadCSV> convert(InventoryList inventoryList);
    int countBy(DownloadCriteria condition);
    InventoryList selectBy(DownloadCriteria condition);
}
