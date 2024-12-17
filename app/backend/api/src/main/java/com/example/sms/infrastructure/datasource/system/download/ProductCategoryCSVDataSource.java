package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.master.product.ProductCategoryList;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.infrastructure.datasource.master.product.ProductCategoryEntityMapper;
import com.example.sms.infrastructure.datasource.master.product.商品分類マスタ;
import com.example.sms.infrastructure.datasource.master.product.商品分類マスタMapper;
import com.example.sms.service.system.download.ProductCategoryCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductCategoryCSVDataSource implements ProductCategoryCSVRepository {
    private final 商品分類マスタMapper productCategoryMapper;
    private final ProductCategoryEntityMapper productCategoryEntityMapper;

    public ProductCategoryCSVDataSource(商品分類マスタMapper productCategoryMapper, ProductCategoryEntityMapper productCategoryEntityMapper) {
        this.productCategoryMapper = productCategoryMapper;
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
        List<商品分類マスタ> productCategoryEntities = productCategoryMapper.selectAll();
        return productCategoryEntities.size();
    }

    @Override
    public ProductCategoryList selectBy(DownloadCondition condition) {
        List<商品分類マスタ> productCategoryEntities = productCategoryMapper.selectAll();
        return new ProductCategoryList(productCategoryEntities.stream()
                .map(productCategoryEntityMapper::mapToDomainModel)
                .toList());
    }
}