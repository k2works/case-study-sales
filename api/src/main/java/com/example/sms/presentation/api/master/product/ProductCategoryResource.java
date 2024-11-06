package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Schema(description = "商品分類")
public class ProductCategoryResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    String productCategoryCode; // 商品分類コード
    String productCategoryName; // 商品分類名
    @NotNull
    Integer productCategoryHierarchy; // 商品分類階層
    String productCategoryPath; // 商品分類パス
    Integer lowestLevelDivision; // 最下層区分
    List<Product> Products; // 商品
}
