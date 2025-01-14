package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.Bom;
import com.example.sms.domain.model.master.product.CustomerSpecificSellingPrice;
import com.example.sms.domain.model.master.product.SubstituteProduct;
import com.example.sms.domain.type.product.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Schema(description = "商品")
public class ProductResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
    Integer supplierBranchNumber; // 仕入先枝番
    List<SubstituteProduct> substituteProduct; // 代替商品
    List<Bom> boms; // 部品表
    List<CustomerSpecificSellingPrice> customerSpecificSellingPrices; // 顧客別販売単価

    boolean addFlag;
    boolean deleteFlag;
}
