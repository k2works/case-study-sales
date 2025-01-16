package com.example.sms.service.master.region;

import lombok.Builder;
import lombok.Value;

/**
 * 地域検索条件
 */
@Value
@Builder
public class RegionCriteria {
    String regionCode; //地域コード
    String regionName;  //地域名
}
