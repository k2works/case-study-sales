package com.example.sms.infrastructure.datasource.sales.purchase.order.order_line;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PurchaseOrderLineCustomMapper {
    List<PurchaseOrderLineCustomEntity> selectByPurchaseOrderNumber(String 発注番号);
    int insert(com.example.sms.infrastructure.datasource.autogen.model.発注データ明細 record);
    int updateByPrimaryKey(com.example.sms.infrastructure.datasource.autogen.model.発注データ明細 record);
    int deleteByPrimaryKey(@Param("発注番号") String 発注番号, @Param("発注行番号") Integer 発注行番号);
    void deleteByPurchaseOrderNumber(String 発注番号);
    void deleteAll();
}