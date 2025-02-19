package com.example.sms.service.sales_order;

import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

public interface SalesOrderRepository {
    void deleteAll();

    void save(SalesOrder salesOrder);

    SalesOrderList selectAll();

    Optional<SalesOrder> findById(String salesOrderCode);

    void delete(SalesOrder salesOrderCode);

    PageInfo<SalesOrder> selectAllWithPageInfo();

    PageInfo<SalesOrder> searchWithPageInfo(SalesOrderCriteria criteria);

    void save(SalesOrderList salesOrderList);
}
