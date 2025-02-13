package com.example.sms.domain.model.master.region;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 地域
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Region {
    RegionCode regionCode; //地域コード
    String regionName;  //地域名

    public static Region of(String regionCode, String regionName) {
        return new Region(RegionCode.of(regionCode), regionName);
    }
}
