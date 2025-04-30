package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.master.product.ProductList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.master.product.ProductCustomEntity;
import com.example.sms.infrastructure.datasource.master.product.ProductCustomMapper;
import com.example.sms.infrastructure.datasource.master.product.ProductEntityMapper;
import com.example.sms.service.system.download.ProductCSVRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ProductCSVDataSource implements ProductCSVRepository {
    private final ProductCustomMapper productCustomMapper;
    private final ProductEntityMapper productEntityMapper;

    public ProductCSVDataSource(ProductCustomMapper productCustomMapper, ProductEntityMapper productEntityMapper) {
        this.productCustomMapper = productCustomMapper;
        this.productEntityMapper = productEntityMapper;
    }

    @Override
    public List<ProductDownloadCSV> convert(ProductList productList) {
        if (productList != null) {
            return productList.asList().stream()
                    .map(productEntityMapper::mapToCsvModel)
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<ProductCustomEntity> productEntities = productCustomMapper.selectAll();
        return productEntities.size();
    }

    @Override
    public ProductList selectBy(DownloadCriteria condition) {
        List<ProductCustomEntity> productEntities = productCustomMapper.selectAll();
        return new ProductList(productEntities.stream()
                .map(ProductEntityMapper::mapToDomainModel)
                .toList());
    }
}