package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.CustomerSpecificSellingPrice;
import com.example.sms.domain.type.money.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Schema(description = "顧客別販売単価")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSpecificSellingPriceResource {
    @Schema(description = "商品コード")
    String productCode;
    @Schema(description = "顧客コード")
    String customerCode;
    @Schema(description = "販売単価")
    Money sellingPrice;

    public static CustomerSpecificSellingPriceResource from(CustomerSpecificSellingPrice customerSpecificSellingPrice) {
        return CustomerSpecificSellingPriceResource.builder()
                .productCode(customerSpecificSellingPrice.getProductCode().getValue())
                .customerCode(customerSpecificSellingPrice.getCustomerCode())
                .sellingPrice(customerSpecificSellingPrice.getSellingPrice())
                .build();
    }

    public static List<CustomerSpecificSellingPriceResource> from(List<CustomerSpecificSellingPrice> customerSpecificSellingPrices) {
        return customerSpecificSellingPrices.stream().map(CustomerSpecificSellingPriceResource::from).toList();
    }
}
