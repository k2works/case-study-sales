package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.Bom;
import com.example.sms.domain.type.quantity.Quantity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Schema(description = "部品表")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BomResource {
    String productCode; // 商品コード
    String componentCode; // 部品コード
    Quantity componentQuantity; // 部品数量

    public static BomResource from(Bom bom) {
        return BomResource.builder()
                .productCode(bom.getProductCode().getValue())
                .componentCode(bom.getComponentCode().getValue())
                .componentQuantity(bom.getComponentQuantity())
                .build();
    }

    public static List<BomResource> from(List<Bom> boms) {
        return boms.stream().map(BomResource::from).toList();
    }
}
