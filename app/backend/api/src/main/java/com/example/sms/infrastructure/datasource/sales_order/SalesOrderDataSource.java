package com.example.sms.infrastructure.datasource.sales_order;

import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.ObjectOptimisticLockingFailureException;
import com.example.sms.infrastructure.datasource.autogen.mapper.受注データMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.受注データ明細Mapper;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ明細;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ明細Key;
import com.example.sms.infrastructure.datasource.sales_order.sales_order_line.SalesOrderLineCustomMapper;
import com.example.sms.service.sales_order.SalesOrderCriteria;
import com.example.sms.service.sales_order.SalesOrderRepository;
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
    final SalesOrderCustomMapper salesOrderCustomMapper;
    final 受注データ明細Mapper salesOrderLineMapper;
    final SalesOrderLineCustomMapper salesOrderLineCustomMapper;
    final SalesOrderEntityMapper salesOrderEntityMapper;

    public SalesOrderDataSource(受注データMapper salesOrderMapper, SalesOrderCustomMapper salesOrderCustomMapper, 受注データ明細Mapper salesOrderLineMapper, SalesOrderLineCustomMapper salesOrderLineCustomMapper, SalesOrderEntityMapper salesOrderEntityMapper) {
        this.salesOrderMapper = salesOrderMapper;
        this.salesOrderCustomMapper = salesOrderCustomMapper;
        this.salesOrderLineMapper = salesOrderLineMapper;
        this.salesOrderLineCustomMapper = salesOrderLineCustomMapper;
        this.salesOrderEntityMapper = salesOrderEntityMapper;
    }

    @Override
    public void deleteAll() {
        salesOrderLineCustomMapper.deleteAll();
        salesOrderCustomMapper.deleteAll();
    }

    @Override
    public void save(SalesOrder salesOrder) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        Optional<SalesOrderCustomEntity> salesOrderEntity = Optional.ofNullable(salesOrderCustomMapper.selectByPrimaryKey(salesOrder.getOrderNumber()));
        if (salesOrderEntity.isEmpty()) {
            createSalesOrder(salesOrder, username);
        } else {
            updateSalesOrder(salesOrder, salesOrderEntity, username);
        }
    }

    private void updateSalesOrder(SalesOrder salesOrder, Optional<SalesOrderCustomEntity> salesOrderEntity, String username) {
        受注データ salesOrderData = salesOrderEntityMapper.mapToEntity(salesOrder);
        if (salesOrderEntity.isPresent()) {
            salesOrderData.set作成日時(salesOrderEntity.get().get作成日時());
            salesOrderData.set作成者名(salesOrderEntity.get().get作成者名());
            salesOrderData.set更新日時(LocalDateTime.now());
            salesOrderData.set更新者名(username);
            salesOrderData.setVersion(salesOrderEntity.get().getVersion());
        }
        int updateCount = salesOrderCustomMapper.updateByPrimaryKeyForOptimisticLock(salesOrderData);
        if (updateCount == 0) {
            throw new ObjectOptimisticLockingFailureException(受注データ.class, salesOrder.getOrderNumber());
        }

        if (salesOrder.getSalesOrderLines() != null) {
            salesOrderLineCustomMapper.deleteBySalesOrderNumber(salesOrder.getOrderNumber());

            AtomicInteger index = new AtomicInteger(1);
            salesOrder.getSalesOrderLines().forEach(salesOrderLine -> {
                受注データ明細Key key = new 受注データ明細Key();
                key.set受注番号(salesOrder.getOrderNumber());
                key.set受注行番号(index.getAndIncrement());

                受注データ明細 salesOrderLineData = salesOrderEntityMapper.mapToEntity(key, salesOrderLine);
                salesOrderLineData.set作成日時(LocalDateTime.now());
                salesOrderLineData.set作成者名(username);
                salesOrderLineData.set更新日時(LocalDateTime.now());
                salesOrderLineData.set更新者名(username);
                salesOrderLineMapper.insert(salesOrderLineData);
            });
        }
    }


    private void createSalesOrder(SalesOrder salesOrder, String username) {
        受注データ salesOrderData = salesOrderEntityMapper.mapToEntity(salesOrder);
        salesOrderData.set作成日時(LocalDateTime.now());
        salesOrderData.set作成者名(username);
        salesOrderData.set更新日時(LocalDateTime.now());
        salesOrderData.set更新者名(username);
        salesOrderCustomMapper.insertForOptimisticLock(salesOrderData);

        if (salesOrder.getSalesOrderLines() != null) {
            salesOrderLineCustomMapper.deleteBySalesOrderNumber(salesOrder.getOrderNumber());

            AtomicInteger index = new AtomicInteger(1);
            受注データ明細Key key = new 受注データ明細Key();
            salesOrder.getSalesOrderLines().forEach(salesOrderLine -> {
                key.set受注番号(salesOrder.getOrderNumber());
                key.set受注行番号(index.getAndIncrement());
                受注データ明細 salesOrderLineData = salesOrderEntityMapper.mapToEntity(key, salesOrderLine);
                salesOrderLineData.set作成日時(LocalDateTime.now());
                salesOrderLineData.set作成者名(username);
                salesOrderLineData.set更新日時(LocalDateTime.now());
                salesOrderLineData.set更新者名(username);
                salesOrderLineMapper.insert(salesOrderLineData);
            });
        }
    }

    @Override
    public SalesOrderList selectAll() {
        List<SalesOrderCustomEntity> salesOrderCustomEntities = salesOrderCustomMapper.selectAll();

        return new SalesOrderList(salesOrderCustomEntities.stream()
                .map(salesOrderEntityMapper::mapToDomainModel)
                .toList()
        );
    }

    @Override
    public Optional<SalesOrder> findById(String salesOrderCode) {
        SalesOrderCustomEntity salesOrderCustomEntity = salesOrderCustomMapper.selectByPrimaryKey(salesOrderCode);
        if (salesOrderCustomEntity != null) {
            return Optional.of(salesOrderEntityMapper.mapToDomainModel(salesOrderCustomEntity));
        }
        return Optional.empty();
    }

    @Override
    public void delete(SalesOrder salesOrder) {
        if (!salesOrder.getSalesOrderLines().isEmpty()) {
            salesOrder.getSalesOrderLines().forEach(salesOrderLine -> {
                受注データ明細Key key = new 受注データ明細Key();
                key.set受注番号(salesOrder.getOrderNumber());
                key.set受注行番号(salesOrderLine.getOrderLineNumber());
                salesOrderLineMapper.deleteByPrimaryKey(key);
            });
        }

        salesOrderMapper.deleteByPrimaryKey(salesOrder.getOrderNumber());
    }

    @Override
    public PageInfo<SalesOrder> selectAllWithPageInfo() {
        List<SalesOrderCustomEntity> salesOrderCustomEntities = salesOrderCustomMapper.selectAll();
        PageInfo<SalesOrderCustomEntity> pageInfo = new PageInfo<>(salesOrderCustomEntities);

        return PageInfoHelper.of(pageInfo, salesOrderEntityMapper::mapToDomainModel);
    }

    @Override
    public PageInfo<SalesOrder> searchWithPageInfo(SalesOrderCriteria criteria) {
        List<SalesOrderCustomEntity> salesOrderCustomEntities = salesOrderCustomMapper.selectByCriteria(criteria);
        PageInfo<SalesOrderCustomEntity> pageInfo = new PageInfo<>(salesOrderCustomEntities);

        return PageInfoHelper.of(pageInfo, salesOrderEntityMapper::mapToDomainModel);
    }

    @Override
    public void save(SalesOrderList salesOrderList) {
        salesOrderList.asList().forEach(this::save);
    }
}
