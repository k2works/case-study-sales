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
    @Schema(description = "商品コード")
    String productCode;
    @NotNull
    @Schema(description = "商品正式名")
    String productFormalName;
    @NotNull
    @Schema(description = "商品略称")
    String productAbbreviation;
    @NotNull
    @Schema(description = "商品名カナ")
    String productNameKana;
    @Schema(description = "商品区分")
    ProductType productType;
    @NotNull
    @Schema(description = "販売単価")
    Integer sellingPrice;
    @Schema(description = "仕入単価")
    Integer purchasePrice;
    @NotNull
    @Schema(description = "売上原価")
    Integer costOfSales;
    @NotNull
    @Schema(description = "税区分")
    TaxType taxType;
    @Schema(description = "商品分類コード")
    String productClassificationCode;
    @Schema(description = "雑区分")
    MiscellaneousType miscellaneousType;
    @Schema(description = "在庫管理対象区分")
    StockManagementTargetType stockManagementTargetType;
    @Schema(description = "在庫引当区分")
    StockAllocationType stockAllocationType;
    @NotNull
    @Schema(description = "仕入先コード")
    String vendorCode;
    @Schema(description = "仕入先枝番")
    Integer vendorBranchNumber;
    @Schema(description = "代替商品")
    List<SubstituteProductResource> substituteProduct;
    @Schema(description = "部品表")
    List<BomResource> boms;
    @Schema(description = "顧客別販売単価")
    List<CustomerSpecificSellingPriceResource> customerSpecificSellingPrices;

    @Schema(description = "追加フラグ")
    boolean addFlag;
    @Schema(description = "削除フラグ")
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
