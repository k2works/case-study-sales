package com.example.sms.infrastructure.datasource.master.warehouse;

import com.example.sms.service.master.warehouse.WarehouseCriteria;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WarehouseCustomMapper {
    WarehouseCustomEntity selectByPrimaryKey(String warehouseCode);

    @Delete("DELETE FROM 倉庫マスタ")
    void deleteAll();

    List<WarehouseCustomEntity> selectAll();

    List<WarehouseCustomEntity> selectByWarehouseCode(String warehouseCode);

    List<WarehouseCustomEntity> selectByCriteria(WarehouseCriteria criteria);
}