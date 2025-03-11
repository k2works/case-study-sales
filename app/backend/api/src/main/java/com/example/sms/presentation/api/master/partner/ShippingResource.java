package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.customer.Shipping;
import com.example.sms.domain.model.master.partner.customer.ShippingCode;
import com.example.sms.domain.model.master.region.RegionCode;
import com.example.sms.domain.type.address.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Schema(description = "出荷")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingResource {
    ShippingCode shippingCode; // 出荷先コード
    String destinationName; // 出荷先名
    RegionCode regionCode; // 地域コード
    Address shippingAddress; // 出荷先住所

    public static ShippingResource from(Shipping shipping) {
        return ShippingResource.builder()
                .shippingCode(shipping.getShippingCode())
                .destinationName(shipping.getDestinationName())
                .regionCode(shipping.getRegionCode())
                .shippingAddress(shipping.getShippingAddress())
                .build();
    }

    public static List<ShippingResource> from(List<Shipping> shippings) {
        return shippings.stream()
                .map(ShippingResource::from)
                .toList();
    }
}
