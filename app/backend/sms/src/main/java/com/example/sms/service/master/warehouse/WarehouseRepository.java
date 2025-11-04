package com.example.sms.service.master.warehouse;

import com.example.sms.domain.model.master.warehouse.Warehouse;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.model.master.warehouse.WarehouseList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

public interface WarehouseRepository {
    Optional<Warehouse> findById(WarehouseCode warehouseCode);
    WarehouseList findByCode(String warehouseCode);
    WarehouseList selectAll();
    PageInfo<Warehouse> selectAllWithPageInfo();
    void save(Warehouse warehouse);
    void deleteById(WarehouseCode warehouseCode);
    void deleteAll();
    PageInfo<Warehouse> searchWithPageInfo(WarehouseCriteria criteria);
}