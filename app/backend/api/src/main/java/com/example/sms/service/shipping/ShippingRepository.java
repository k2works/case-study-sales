package com.example.sms.service.shipping;

import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.model.shipping.ShippingList;
import com.example.sms.service.sales_order.SalesOrderCriteria;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

public interface ShippingRepository {
    void save(Shipping shipping);

    ShippingList selectAll();

    Optional<Shipping> findById(String orderNumber);

    PageInfo<Shipping> selectAllWithPageInfo();

    PageInfo<Shipping> searchWithPageInfo(ShippingCriteria shippingCriteria, SalesOrderCriteria salesOrderCriteria);
}
