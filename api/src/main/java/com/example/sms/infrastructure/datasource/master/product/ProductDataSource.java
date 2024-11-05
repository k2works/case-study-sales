package com.example.sms.infrastructure.datasource.master.product;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductList;
import com.example.sms.service.master.product.ProductRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductDataSource implements ProductRepository {
    final 商品マスタMapper productMapper;
    final 代替商品Mapper substituteProductMapper;
    final 部品表Mapper bomMapper;
    final 顧客別販売単価Mapper customerSellingPriceMapper;
    final ProductEntityMapper productEntityMapper;

    public ProductDataSource(商品マスタMapper productMapper, 代替商品Mapper substituteProductMapper, 部品表Mapper bomMapper, 顧客別販売単価Mapper customerSellingPriceMapper, ProductEntityMapper productEntityMapper) {
        this.productMapper = productMapper;
        this.substituteProductMapper = substituteProductMapper;
        this.bomMapper = bomMapper;
        this.customerSellingPriceMapper = customerSellingPriceMapper;
        this.productEntityMapper = productEntityMapper;
    }

    @Override
    public void deleteAll() {
        customerSellingPriceMapper.deleteAll();
        bomMapper.deleteAll();
        substituteProductMapper.deleteAll();
        productMapper.deleteAll();
    }

    @Override
    public void save(Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        Optional<商品マスタ> productEntity = Optional.ofNullable(productMapper.selectByPrimaryKey(product.getProductCode()));
        if (productEntity.isEmpty()) {
            商品マスタ newProductEntity = productEntityMapper.mapToEntity(product);
            newProductEntity.set作成日時(LocalDateTime.now());
            newProductEntity.set作成者名(username);
            newProductEntity.set更新日時(LocalDateTime.now());
            newProductEntity.set更新者名(username);
            productMapper.insert(newProductEntity);

            if (product.getSubstituteProduct() != null) {
                product.getSubstituteProduct().forEach(substituteProduct -> {
                    代替商品 substituteProductEntity = productEntityMapper.mapToEntity(substituteProduct);
                    substituteProductEntity.set作成日時(LocalDateTime.now());
                    substituteProductEntity.set作成者名(username);
                    substituteProductEntity.set更新日時(LocalDateTime.now());
                    substituteProductEntity.set更新者名(username);
                    substituteProductMapper.insert(substituteProductEntity);
                });
            }
        } else {
            商品マスタ updateProductEntity = productEntityMapper.mapToEntity(product);
            updateProductEntity.set作成日時(productEntity.get().get作成日時());
            updateProductEntity.set作成者名(productEntity.get().get作成者名());
            updateProductEntity.set更新日時(LocalDateTime.now());
            updateProductEntity.set更新者名(username);
            productMapper.updateByPrimaryKey(updateProductEntity);

            if (product.getSubstituteProduct() != null) {
                product.getSubstituteProduct().forEach(substituteProduct -> {
                    代替商品Key key = new 代替商品Key();
                    key.set代替商品コード(substituteProduct.getSubstituteProductKey().getSubstituteProductCode());
                    key.set商品コード(substituteProduct.getSubstituteProductKey().getProductCode());
                    substituteProductMapper.deleteByPrimaryKey(key);

                    代替商品 substituteProductEntity = productEntityMapper.mapToEntity(substituteProduct);
                    substituteProductEntity.set作成日時(LocalDateTime.now());
                    substituteProductEntity.set作成者名(username);
                    substituteProductEntity.set更新日時(LocalDateTime.now());
                    substituteProductEntity.set更新者名(username);
                    substituteProductMapper.insert(substituteProductEntity);
                });
            }
        }
    }

    @Override
    public ProductList selectAll() {
        List<商品マスタ> productEntities = productMapper.selectAll();

        return new ProductList(productEntities.stream()
                .map(productEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public Optional<Product> findById(String productCode) {
        商品マスタ productEntity = productMapper.selectByPrimaryKey(productCode);
        if (productEntity != null) {
            return Optional.of(productEntityMapper.mapToDomainModel(productEntity));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Product product) {
        if (!product.getSubstituteProduct().isEmpty()) {
            product.getSubstituteProduct().forEach(substituteProduct -> {
                代替商品Key key = new 代替商品Key();
                key.set商品コード(substituteProduct.getSubstituteProductKey().getProductCode());
                key.set代替商品コード(substituteProduct.getSubstituteProductKey().getSubstituteProductCode());
                substituteProductMapper.deleteByPrimaryKey(key);
            });
        }

        productMapper.deleteByPrimaryKey(product.getProductCode());
    }
}
