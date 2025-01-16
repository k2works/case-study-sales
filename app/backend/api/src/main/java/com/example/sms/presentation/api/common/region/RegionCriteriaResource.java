package com.example.sms.presentation.api.common.region;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Schema(description = "地域検索条件")
public class RegionCriteriaResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    String regionCode; //地域コード
    String regionName;  //地域名
}
