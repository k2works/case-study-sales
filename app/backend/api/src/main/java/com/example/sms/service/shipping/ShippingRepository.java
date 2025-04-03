package com.example.sms.service.shipping;

import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.model.shipping.ShippingList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

public interface ShippingRepository {
    void deleteAll();

    void save(Shipping salesOrder);

    SalesOrderList selectAll();

    Optional<Shipping> findById(String orderCode);

    void delete(Shipping orderCode);

    PageInfo<Shipping> selectAllWithPageInfo();

    PageInfo<Shipping> searchWithPageInfo(ShippingCriteria criteria);

    void save(ShippingList orderList);

    ShippingList selectAllNotComplete();
}
