package com.example.sms.infrastructure.datasource.master.product;

import com.example.sms.domain.model.master.product.Bom;
import com.example.sms.domain.model.master.product.CustomerSpecificSellingPrice;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.SubstituteProduct;
import com.example.sms.domain.model.master.product.*;
import com.example.sms.infrastructure.datasource.system.download.ProductDownloadCSV;
import com.example.sms.infrastructure.datasource.autogen.model.代替商品;
import com.example.sms.infrastructure.datasource.autogen.model.商品マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.部品表;
import com.example.sms.infrastructure.datasource.autogen.model.顧客別販売単価;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

//TODO:製品型番追加
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
        productEntity.set仕入先コード(product.getVendorCode().getCode().getValue());
        productEntity.set仕入先枝番(product.getVendorCode().getBranchNumber());

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
        bomEntity.set部品数量(bom.getComponentQuantity().getAmount());

        return bomEntity;
    }

    public 顧客別販売単価 mapToEntity(CustomerSpecificSellingPrice customerSpecificSellingPrice) {
        顧客別販売単価 customerSellingPriceEntity = new 顧客別販売単価();
        customerSellingPriceEntity.set商品コード(customerSpecificSellingPrice.getProductCode().getValue());
        customerSellingPriceEntity.set取引先コード(customerSpecificSellingPrice.getCustomerCode());
        customerSellingPriceEntity.set販売単価(customerSpecificSellingPrice.getSellingPrice().getAmount());

        return customerSellingPriceEntity;
    }

    public static Product mapToDomainModel(ProductCustomEntity productEntity) {
        Function<代替商品, SubstituteProduct> mapToSubstituteProduct = s -> (
                SubstituteProduct.of(
                        s.get商品コード(),
                        s.get代替商品コード(),
                        s.get優先順位()
                )
        );

        Function<部品表, Bom> mapToBom = b -> (
                Bom.of(
                        b.get商品コード(),
                        b.get部品コード(),
                        b.get部品数量()
                )
        );

        Function<顧客別販売単価, CustomerSpecificSellingPrice> mapToCustomerSpecificSellingPrice = c -> (
                CustomerSpecificSellingPrice.of(
                        c.get商品コード(),
                        c.get取引先コード(),
                        c.get販売単価()
                )
        );

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
                .map(mapToSubstituteProduct)
                .toList();
        List<Bom> boms = productEntity.get部品表().stream()
                .map(mapToBom)
                .toList();
        List<CustomerSpecificSellingPrice> customerSpecificSellingPrices = productEntity.get顧客別販売単価().stream()
                .map(mapToCustomerSpecificSellingPrice)
                .toList();
        product = Product.of(product, substituteProducts, boms, customerSpecificSellingPrices);

        return product;
    }

    public ProductDownloadCSV mapToCsvModel(Product product) {
        return new ProductDownloadCSV(
                product.getProductCode().getValue(),
                product.getProductName().getProductFormalName(),
                product.getProductName().getProductAbbreviation(),
                product.getProductName().getProductNameKana(),
                product.getProductType().getCode(),
                "",
                product.getSellingPrice().getAmount(),
                product.getPurchasePrice().getAmount(),
                product.getCostOfSales().getAmount(),
                product.getTaxType().getCode(),
                product.getProductCategoryCode().getValue(),
                product.getMiscellaneousType().getCode(),
                product.getStockManagementTargetType().getCode(),
                product.getStockAllocationType().getCode(),
                product.getVendorCode().getCode().getValue(),
                product.getVendorCode().getBranchNumber()
        );
    }
}
