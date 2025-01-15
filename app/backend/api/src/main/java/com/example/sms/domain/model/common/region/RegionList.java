package com.example.sms.domain.model.common.region;

import java.util.Collections;
import java.util.List;

/**
 * 地域リスト
 */
public class RegionList {
    List<Region> value;

    public RegionList(List<Region> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public RegionList add(Region region) {
        List<Region> newValue = value;
        newValue.add(region);
        return new RegionList(newValue);
    }

    public List<Region> asList() {
        return value;
    }
}
