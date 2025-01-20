package com.example.sms.infrastructure.datasource.master.product;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductList;
import com.example.sms.domain.model.master.product.ProductType;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.代替商品Mapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.商品マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.部品表Mapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.顧客別販売単価Mapper;
import com.example.sms.infrastructure.datasource.autogen.model.*;
import com.example.sms.infrastructure.datasource.master.product.bom.BomCustomMapper;
import com.example.sms.infrastructure.datasource.master.product.customer_specific_price.CustomerSpecificSellingPriceCustomMapper;
import com.example.sms.infrastructure.datasource.master.product.substitute.SubstituteProductCustomMapper;
import com.example.sms.service.master.product.ProductRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductDataSource implements ProductRepository {
    final 商品マスタMapper productMapper;
    final ProductCustomMapper productCustomMapper;
    final 代替商品Mapper substituteProductMapper;
    final SubstituteProductCustomMapper substituteProductCustomMapper;
    final 部品表Mapper bomMapper;
    final BomCustomMapper bomCustomMapper;
    final 顧客別販売単価Mapper customerSellingPriceMapper;
    final CustomerSpecificSellingPriceCustomMapper customerSpecificSellingPriceCustomMapper;
    final ProductEntityMapper productEntityMapper;

    public ProductDataSource(商品マスタMapper productMapper, ProductCustomMapper productCustomMapper, 代替商品Mapper substituteProductMapper, SubstituteProductCustomMapper substituteProductCustomMapper, 部品表Mapper bomMapper, BomCustomMapper bomCustomMapper, 顧客別販売単価Mapper customerSellingPriceMapper, CustomerSpecificSellingPriceCustomMapper customerSpecificSellingPriceCustomMapper, ProductEntityMapper productEntityMapper) {
        this.productMapper = productMapper;
        this.productCustomMapper = productCustomMapper;
        this.substituteProductMapper = substituteProductMapper;
        this.substituteProductCustomMapper = substituteProductCustomMapper;
        this.bomMapper = bomMapper;
        this.bomCustomMapper = bomCustomMapper;
        this.customerSellingPriceMapper = customerSellingPriceMapper;
        this.customerSpecificSellingPriceCustomMapper = customerSpecificSellingPriceCustomMapper;
        this.productEntityMapper = productEntityMapper;
    }

    @Override
    public void deleteAll() {
        customerSpecificSellingPriceCustomMapper.deleteAll();
        bomCustomMapper.deleteAll();
        substituteProductCustomMapper.deleteAll();
        productCustomMapper.deleteAll();
    }

    @Override
    public void save(Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        Optional<ProductCustomEntity> productEntity = Optional.ofNullable(productCustomMapper.selectByPrimaryKey(product.getProductCode().getValue()));
        if (productEntity.isEmpty()) {
            createProduct(product, username);
        } else {
            updateProduct(product, productEntity, username);
        }
    }

    private void updateProduct(Product product, Optional<ProductCustomEntity> productEntity, String username) {
        商品マスタ updateProductEntity = productEntityMapper.mapToEntity(product);
        updateProductEntity.set作成日時(productEntity.get().get作成日時());
        updateProductEntity.set作成者名(productEntity.get().get作成者名());
        updateProductEntity.set更新日時(LocalDateTime.now());
        updateProductEntity.set更新者名(username);
        productMapper.updateByPrimaryKey(updateProductEntity);

        if (product.getSubstituteProduct() != null) {
            substituteProductCustomMapper.deleteByProductCode(product.getProductCode().getValue());

            product.getSubstituteProduct().forEach(substituteProduct -> {
                代替商品Key key = new 代替商品Key();
                key.set代替商品コード(substituteProduct.getSubstituteProductCode().getValue());
                key.set商品コード(substituteProduct.getProductCode().getValue());
                substituteProductMapper.deleteByPrimaryKey(key);

                代替商品 substituteProductEntity = productEntityMapper.mapToEntity(substituteProduct);
                substituteProductEntity.set作成日時(LocalDateTime.now());
                substituteProductEntity.set作成者名(username);
                substituteProductEntity.set更新日時(LocalDateTime.now());
                substituteProductEntity.set更新者名(username);
                substituteProductMapper.insert(substituteProductEntity);
            });
        }

        if (product.getBoms() != null) {
            bomCustomMapper.deleteByProductCode(product.getProductCode().getValue());

            product.getBoms().forEach(bom -> {
                部品表Key key = new 部品表Key();
                key.set商品コード(bom.getProductCode().getValue());
                key.set部品コード(bom.getComponentCode().getValue());
                bomMapper.deleteByPrimaryKey(key);

                部品表 bomEntity = productEntityMapper.mapToEntity(bom);
                bomEntity.set作成日時(LocalDateTime.now());
                bomEntity.set作成者名(username);
                bomEntity.set更新日時(LocalDateTime.now());
                bomEntity.set更新者名(username);
                bomMapper.insert(bomEntity);
            });
        }

        if (product.getCustomerSpecificSellingPrices() != null) {
            customerSpecificSellingPriceCustomMapper.deleteByProductCode(product.getProductCode().getValue());

            product.getCustomerSpecificSellingPrices().forEach(customerSpecificSellingPrice -> {
                顧客別販売単価Key key = new 顧客別販売単価Key();
                key.set商品コード(customerSpecificSellingPrice.getProductCode().getValue());
                key.set取引先コード(customerSpecificSellingPrice.getCustomerCode());
                customerSellingPriceMapper.deleteByPrimaryKey(key);

                顧客別販売単価 customerSellingPriceEntity = productEntityMapper.mapToEntity(customerSpecificSellingPrice);
                customerSellingPriceEntity.set作成日時(LocalDateTime.now());
                customerSellingPriceEntity.set作成者名(username);
                customerSellingPriceEntity.set更新日時(LocalDateTime.now());
                customerSellingPriceEntity.set更新者名(username);
                customerSellingPriceMapper.insert(customerSellingPriceEntity);
            });
        }
    }

    private void createProduct(Product product, String username) {
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

        if (product.getBoms() != null) {
            product.getBoms().forEach(bom -> {
                部品表 bomEntity = productEntityMapper.mapToEntity(bom);
                bomEntity.set作成日時(LocalDateTime.now());
                bomEntity.set作成者名(username);
                bomEntity.set更新日時(LocalDateTime.now());
                bomEntity.set更新者名(username);
                bomMapper.insert(bomEntity);
            });
        }

        if (product.getCustomerSpecificSellingPrices() != null) {
            product.getCustomerSpecificSellingPrices().forEach(customerSpecificSellingPrice -> {
                顧客別販売単価 customerSellingPriceEntity = productEntityMapper.mapToEntity(customerSpecificSellingPrice);
                customerSellingPriceEntity.set作成日時(LocalDateTime.now());
                customerSellingPriceEntity.set作成者名(username);
                customerSellingPriceEntity.set更新日時(LocalDateTime.now());
                customerSellingPriceEntity.set更新者名(username);
                customerSellingPriceMapper.insert(customerSellingPriceEntity);
            });
        }
    }

    @Override
    public ProductList selectAll() {
        List<ProductCustomEntity> productEntities = productCustomMapper.selectAll();

        return new ProductList(productEntities.stream()
                .map(productEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public PageInfo<Product> selectAllWithPageInfo() {
        List<ProductCustomEntity> productEntities = productCustomMapper.selectAll();
        PageInfo<ProductCustomEntity> pageInfo = new PageInfo<>(productEntities);

        return PageInfoHelper.of(pageInfo, productEntityMapper::mapToDomainModel);
    }

    @Override
    public PageInfo<Product> selectAllBoms() {
        List<ProductCustomEntity> productEntities = productCustomMapper.selectAllBoms(List.of(ProductType.製品.getCode(), ProductType.部品.getCode(), ProductType.包材.getCode()));
        PageInfo<ProductCustomEntity> pageInfo = new PageInfo<>(productEntities);

        return PageInfoHelper.of(pageInfo, productEntityMapper::mapToDomainModel);
    }

    @Override
    public Optional<Product> findById(String productCode) {
        ProductCustomEntity productEntity = productCustomMapper.selectByPrimaryKey(productCode);
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
                key.set商品コード(substituteProduct.getProductCode().getValue());
                key.set代替商品コード(substituteProduct.getSubstituteProductCode().getValue());
                substituteProductMapper.deleteByPrimaryKey(key);
            });
        }

        if (!product.getBoms().isEmpty()) {
            product.getBoms().forEach(bom -> {
                部品表Key key = new 部品表Key();
                key.set商品コード(bom.getProductCode().getValue());
                key.set部品コード(bom.getComponentCode().getValue());
                bomMapper.deleteByPrimaryKey(key);
            });
        }

        if (!product.getCustomerSpecificSellingPrices().isEmpty()) {
            product.getCustomerSpecificSellingPrices().forEach(customerSpecificSellingPrice -> {
                顧客別販売単価Key key = new 顧客別販売単価Key();
                key.set商品コード(customerSpecificSellingPrice.getProductCode().getValue());
                key.set取引先コード(customerSpecificSellingPrice.getCustomerCode());
                customerSellingPriceMapper.deleteByPrimaryKey(key);
            });
        }

        productMapper.deleteByPrimaryKey(product.getProductCode().getValue());
    }
}
