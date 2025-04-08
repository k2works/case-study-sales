package com.example.sms.infrastructure.datasource.sales_order;

import com.example.sms.infrastructure.datasource.autogen.model.受注データ;
import com.example.sms.service.sales_order.SalesOrderCriteria;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SalesOrderCustomMapper {
    SalesOrderCustomEntity selectByPrimaryKey(String salesOrderCode);

    List<SalesOrderCustomEntity> selectAll();

    @Delete("DELETE FROM public.受注データ")
    void deleteAll();

    void insert(SalesOrderCustomEntity entity);

    void insertForOptimisticLock(受注データ entity);

    int updateByPrimaryKeyForOptimisticLock(受注データ entity);

    List<SalesOrderCustomEntity> selectByCriteria(SalesOrderCriteria criteria);

    List<SalesOrderCustomEntity> selectAllWithCompletionFlag(Integer completionFlag);
}
