package com.example.sms.infrastructure.datasource.sales_order.sales_order_line;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SalesOrderLineCustomMapper {
    List<SalesOrderLineCustomEntity> selectAll();

    @Delete("DELETE FROM public.受注データ明細")
    void deleteAll();

    List<SalesOrderLineCustomEntity> selectBySalesOrderNumber(String salesOrderNumber);

    void deleteBySalesOrderNumber(String customerOrderNumber);
}
