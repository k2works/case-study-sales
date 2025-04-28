package com.example.sms.service.shipping;

import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.model.shipping.ShippingList;
import com.example.sms.service.order.SalesOrderCriteria;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

public interface ShippingRepository {
    void save(Shipping shipping);

    ShippingList selectAll();

    ShippingList selectAllComplete();

    ShippingList selectAllNotComplete();

    Optional<Shipping> findById(String orderNumber, Integer orderLineNumber);

    PageInfo<Shipping> selectAllWithPageInfo();

    PageInfo<Shipping> selectAllWithPageInfoAllComplete();

    PageInfo<Shipping> selectAllWithPageInfoNotComplete();

    PageInfo<Shipping> searchWithPageInfo(ShippingCriteria shippingCriteria, SalesOrderCriteria salesOrderCriteria);

    void updateSalesOrderLine(ShippingList shippingList);

    ShippingList search(ShippingCriteria criteria, SalesOrderCriteria salesOrderCriteria);
}
