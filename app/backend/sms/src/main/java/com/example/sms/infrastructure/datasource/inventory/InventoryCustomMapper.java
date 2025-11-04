package com.example.sms.infrastructure.datasource.inventory;

import com.example.sms.infrastructure.datasource.autogen.model.在庫データ;
import com.example.sms.infrastructure.datasource.autogen.model.在庫データKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface InventoryCustomMapper {
    InventoryCustomEntity selectByPrimaryKey(在庫データKey key);
    List<InventoryCustomEntity> selectAll();
    List<InventoryCustomEntity> selectByCriteria(Map<String, Object> criteria);
    int insertForOptimisticLock(在庫データ record);
    int updateByPrimaryKeyForOptimisticLock(在庫データ record);
    void deleteAll();
}