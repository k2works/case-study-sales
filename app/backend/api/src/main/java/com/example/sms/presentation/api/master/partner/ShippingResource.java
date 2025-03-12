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
    @Schema(description = "顧客コード")
    String customerCode;
    @Schema(description = "顧客枝番")
    Integer customerBranchNumber;
    @Schema(description = "出荷先番号")
    Integer destinationNumber;
    @Schema(description = "出荷先名")
    String destinationName;
    @Schema(description = "地域コード")
    String regionCode;
    @Schema(description = "郵便番号")
    String postalCode;
    @Schema(description = "都道府県")
    String prefecture;
    @Schema(description = "住所1")
    String address1;
    @Schema(description = "住所2")
    String address2;

    public static ShippingResource from(Shipping shipping) {
        return ShippingResource.builder()
                .customerCode(shipping.getShippingCode().getCustomerCode().getCode().getValue())
                .customerBranchNumber(shipping.getShippingCode().getCustomerCode().getBranchNumber())
                .destinationNumber(shipping.getShippingCode().getDestinationNumber())
                .destinationName(shipping.getDestinationName())
                .regionCode(shipping.getRegionCode().getValue())
                .postalCode(shipping.getShippingAddress().getPostalCode().getValue())
                .prefecture(shipping.getShippingAddress().getPrefecture().name())
                .address1(shipping.getShippingAddress().getAddress1())
                .address2(shipping.getShippingAddress().getAddress2())
                .build();
    }

    public static List<ShippingResource> from(List<Shipping> shippings) {
        return shippings.stream()
                .map(ShippingResource::from)
                .toList();
    }
}