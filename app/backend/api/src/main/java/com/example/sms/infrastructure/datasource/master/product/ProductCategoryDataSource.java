package com.example.sms.infrastructure.datasource.master.product;

import com.example.sms.domain.model.master.product.ProductCategory;
import com.example.sms.domain.model.master.product.ProductCategoryList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.service.master.product.ProductCategoryRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductCategoryDataSource implements ProductCategoryRepository {
    final 商品分類マスタMapper productCategoryMapper;
    final ProductCategoryEntityMapper productCategoryEntityMapper;

    public ProductCategoryDataSource(商品分類マスタMapper productCategoryMapper, ProductCategoryEntityMapper productCategoryEntityMapper) {
        this.productCategoryMapper = productCategoryMapper;
        this.productCategoryEntityMapper = productCategoryEntityMapper;
    }

    @Override
    public void deleteAll() {
        productCategoryMapper.deleteAll();
    }

    @Override
    public void save(ProductCategory product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        Optional<商品分類マスタ> productCategoryEntity = Optional.ofNullable(productCategoryMapper.selectByPrimaryKey(product.getProductCategoryCode().getValue()));
        商品分類マスタ newProductCategoryEntity = productCategoryEntityMapper.mapToEntity(product);
        if (productCategoryEntity.isEmpty()) {
            newProductCategoryEntity.set作成日時(LocalDateTime.now());
            newProductCategoryEntity.set作成者名(username);
            newProductCategoryEntity.set更新日時(LocalDateTime.now());
            newProductCategoryEntity.set更新者名(username);
            productCategoryMapper.insert(newProductCategoryEntity);
        } else {
            newProductCategoryEntity.set作成日時(productCategoryEntity.get().get作成日時());
            newProductCategoryEntity.set作成者名(productCategoryEntity.get().get作成者名());
            newProductCategoryEntity.set更新日時(LocalDateTime.now());
            newProductCategoryEntity.set更新者名(username);
            productCategoryMapper.updateByPrimaryKey(newProductCategoryEntity);
        }
    }

    @Override
    public ProductCategoryList selectAll() {
        List<商品分類マスタ> productCategoryEntities = productCategoryMapper.selectAll();

        return new ProductCategoryList(productCategoryEntities.stream()
                .map(productCategoryEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public PageInfo<ProductCategory> selectAllWithPageInfo() {
        List<商品分類マスタ> productEntities = productCategoryMapper.selectAll();
        PageInfo<商品分類マスタ> pageInfo = new PageInfo<>(productEntities);

        return PageInfoHelper.of(pageInfo, productCategoryEntityMapper::mapToDomainModel);
    }

    @Override
    public Optional<ProductCategory> findById(String productCategoryCode) {
        商品分類マスタ productCategoryEntity = productCategoryMapper.selectByPrimaryKey(productCategoryCode);
        if (productCategoryEntity != null) {
            return Optional.of(productCategoryEntityMapper.mapToDomainModel(productCategoryEntity));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(ProductCategory productCategory) {
        productCategoryMapper.deleteByPrimaryKey(productCategory.getProductCategoryCode().getValue());
    }
}
