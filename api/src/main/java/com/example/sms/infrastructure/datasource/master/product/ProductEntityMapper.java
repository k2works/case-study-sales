package com.example.sms.infrastructure.datasource.master.product;

import com.example.sms.domain.model.master.product.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityMapper {
    public 商品マスタ mapToEntity(Product product) {
        商品マスタ productEntity = new 商品マスタ();
        productEntity.set商品コード(product.getProductCode());
        productEntity.set商品正式名(product.getProductFormalName());
        productEntity.set商品略称(product.getProductAbbreviation());
        productEntity.set商品名カナ(product.getProductNameKana());
        productEntity.set商品区分(product.getProductCategory());
        productEntity.set販売単価(product.getSellingPrice());
        productEntity.set仕入単価(product.getPurchasePrice());
        productEntity.set売上原価(product.getCostOfSales());
        productEntity.set税区分(product.getTaxCategory());
        productEntity.set商品分類コード(product.getProductClassificationCode());
        productEntity.set雑区分(product.getMiscellaneousCategory());
        productEntity.set在庫管理対象区分(product.getStockManagementTargetCategory());
        productEntity.set在庫引当区分(product.getStockAllocationCategory());
        productEntity.set仕入先コード(product.getSupplierCode());
        productEntity.set仕入先枝番(product.getSupplierBranchNumber());

        return productEntity;
    }

    public Product mapToDomainModel(商品マスタ productEntity) {
        return Product.of(
                productEntity.get商品コード(),
                productEntity.get商品正式名(),
                productEntity.get商品略称(),
                productEntity.get商品名カナ(),
                productEntity.get商品区分(),
                productEntity.get販売単価(),
                productEntity.get仕入単価(),
                productEntity.get売上原価(),
                productEntity.get税区分(),
                productEntity.get商品分類コード(),
                productEntity.get雑区分(),
                productEntity.get在庫管理対象区分(),
                productEntity.get在庫引当区分(),
                productEntity.get仕入先コード(),
                productEntity.get仕入先枝番()
        );
    }
}
