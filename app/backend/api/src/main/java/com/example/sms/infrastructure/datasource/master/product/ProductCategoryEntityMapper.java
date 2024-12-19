package com.example.sms.infrastructure.datasource.master.product;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCategory;
import com.example.sms.domain.type.product.*;
import com.example.sms.infrastructure.datasource.autogen.model.商品分類マスタ;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class ProductCategoryEntityMapper {

    public ProductCategory mapToDomainModel(ProductCategoryCustomEntity productCategoryEntity) {
        Function<商品マスタ, Product> mapToProduct = p ->  (
                Product.of(
                        p.get商品コード(),
                        p.get商品正式名(),
                        p.get商品略称(),
                        p.get商品名カナ(),
                        ProductType.fromCode(p.get商品区分()),
                        p.get販売単価(),
                        p.get仕入単価(),
                        p.get売上原価(),
                        TaxType.fromCode(p.get税区分()),
                        p.get商品分類コード(),
                        MiscellaneousType.fromCode(p.get雑区分()),
                        StockManagementTargetType.fromCode(p.get在庫管理対象区分()),
                        StockAllocationType.fromCode(p.get在庫引当区分()),
                        p.get仕入先コード(),
                        p.get仕入先枝番()
                )
        );

        ProductCategory productCategory = ProductCategory.of(
                productCategoryEntity.get商品分類コード(),
                productCategoryEntity.get商品分類名(),
                productCategoryEntity.get商品分類階層(),
                productCategoryEntity.get商品分類パス(),
                productCategoryEntity.get最下層区分()
        );

        List<Product> products = productCategoryEntity.get商品マスタ().stream()
                .map(mapToProduct)
                .toList();

        return ProductCategory.of(productCategory, products);
    }

    public 商品分類マスタ mapToEntity(ProductCategory product) {
        商品分類マスタ productCategoryEntity = new 商品分類マスタ();
        productCategoryEntity.set商品分類コード(product.getProductCategoryCode().getValue());
        productCategoryEntity.set商品分類名(product.getProductCategoryName());
        productCategoryEntity.set商品分類階層(product.getProductCategoryHierarchy());
        productCategoryEntity.set商品分類パス(product.getProductCategoryPath());
        productCategoryEntity.set最下層区分(product.getLowestLevelDivision());

        return productCategoryEntity;
    }
}
