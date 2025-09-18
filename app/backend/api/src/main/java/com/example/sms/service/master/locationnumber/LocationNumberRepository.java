package com.example.sms.service.master.locationnumber;

import com.example.sms.domain.model.master.locationnumber.LocationNumber;
import com.example.sms.domain.model.master.locationnumber.LocationNumberList;
import com.example.sms.infrastructure.datasource.autogen.model.棚番マスタKey;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

public interface LocationNumberRepository {
    Optional<LocationNumber> findById(棚番マスタKey key);

    LocationNumberList findByWarehouseCode(String warehouseCode);

    LocationNumberList findByLocationNumberCode(String locationNumberCode);

    LocationNumberList selectAll();

    PageInfo<LocationNumber> selectAllWithPageInfo();

    void save(LocationNumber locationNumber);

    void deleteById(棚番マスタKey key);

    void deleteAll();

    PageInfo<LocationNumber> searchWithPageInfo(LocationNumberCriteria criteria);
}