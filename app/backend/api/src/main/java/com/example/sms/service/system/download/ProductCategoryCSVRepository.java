package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.product.ProductCategoryList;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.infrastructure.datasource.master.product.ProductCategoryDownloadCSV;

import java.util.List;

public interface ProductCategoryCSVRepository {
    List<ProductCategoryDownloadCSV> convert(ProductCategoryList productCategoryList);

    int countBy(DownloadCondition condition);

    ProductCategoryList selectBy(DownloadCondition condition);
}
