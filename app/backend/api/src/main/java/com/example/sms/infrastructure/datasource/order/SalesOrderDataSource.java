package com.example.sms.infrastructure.datasource.order;

import com.example.sms.domain.model.order.CompletionFlag;
import com.example.sms.domain.model.order.Order;
import com.example.sms.domain.model.order.OrderList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.ObjectOptimisticLockingFailureException;
import com.example.sms.infrastructure.datasource.autogen.mapper.受注データMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.受注データ明細Mapper;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ明細;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ明細Key;
import com.example.sms.infrastructure.datasource.order.order_line.OrderLineCustomMapper;
import com.example.sms.service.order.SalesOrderCriteria;
import com.example.sms.service.order.SalesOrderRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class SalesOrderDataSource implements SalesOrderRepository {
    final 受注データMapper salesOrderMapper;
    final OrderCustomMapper orderCustomMapper;
    final 受注データ明細Mapper salesOrderLineMapper;
    final OrderLineCustomMapper orderLineCustomMapper;
    final OrderEntityMapper orderEntityMapper;

    public SalesOrderDataSource(受注データMapper salesOrderMapper, OrderCustomMapper orderCustomMapper, 受注データ明細Mapper salesOrderLineMapper, OrderLineCustomMapper orderLineCustomMapper, OrderEntityMapper orderEntityMapper) {
        this.salesOrderMapper = salesOrderMapper;
        this.orderCustomMapper = orderCustomMapper;
        this.salesOrderLineMapper = salesOrderLineMapper;
        this.orderLineCustomMapper = orderLineCustomMapper;
        this.orderEntityMapper = orderEntityMapper;
    }

    @Override
    public void deleteAll() {
        orderLineCustomMapper.deleteAll();
        orderCustomMapper.deleteAll();
    }

    @Override
    public void save(Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        Optional<OrderCustomEntity> salesOrderEntity = Optional.ofNullable(orderCustomMapper.selectByPrimaryKey(order.getOrderNumber().getValue()));
        if (salesOrderEntity.isEmpty()) {
            createSalesOrder(order, username);
        } else {
            updateSalesOrder(order, salesOrderEntity, username);
        }
    }

    private void updateSalesOrder(Order order, Optional<OrderCustomEntity> salesOrderEntity, String username) {
        受注データ salesOrderData = orderEntityMapper.mapToEntity(order);
        if (salesOrderEntity.isPresent()) {
            salesOrderData.set作成日時(salesOrderEntity.get().get作成日時());
            salesOrderData.set作成者名(salesOrderEntity.get().get作成者名());
            salesOrderData.set更新日時(LocalDateTime.now());
            salesOrderData.set更新者名(username);
            salesOrderData.setVersion(salesOrderEntity.get().getVersion());
        }
        int updateCount = orderCustomMapper.updateByPrimaryKeyForOptimisticLock(salesOrderData);
        if (updateCount == 0) {
            throw new ObjectOptimisticLockingFailureException(受注データ.class, order.getOrderNumber());
        }

        if (order.getOrderLines() != null) {
            orderLineCustomMapper.deleteBySalesOrderNumber(order.getOrderNumber().getValue());

            AtomicInteger index = new AtomicInteger(1);
            order.getOrderLines().forEach(salesOrderLine -> {
                受注データ明細Key key = new 受注データ明細Key();
                key.set受注番号(order.getOrderNumber().getValue());
                key.set受注行番号(index.getAndIncrement());

                受注データ明細 salesOrderLineData = orderEntityMapper.mapToEntity(key, salesOrderLine);
                salesOrderLineData.set作成日時(LocalDateTime.now());
                salesOrderLineData.set作成者名(username);
                salesOrderLineData.set更新日時(LocalDateTime.now());
                salesOrderLineData.set更新者名(username);
                salesOrderLineMapper.insert(salesOrderLineData);
            });
        }
    }


    private void createSalesOrder(Order order, String username) {
        受注データ salesOrderData = orderEntityMapper.mapToEntity(order);
        salesOrderData.set作成日時(LocalDateTime.now());
        salesOrderData.set作成者名(username);
        salesOrderData.set更新日時(LocalDateTime.now());
        salesOrderData.set更新者名(username);
        orderCustomMapper.insertForOptimisticLock(salesOrderData);

        if (order.getOrderLines() != null) {
            orderLineCustomMapper.deleteBySalesOrderNumber(order.getOrderNumber().getValue());

            AtomicInteger index = new AtomicInteger(1);
            受注データ明細Key key = new 受注データ明細Key();
            order.getOrderLines().forEach(salesOrderLine -> {
                key.set受注番号(order.getOrderNumber().getValue());
                key.set受注行番号(index.getAndIncrement());
                受注データ明細 salesOrderLineData = orderEntityMapper.mapToEntity(key, salesOrderLine);
                salesOrderLineData.set作成日時(LocalDateTime.now());
                salesOrderLineData.set作成者名(username);
                salesOrderLineData.set更新日時(LocalDateTime.now());
                salesOrderLineData.set更新者名(username);
                salesOrderLineMapper.insert(salesOrderLineData);
            });
        }
    }

    @Override
    public OrderList selectAll() {
        List<OrderCustomEntity> salesOrderCustomEntities = orderCustomMapper.selectAll();

        return new OrderList(salesOrderCustomEntities.stream()
                .map(orderEntityMapper::mapToDomainModel)
                .toList()
        );
    }

    @Override
    public Optional<Order> findById(String salesOrderCode) {
        OrderCustomEntity orderCustomEntity = orderCustomMapper.selectByPrimaryKey(salesOrderCode);
        if (orderCustomEntity != null) {
            return Optional.of(orderEntityMapper.mapToDomainModel(orderCustomEntity));
        }
        return Optional.empty();
    }

    @Override
    public void delete(Order order) {
        if (!order.getOrderLines().isEmpty()) {
            order.getOrderLines().forEach(salesOrderLine -> {
                受注データ明細Key key = new 受注データ明細Key();
                key.set受注番号(order.getOrderNumber().getValue());
                key.set受注行番号(salesOrderLine.getOrderLineNumber());
                salesOrderLineMapper.deleteByPrimaryKey(key);
            });
        }

        salesOrderMapper.deleteByPrimaryKey(order.getOrderNumber().getValue());
    }

    @Override
    public PageInfo<Order> selectAllWithPageInfo() {
        List<OrderCustomEntity> salesOrderCustomEntities = orderCustomMapper.selectAll();
        PageInfo<OrderCustomEntity> pageInfo = new PageInfo<>(salesOrderCustomEntities);

        return PageInfoHelper.of(pageInfo, orderEntityMapper::mapToDomainModel);
    }

    @Override
    public PageInfo<Order> searchWithPageInfo(SalesOrderCriteria criteria) {
        List<OrderCustomEntity> salesOrderCustomEntities = orderCustomMapper.selectByCriteria(criteria);
        PageInfo<OrderCustomEntity> pageInfo = new PageInfo<>(salesOrderCustomEntities);

        return PageInfoHelper.of(pageInfo, orderEntityMapper::mapToDomainModel);
    }

    @Override
    public void save(OrderList orderList) {
        orderList.asList().forEach(this::save);
    }

    @Override
    public OrderList selectAllNotComplete() {
        List<OrderCustomEntity> salesOrderCustomEntities = orderCustomMapper.selectAllWithCompletionFlag(CompletionFlag.未完了.getValue());

        return new OrderList(salesOrderCustomEntities.stream()
                .map(orderEntityMapper::mapToDomainModel)
                .toList()
        );
    }
}
