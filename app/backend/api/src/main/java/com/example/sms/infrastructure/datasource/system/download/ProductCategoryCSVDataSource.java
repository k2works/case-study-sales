package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.master.product.ProductCategoryList;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.infrastructure.datasource.autogen.mapper.商品分類マスタMapper;
import com.example.sms.infrastructure.datasource.master.product_category.ProductCategoryCustomEntity;
import com.example.sms.infrastructure.datasource.master.product_category.ProductCategoryCustomMapper;
import com.example.sms.infrastructure.datasource.master.product_category.ProductCategoryEntityMapper;
import com.example.sms.service.system.download.ProductCategoryCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductCategoryCSVDataSource implements ProductCategoryCSVRepository {
    private final ProductCategoryCustomMapper productCategoryCustomMapper;
    private final ProductCategoryEntityMapper productCategoryEntityMapper;

    public ProductCategoryCSVDataSource(ProductCategoryCustomMapper productCategoryCustomMapper, ProductCategoryEntityMapper productCategoryEntityMapper) {
        this.productCategoryCustomMapper = productCategoryCustomMapper;
        this.productCategoryEntityMapper = productCategoryEntityMapper;
    }


    @Override
    public List<ProductCategoryDownloadCSV> convert(ProductCategoryList productCategoryList) {
        if (productCategoryList != null) {
            return productCategoryList.asList().stream()
                    .map(productCategoryEntityMapper::mapToCsvModel)
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCondition condition) {
        List<ProductCategoryCustomEntity> productCategoryEntities = productCategoryCustomMapper.selectAll();
        return productCategoryEntities.size();
    }

    @Override
    public ProductCategoryList selectBy(DownloadCondition condition) {
        List<ProductCategoryCustomEntity> productCategoryEntities = productCategoryCustomMapper.selectAll();
        return new ProductCategoryList(productCategoryEntities.stream()
                .map(productCategoryEntityMapper::mapToDomainModel)
                .toList());
    }
}