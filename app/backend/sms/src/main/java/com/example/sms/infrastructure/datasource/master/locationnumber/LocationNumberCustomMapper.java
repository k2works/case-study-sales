package com.example.sms.infrastructure.datasource.master.locationnumber;

import com.example.sms.infrastructure.datasource.autogen.model.棚番マスタKey;
import com.example.sms.service.master.locationnumber.LocationNumberCriteria;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LocationNumberCustomMapper {
    LocationNumberCustomEntity selectByPrimaryKey(棚番マスタKey key);

    @Delete("DELETE FROM 棚番マスタ")
    void deleteAll();

    List<LocationNumberCustomEntity> selectAll();

    List<LocationNumberCustomEntity> selectByWarehouseCode(String warehouseCode);

    List<LocationNumberCustomEntity> selectByLocationNumberCode(String locationNumberCode);

    List<LocationNumberCustomEntity> selectByCriteria(LocationNumberCriteria criteria);
}