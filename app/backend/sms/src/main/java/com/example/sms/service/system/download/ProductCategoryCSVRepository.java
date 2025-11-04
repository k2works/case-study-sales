package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.product.ProductCategoryList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.ProductCategoryDownloadCSV;

import java.util.List;

public interface ProductCategoryCSVRepository {
    List<ProductCategoryDownloadCSV> convert(ProductCategoryList productCategoryList);

    int countBy(DownloadCriteria condition);

    ProductCategoryList selectBy(DownloadCriteria condition);
}
