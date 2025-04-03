package com.example.sms.infrastructure.datasource.shipping;

import com.example.sms.domain.model.sales_order.CompletionFlag;
import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.受注データMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.受注データ明細Mapper;
import com.example.sms.infrastructure.datasource.sales_order.SalesOrderCustomEntity;
import com.example.sms.infrastructure.datasource.sales_order.SalesOrderCustomMapper;
import com.example.sms.infrastructure.datasource.sales_order.sales_order_line.SalesOrderLineCustomMapper;
import com.example.sms.service.sales_order.SalesOrderCriteria;
import com.example.sms.service.shipping.ShippingRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
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
        salesOrderLineCustomMapper.deleteAll();
        salesOrderCustomMapper.deleteAll();
    }

    @Override
    public void save(SalesOrder salesOrder) {
        // Implementation omitted for brevity
    }

    @Override
    public SalesOrderList selectAll() {
        List<SalesOrderCustomEntity> salesOrderCustomEntities = salesOrderCustomMapper.selectAll();
        List<Shipping> shippings = shippingEntityMapper.mapToDomainModelList(salesOrderCustomEntities);
        
        return new SalesOrderList(new ArrayList<>());
    }

    @Override
    public Optional<SalesOrder> findById(String salesOrderCode) {
        return Optional.empty();
    }

    @Override
    public void delete(SalesOrder salesOrder) {
        // Implementation omitted for brevity
    }

    @Override
    public PageInfo<SalesOrder> selectAllWithPageInfo() {
        List<SalesOrderCustomEntity> salesOrderCustomEntities = salesOrderCustomMapper.selectAll();
        PageInfo<SalesOrderCustomEntity> pageInfo = new PageInfo<>(salesOrderCustomEntities);
        
        return PageInfoHelper.of(pageInfo, entity -> null);
    }

    @Override
    public PageInfo<SalesOrder> searchWithPageInfo(SalesOrderCriteria criteria) {
        List<SalesOrderCustomEntity> salesOrderCustomEntities = salesOrderCustomMapper.selectByCriteria(criteria);
        PageInfo<SalesOrderCustomEntity> pageInfo = new PageInfo<>(salesOrderCustomEntities);
        
        return PageInfoHelper.of(pageInfo, entity -> null);
    }

    @Override
    public void save(SalesOrderList salesOrderList) {
        // Implementation omitted for brevity
    }

    @Override
    public SalesOrderList selectAllNotComplete() {
        List<SalesOrderCustomEntity> salesOrderCustomEntities = salesOrderCustomMapper.selectAllNotComplete(CompletionFlag.未完了.getValue());
        List<Shipping> shippings = shippingEntityMapper.mapToDomainModelList(salesOrderCustomEntities);
        
        return new SalesOrderList(new ArrayList<>());
    }
}