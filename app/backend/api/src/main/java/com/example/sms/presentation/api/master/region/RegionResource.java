package com.example.sms.presentation.api.master.region;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "地域")
public class RegionResource {
    String regionCode; //地域コード
    String regionName;  //地域名


}
