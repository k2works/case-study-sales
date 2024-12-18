package com.example.sms.infrastructure.datasource.master.product;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductList;
import com.example.sms.domain.type.product.ProductType;
import com.example.sms.infrastructure.PageInfoHelper;
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

        Optional<商品マスタ> productEntity = Optional.ofNullable(productMapper.selectByPrimaryKey(product.getProductCode().getValue()));
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
        } else {
            商品マスタ updateProductEntity = productEntityMapper.mapToEntity(product);
            updateProductEntity.set作成日時(productEntity.get().get作成日時());
            updateProductEntity.set作成者名(productEntity.get().get作成者名());
            updateProductEntity.set更新日時(LocalDateTime.now());
            updateProductEntity.set更新者名(username);
            productMapper.updateByPrimaryKey(updateProductEntity);

            if (product.getSubstituteProduct() != null) {
                substituteProductMapper.deleteByProductCode(product.getProductCode().getValue());

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
                bomMapper.deleteByProductCode(product.getProductCode().getValue());

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
                customerSellingPriceMapper.deleteByProductCode(product.getProductCode().getValue());

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
    }

    @Override
    public ProductList selectAll() {
        List<商品マスタ> productEntities = productMapper.selectAll();

        return new ProductList(productEntities.stream()
                .map(productEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public PageInfo<Product> selectAllWithPageInfo() {
        List<商品マスタ> productEntities = productMapper.selectAll();
        PageInfo<商品マスタ> pageInfo = new PageInfo<>(productEntities);

        return PageInfoHelper.of(pageInfo, productEntityMapper::mapToDomainModel);
    }

    @Override
    public PageInfo<Product> selectAllBoms() {
        List<商品マスタ> productEntities = productMapper.selectAllBoms(List.of(ProductType.製品.getCode(), ProductType.部品.getCode(), ProductType.包材.getCode()));
        PageInfo<商品マスタ> pageInfo = new PageInfo<>(productEntities);

        return PageInfoHelper.of(pageInfo, productEntityMapper::mapToDomainModel);
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
