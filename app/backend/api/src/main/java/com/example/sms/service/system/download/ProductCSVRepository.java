package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.product.ProductList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.ProductDownloadCSV;

import java.util.List;

public interface ProductCSVRepository {
    List<ProductDownloadCSV> convert(ProductList productList);

    int countBy(DownloadCriteria condition);

    ProductList selectBy(DownloadCriteria condition);
}
