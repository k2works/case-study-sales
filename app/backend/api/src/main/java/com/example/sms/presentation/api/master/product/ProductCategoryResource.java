package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Schema(description = "商品分類")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryResource {
    @NotNull
    String productCategoryCode; // 商品分類コード
    String productCategoryName; // 商品分類名
    @NotNull
    Integer productCategoryHierarchy; // 商品分類階層
    String productCategoryPath; // 商品分類パス
    Integer lowestLevelDivision; // 最下層区分
    List<ProductResource> Products; // 商品

    public static ProductCategoryResource from(ProductCategory productCategory) {
        return ProductCategoryResource.builder()
                .productCategoryCode(productCategory.getProductCategoryCode().getValue())
                .productCategoryName(productCategory.getProductCategoryName())
                .productCategoryHierarchy(productCategory.getProductCategoryHierarchy())
                .productCategoryPath(productCategory.getProductCategoryPath())
                .lowestLevelDivision(productCategory.getLowestLevelDivision())
                .Products(productCategory.getProducts().stream().map(ProductResource::from).toList())
                .build();
    }
}
