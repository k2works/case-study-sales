package com.example.sms.infrastructure.datasource.master.product;

import com.example.sms.domain.model.master.product.Bom;
import com.example.sms.domain.model.master.product.CustomerSpecificSellingPrice;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.SubstituteProduct;
import com.example.sms.domain.type.product.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductEntityMapper {
    public 商品マスタ mapToEntity(Product product) {
        商品マスタ productEntity = new 商品マスタ();
        productEntity.set商品コード(product.getProductCode().getValue());
        productEntity.set商品正式名(product.getProductName().getProductFormalName());
        productEntity.set商品略称(product.getProductName().getProductAbbreviation());
        productEntity.set商品名カナ(product.getProductName().getProductNameKana());
        productEntity.set商品区分(product.getProductType().getCode());
        productEntity.set販売単価(product.getSellingPrice().getAmount());
        productEntity.set仕入単価(product.getPurchasePrice().getAmount());
        productEntity.set売上原価(product.getCostOfSales().getAmount());
        productEntity.set税区分(product.getTaxType().getCode());
        productEntity.set商品分類コード(product.getProductCategoryCode().getValue());
        productEntity.set雑区分(product.getMiscellaneousType().getCode());
        productEntity.set在庫管理対象区分(product.getStockManagementTargetType().getCode());
        productEntity.set在庫引当区分(product.getStockAllocationType().getCode());
        productEntity.set仕入先コード(product.getSupplierCode().getValue());
        productEntity.set仕入先枝番(product.getSupplierCode().getBranchNumber());

        return productEntity;
    }

    public 代替商品 mapToEntity(SubstituteProduct substituteProduct) {
        代替商品 substituteProductEntity = new 代替商品();
        substituteProductEntity.set商品コード(substituteProduct.getProductCode().getValue());
        substituteProductEntity.set代替商品コード(substituteProduct.getSubstituteProductCode().getValue());
        substituteProductEntity.set優先順位(substituteProduct.getPriority());

        return substituteProductEntity;
    }

    public 部品表 mapToEntity(Bom bom) {
        部品表 bomEntity = new 部品表();
        bomEntity.set商品コード(bom.getProductCode().getValue());
        bomEntity.set部品コード(bom.getComponentCode().getValue());
        bomEntity.set部品数量(bom.getComponentQuantity());

        return bomEntity;
    }

    public 顧客別販売単価 mapToEntity(CustomerSpecificSellingPrice customerSpecificSellingPrice) {
        顧客別販売単価 customerSellingPriceEntity = new 顧客別販売単価();
        customerSellingPriceEntity.set商品コード(customerSpecificSellingPrice.getProductCode().getValue());
        customerSellingPriceEntity.set取引先コード(customerSpecificSellingPrice.getCustomerCode());
        customerSellingPriceEntity.set販売単価(customerSpecificSellingPrice.getSellingPrice().getAmount());

        return customerSellingPriceEntity;
    }

    public Product mapToDomainModel(商品マスタ productEntity) {
        Product product = Product.of(
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

        List<SubstituteProduct> substituteProducts = productEntity.get代替商品().stream()
                .map(this::mapToSubstituteProduct)
                .toList();
        List<Bom> boms = productEntity.get部品表().stream()
                .map(this::mapToBom)
                .toList();
        List<CustomerSpecificSellingPrice> customerSpecificSellingPrices = productEntity.get顧客別販売単価().stream()
                .map(this::mapToCustomerSpecificSellingPrice)
                .toList();
        product = Product.of(product, substituteProducts, boms, customerSpecificSellingPrices);

        return product;
    }


    private SubstituteProduct mapToSubstituteProduct(代替商品 substituteProductEntity) {
        return SubstituteProduct.of(
                substituteProductEntity.get商品コード(),
                substituteProductEntity.get代替商品コード(),
                substituteProductEntity.get優先順位()
        );
    }

    private Bom mapToBom(部品表 bomEntity) {
        return Bom.of(
                bomEntity.get商品コード(),
                bomEntity.get部品コード(),
                bomEntity.get部品数量()
        );
    }

    private CustomerSpecificSellingPrice mapToCustomerSpecificSellingPrice(顧客別販売単価 顧客別販売単価) {
        return CustomerSpecificSellingPrice.of(
                顧客別販売単価.get商品コード(),
                顧客別販売単価.get取引先コード(),
                顧客別販売単価.get販売単価()
        );
    }
}
