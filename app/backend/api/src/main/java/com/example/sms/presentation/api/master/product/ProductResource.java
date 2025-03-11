package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Schema(description = "商品")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResource {
    @NotNull
    String productCode; // 商品コード
    @NotNull
    String productFormalName; // 商品正式名
    @NotNull
    String productAbbreviation; // 商品略称
    @NotNull
    String productNameKana; // 商品名カナ
    ProductType productType; // 商品区分
    @NotNull
    Integer sellingPrice; // 販売単価
    Integer purchasePrice; // 仕入単価
    @NotNull
    Integer costOfSales; // 売上原価
    @NotNull
    TaxType taxType; // 税区分
    String productClassificationCode; // 商品分類コード
    MiscellaneousType miscellaneousType; // 雑区分
    StockManagementTargetType stockManagementTargetType; // 在庫管理対象区分
    StockAllocationType stockAllocationType; // 在庫引当区分
    @NotNull
    String vendorCode; // 仕入先コード
    Integer vendorBranchNumber; // 仕入先枝番
    List<SubstituteProductResource> substituteProduct; // 代替商品
    List<BomResource> boms; // 部品表
    List<CustomerSpecificSellingPriceResource> customerSpecificSellingPrices; // 顧客別販売単価

    boolean addFlag;
    boolean deleteFlag;

    public static ProductResource from(Product product) {
        return ProductResource.builder()
                .productCode(product.getProductCode().getValue())
                .productFormalName(product.getProductName().getProductFormalName())
                .productAbbreviation(product.getProductName().getProductAbbreviation())
                .productNameKana(product.getProductName().getProductNameKana())
                .productType(product.getProductType())
                .sellingPrice(product.getSellingPrice().getAmount())
                .purchasePrice(product.getPurchasePrice().getAmount())
                .costOfSales(product.getCostOfSales().getAmount())
                .taxType(product.getTaxType())
                .productClassificationCode(product.getProductCategoryCode().getValue())
                .miscellaneousType(product.getMiscellaneousType())
                .stockManagementTargetType(product.getStockManagementTargetType())
                .stockAllocationType(product.getStockAllocationType())
                .vendorCode(product.getVendorCode().getCode().getValue())
                .vendorBranchNumber(product.getVendorCode().getBranchNumber())
                .substituteProduct(SubstituteProductResource.from(product.getSubstituteProduct()))
                .boms(BomResource.from(product.getBoms()))
                .customerSpecificSellingPrices(CustomerSpecificSellingPriceResource.from(product.getCustomerSpecificSellingPrices()))
                .addFlag(false)
                .deleteFlag(false)
                .build();
    }
}
