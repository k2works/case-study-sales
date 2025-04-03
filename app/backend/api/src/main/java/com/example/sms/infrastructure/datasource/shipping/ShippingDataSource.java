package com.example.sms.infrastructure.datasource.shipping;

import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.model.shipping.ShippingList;
import com.example.sms.infrastructure.datasource.autogen.mapper.受注データMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.受注データ明細Mapper;
import com.example.sms.infrastructure.datasource.sales_order.SalesOrderCustomMapper;
import com.example.sms.infrastructure.datasource.sales_order.sales_order_line.SalesOrderLineCustomMapper;
import com.example.sms.service.shipping.ShippingCriteria;
import com.example.sms.service.shipping.ShippingRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ShippingDataSource implements ShippingRepository {
    final 受注データMapper salesOrderMapper;
    final SalesOrderCustomMapper salesOrderCustomMapper;
    final 受注データ明細Mapper salesOrderLineMapper;
    final SalesOrderLineCustomMapper salesOrderLineCustomMapper;
    final ShippingEntityMapper shippingEntityMapper;

    public ShippingDataSource(受注データMapper salesOrderMapper, SalesOrderCustomMapper salesOrderCustomMapper, 受注データ明細Mapper salesOrderLineMapper, SalesOrderLineCustomMapper salesOrderLineCustomMapper, ShippingEntityMapper shippingEntityMapper) {
        this.salesOrderMapper = salesOrderMapper;
        this.salesOrderCustomMapper = salesOrderCustomMapper;
        this.salesOrderLineMapper = salesOrderLineMapper;
        this.salesOrderLineCustomMapper = salesOrderLineCustomMapper;
        this.shippingEntityMapper = shippingEntityMapper;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void save(Shipping salesOrder) {

    }

    @Override
    public SalesOrderList selectAll() {
        return null;
    }

    @Override
    public Optional<Shipping> findById(String orderCode) {
        return Optional.empty();
    }

    @Override
    public void delete(Shipping orderCode) {

    }

    @Override
    public PageInfo<Shipping> selectAllWithPageInfo() {
        return null;
    }

    @Override
    public PageInfo<Shipping> searchWithPageInfo(ShippingCriteria criteria) {
        return null;
    }

    @Override
    public void save(ShippingList orderList) {

    }

    @Override
    public ShippingList selectAllNotComplete() {
        return null;
    }
}