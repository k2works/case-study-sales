package com.example.sms.infrastructure.datasource.master.partner.supplier;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 仕入先カスタムマッパー
 */
@Mapper
public interface SupplierCustomMapper {
    
    SupplierCustomEntity selectByPrimaryKey(Map<String, Object> keys);
    List<SupplierCustomEntity> selectAll();
    List<SupplierCustomEntity> selectByCriteria(Map<String, Object> criteria);
}