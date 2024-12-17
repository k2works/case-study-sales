package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.master.product.ProductList;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.infrastructure.datasource.master.product.ProductEntityMapper;
import com.example.sms.infrastructure.datasource.master.product.商品マスタ;
import com.example.sms.infrastructure.datasource.master.product.商品マスタMapper;
import com.example.sms.service.system.download.ProductCSVRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ProductCSVDataSource implements ProductCSVRepository {
    private final 商品マスタMapper productMapper;
    private final ProductEntityMapper productEntityMapper;

    public ProductCSVDataSource(商品マスタMapper productMapper, ProductEntityMapper productEntityMapper) {
        this.productMapper = productMapper;
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
    public int countBy(DownloadCondition condition) {
        List<商品マスタ> productEntities = productMapper.selectAll();
        return productEntities.size();
    }

    @Override
    public ProductList selectBy(DownloadCondition condition) {
        List<商品マスタ> productEntities = productMapper.selectAll();
        return new ProductList(productEntities.stream()
                .map(productEntityMapper::mapToDomainModel)
                .toList());
    }
}