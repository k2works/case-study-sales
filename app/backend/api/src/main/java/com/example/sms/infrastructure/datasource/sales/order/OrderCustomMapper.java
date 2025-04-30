package com.example.sms.infrastructure.datasource.sales.order;

import com.example.sms.infrastructure.datasource.autogen.model.受注データ;
import com.example.sms.service.sales.order.SalesOrderCriteria;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderCustomMapper {
    OrderCustomEntity selectByPrimaryKey(String salesOrderCode);

    List<OrderCustomEntity> selectAll();

    @Delete("DELETE FROM public.受注データ")
    void deleteAll();

    void insert(OrderCustomEntity entity);

    void insertForOptimisticLock(受注データ entity);

    int updateByPrimaryKeyForOptimisticLock(受注データ entity);

    List<OrderCustomEntity> selectByCriteria(SalesOrderCriteria criteria);

    List<OrderCustomEntity> selectAllWithCompletionFlag(Integer completionFlag);
}
