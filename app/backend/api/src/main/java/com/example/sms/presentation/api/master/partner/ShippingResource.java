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
    @Schema(description = "出荷先コード")
    ShippingCode shippingCode;
    @Schema(description = "出荷先名")
    String destinationName;
    @Schema(description = "地域コード")
    RegionCode regionCode;
    @Schema(description = "出荷先住所")
    Address shippingAddress;

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
