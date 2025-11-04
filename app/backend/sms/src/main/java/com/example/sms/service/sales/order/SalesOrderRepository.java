package com.example.sms.service.sales.order;

import com.example.sms.domain.model.sales.order.Order;
import com.example.sms.domain.model.sales.order.OrderList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

public interface SalesOrderRepository {
    void deleteAll();

    void save(Order order);

    OrderList selectAll();

    Optional<Order> findById(String salesOrderCode);

    void delete(Order orderCode);

    PageInfo<Order> selectAllWithPageInfo();

    PageInfo<Order> searchWithPageInfo(SalesOrderCriteria criteria);

    void save(OrderList orderList);

    OrderList selectAllNotComplete();
}
