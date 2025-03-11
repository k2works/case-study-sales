package com.example.sms.presentation.api.master.region;

import com.example.sms.domain.model.master.region.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Schema(description = "地域")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegionResource {
    String regionCode; //地域コード
    String regionName;  //地域名


    public static RegionResource from(Region region) {
        return RegionResource.builder()
                .regionCode(region.getRegionCode().getValue())
                .regionName(region.getRegionName())
                .build();
    }
}
