package com.example.sms.infrastructure.datasource.sales.order.order_line;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderLineCustomMapper {
    List<OrderLineCustomEntity> selectAll();

    @Delete("DELETE FROM public.受注データ明細")
    void deleteAll();

    @Delete("DELETE FROM public.受注データ明細 WHERE 完了フラグ = 0")
    void deleteAllNotComplete();

    List<OrderLineCustomEntity> selectBySalesOrderNumber(String salesOrderNumber);

    OrderLineCustomEntity selectBySalesOrderNumberAndLineNumber(@Param("salesOrderNumber") String salesOrderNumber,
                                                                @Param("lineNumber") Integer lineNumber);

    void deleteBySalesOrderNumber(String customerOrderNumber);

    void deleteBySalesOrderNumberAndLineNumber(@Param("salesOrderNumber") String salesOrderNumber,
                                               @Param("lineNumber") Integer lineNumber);
}
