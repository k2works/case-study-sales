package com.example.sms.infrastructure.datasource.master.product;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCategory;
import com.example.sms.domain.type.product.*;
import com.example.sms.infrastructure.datasource.system.download.ProductCategoryDownloadCSV;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductCategoryEntityMapper {

    public ProductCategory mapToDomainModel(商品分類マスタ productCategoryEntity) {
        ProductCategory productCategory = ProductCategory.of(
                productCategoryEntity.get商品分類コード(),
                productCategoryEntity.get商品分類名(),
                productCategoryEntity.get商品分類階層(),
                productCategoryEntity.get商品分類パス(),
                productCategoryEntity.get最下層区分()
        );

        List<Product> products = productCategoryEntity.get商品マスタ().stream()
                .map(this::mapToProduct)
                .toList();

        return ProductCategory.of(productCategory, products);
    }

    private Product mapToProduct(商品マスタ productEntity) {
        return Product.of(
                productEntity.get商品コード(),
                productEntity.get商品正式名(),
                productEntity.get商品略称(),
                productEntity.get商品名カナ(),
                ProductType.fromCode(productEntity.get商品区分()),
                productEntity.get販売単価(),
                productEntity.get仕入単価(),
                productEntity.get売上原価(),
                TaxType.fromCode(productEntity.get税区分()),
                productEntity.get商品分類コード(),
                MiscellaneousType.fromCode(productEntity.get雑区分()),
                StockManagementTargetType.fromCode(productEntity.get在庫管理対象区分()),
                StockAllocationType.fromCode(productEntity.get在庫引当区分()),
                productEntity.get仕入先コード(),
                productEntity.get仕入先枝番()
        );
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

    public ProductCategoryDownloadCSV mapToCsvModel(ProductCategory productCategory) {
        return new ProductCategoryDownloadCSV(
                productCategory.getProductCategoryCode().getValue(),
                productCategory.getProductCategoryName(),
                productCategory.getProductCategoryHierarchy(),
                productCategory.getProductCategoryPath(),
                productCategory.getLowestLevelDivision()
        );
    }
}
