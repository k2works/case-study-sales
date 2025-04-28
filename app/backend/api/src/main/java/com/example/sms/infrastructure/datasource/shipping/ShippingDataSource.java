package com.example.sms.infrastructure.datasource.shipping;

import com.example.sms.domain.model.order.CompletionFlag;
import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.model.shipping.ShippingList;
import com.example.sms.infrastructure.datasource.ObjectOptimisticLockingFailureException;
import com.example.sms.infrastructure.datasource.autogen.mapper.受注データMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.受注データ明細Mapper;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ明細;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ明細Key;
import com.example.sms.infrastructure.datasource.order.OrderCustomEntity;
import com.example.sms.infrastructure.datasource.order.OrderCustomMapper;
import com.example.sms.infrastructure.datasource.order.order_line.OrderLineCustomEntity;
import com.example.sms.infrastructure.datasource.order.order_line.OrderLineCustomMapper;
import com.example.sms.service.order.SalesOrderCriteria;
import com.example.sms.service.shipping.ShippingCriteria;
import com.example.sms.service.shipping.ShippingRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ShippingDataSource implements ShippingRepository {
    final 受注データMapper salesOrderMapper;
    final OrderCustomMapper orderCustomMapper;
    final 受注データ明細Mapper salesOrderLineMapper;
    final OrderLineCustomMapper orderLineCustomMapper;
    final ShippingEntityMapper shippingEntityMapper;

    public ShippingDataSource(受注データMapper salesOrderMapper, OrderCustomMapper orderCustomMapper, 受注データ明細Mapper salesOrderLineMapper, OrderLineCustomMapper orderLineCustomMapper, ShippingEntityMapper shippingEntityMapper) {
        this.salesOrderMapper = salesOrderMapper;
        this.orderCustomMapper = orderCustomMapper;
        this.salesOrderLineMapper = salesOrderLineMapper;
        this.orderLineCustomMapper = orderLineCustomMapper;
        this.shippingEntityMapper = shippingEntityMapper;
    }

    @Override
    public void save(Shipping shipping) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        Optional<OrderCustomEntity> salesOrderEntity = Optional.ofNullable(orderCustomMapper.selectByPrimaryKey(shipping.getOrderNumber().getValue()));
        if (salesOrderEntity.isEmpty()) {
            createShipping(shipping, username);
        } else {
            updateShipping(shipping, salesOrderEntity, username);
        }
    }

    private void updateShipping(Shipping shipping, Optional<OrderCustomEntity> salesOrderEntity, String username) {
        受注データ salesOrderData = shippingEntityMapper.mapToEntity(shipping);
        if (salesOrderEntity.isPresent()) {
            salesOrderData.set作成日時(salesOrderEntity.get().get作成日時());
            salesOrderData.set作成者名(salesOrderEntity.get().get作成者名());
            salesOrderData.set更新日時(LocalDateTime.now());
            salesOrderData.set更新者名(username);
            salesOrderData.setVersion(salesOrderEntity.get().getVersion());
        }
        int updateCount = orderCustomMapper.updateByPrimaryKeyForOptimisticLock(salesOrderData);
        if (updateCount == 0) {
            throw new ObjectOptimisticLockingFailureException(受注データ.class, shipping.getOrderNumber());
        }

        受注データ明細Key key = new 受注データ明細Key();
        key.set受注番号(shipping.getOrderNumber().getValue());
        key.set受注行番号(shipping.getOrderLineNumber());

        orderLineCustomMapper.deleteBySalesOrderNumberAndLineNumber(shipping.getOrderNumber().getValue(), shipping.getOrderLineNumber());

        受注データ明細 salesOrderLineData = shippingEntityMapper.mapToEntity(key, shipping);
        salesOrderLineData.set作成日時(LocalDateTime.now());
        salesOrderLineData.set作成者名(username);
        salesOrderLineData.set更新日時(LocalDateTime.now());
        salesOrderLineData.set更新者名(username);
        salesOrderLineMapper.insert(salesOrderLineData);
    }

    private void createShipping(Shipping shipping, String username) {
        受注データ salesOrderData = shippingEntityMapper.mapToEntity(shipping);
        salesOrderData.set作成日時(LocalDateTime.now());
        salesOrderData.set作成者名(username);
        salesOrderData.set更新日時(LocalDateTime.now());
        salesOrderData.set更新者名(username);
        orderCustomMapper.insertForOptimisticLock(salesOrderData);

        受注データ明細Key key = new 受注データ明細Key();
        key.set受注番号(shipping.getOrderNumber().getValue());
        key.set受注行番号(shipping.getOrderLineNumber());

        orderLineCustomMapper.deleteBySalesOrderNumberAndLineNumber(shipping.getOrderNumber().getValue(), shipping.getOrderLineNumber());

        受注データ明細 salesOrderLineData = shippingEntityMapper.mapToEntity(key, shipping);
        salesOrderLineData.set作成日時(LocalDateTime.now());
        salesOrderLineData.set作成者名(username);
        salesOrderLineData.set更新日時(LocalDateTime.now());
        salesOrderLineData.set更新者名(username);
        salesOrderLineMapper.insert(salesOrderLineData);
    }

    @Override
    public ShippingList selectAll() {
        List<OrderCustomEntity> salesOrderCustomEntities = orderCustomMapper.selectAll();
        List<Shipping> shippings = new ArrayList<>();

        for (OrderCustomEntity orderCustomEntity : salesOrderCustomEntities) {
            List<OrderLineCustomEntity> salesOrderLineCustomEntities = orderLineCustomMapper.selectBySalesOrderNumber(orderCustomEntity.get受注番号());
            for (OrderLineCustomEntity orderLineCustomEntity : salesOrderLineCustomEntities) {
                shippings.add(shippingEntityMapper.mapToDomainModel(orderCustomEntity, orderLineCustomEntity));
            }
        }

        return shippingEntityMapper.mapToShippingList(shippings);
    }


    @Override
    public ShippingList selectAllComplete() {
        List<OrderCustomEntity> salesOrderCustomEntities = orderCustomMapper.selectAllWithCompletionFlag(CompletionFlag.完了.getValue());
        List<Shipping> shippings = new ArrayList<>();

        for (OrderCustomEntity orderCustomEntity : salesOrderCustomEntities) {
            List<OrderLineCustomEntity> salesOrderLineCustomEntities = orderLineCustomMapper.selectBySalesOrderNumber(orderCustomEntity.get受注番号());
            for (OrderLineCustomEntity orderLineCustomEntity : salesOrderLineCustomEntities) {
                if (orderLineCustomEntity.get完了フラグ() == CompletionFlag.完了.getValue()) {
                    shippings.add(shippingEntityMapper.mapToDomainModel(orderCustomEntity, orderLineCustomEntity));
                }
            }
        }

        return shippingEntityMapper.mapToShippingList(shippings);
    }

    @Override
    public ShippingList selectAllNotComplete() {
        List<OrderCustomEntity> salesOrderCustomEntities = orderCustomMapper.selectAllWithCompletionFlag(CompletionFlag.未完了.getValue());
        List<Shipping> shippings = new ArrayList<>();

        for (OrderCustomEntity orderCustomEntity : salesOrderCustomEntities) {
            List<OrderLineCustomEntity> salesOrderLineCustomEntities = orderLineCustomMapper.selectBySalesOrderNumber(orderCustomEntity.get受注番号());
            for (OrderLineCustomEntity orderLineCustomEntity : salesOrderLineCustomEntities) {
                shippings.add(shippingEntityMapper.mapToDomainModel(orderCustomEntity, orderLineCustomEntity));
            }
        }

        return shippingEntityMapper.mapToShippingList(shippings);
    }

    @Override
    public Optional<Shipping> findById(String orderNumber, Integer orderLineNumber) {
        OrderCustomEntity orderCustomEntity = orderCustomMapper.selectByPrimaryKey(orderNumber);
        if (orderCustomEntity != null) {
            OrderLineCustomEntity salesOrderLineCustomEntities = orderLineCustomMapper.selectBySalesOrderNumberAndLineNumber(orderNumber, orderLineNumber);
            if (salesOrderLineCustomEntities != null) {
                return Optional.of(shippingEntityMapper.mapToDomainModel(orderCustomEntity, salesOrderLineCustomEntities));
            }
        }
        return Optional.empty();
    }

    @Override
    public PageInfo<Shipping> selectAllWithPageInfo() {
        List<OrderCustomEntity> salesOrderCustomEntities = orderCustomMapper.selectAll();
        List<Shipping> shippings = new ArrayList<>();

        for (OrderCustomEntity orderCustomEntity : salesOrderCustomEntities) {
            List<OrderLineCustomEntity> salesOrderLineCustomEntities = orderLineCustomMapper.selectBySalesOrderNumber(orderCustomEntity.get受注番号());
            for (OrderLineCustomEntity orderLineCustomEntity : salesOrderLineCustomEntities) {
                shippings.add(shippingEntityMapper.mapToDomainModel(orderCustomEntity, orderLineCustomEntity));
            }
        }

        return new PageInfo<>(shippings);
    }

    @Override
    public PageInfo<Shipping> selectAllWithPageInfoAllComplete() {
        List<OrderCustomEntity> salesOrderCustomEntities = orderCustomMapper.selectAllWithCompletionFlag(CompletionFlag.完了.getValue());
        List<Shipping> shippings = new ArrayList<>();

        for (OrderCustomEntity orderCustomEntity : salesOrderCustomEntities) {
            List<OrderLineCustomEntity> salesOrderLineCustomEntities = orderLineCustomMapper.selectBySalesOrderNumber(orderCustomEntity.get受注番号());
            for (OrderLineCustomEntity orderLineCustomEntity : salesOrderLineCustomEntities) {
                if (orderLineCustomEntity.get完了フラグ() == CompletionFlag.完了.getValue()) {
                    shippings.add(shippingEntityMapper.mapToDomainModel(orderCustomEntity, orderLineCustomEntity));
                }
            }
        }

        return new PageInfo<>(shippings);
    }

    @Override
    public PageInfo<Shipping> selectAllWithPageInfoNotComplete() {
        List<OrderCustomEntity> salesOrderCustomEntities = orderCustomMapper.selectAllWithCompletionFlag(CompletionFlag.未完了.getValue());
        List<Shipping> shippings = new ArrayList<>();

        for (OrderCustomEntity orderCustomEntity : salesOrderCustomEntities) {
            List<OrderLineCustomEntity> salesOrderLineCustomEntities = orderLineCustomMapper.selectBySalesOrderNumber(orderCustomEntity.get受注番号());
            for (OrderLineCustomEntity orderLineCustomEntity : salesOrderLineCustomEntities) {
                shippings.add(shippingEntityMapper.mapToDomainModel(orderCustomEntity, orderLineCustomEntity));
            }
        }

        return new PageInfo<>(shippings);
    }

    @Override
    public PageInfo<Shipping> searchWithPageInfo(ShippingCriteria criteria, SalesOrderCriteria salesOrderCriteria) {
        List<OrderCustomEntity> salesOrderCustomEntities = orderCustomMapper.selectByCriteria(salesOrderCriteria);

        List<Shipping> shippings = new ArrayList<>();

        for (OrderCustomEntity orderCustomEntity : salesOrderCustomEntities) {
            List<OrderLineCustomEntity> salesOrderLineCustomEntities = orderLineCustomMapper.selectBySalesOrderNumber(orderCustomEntity.get受注番号());
            for (OrderLineCustomEntity orderLineCustomEntity : salesOrderLineCustomEntities) {
                if ((criteria.getOrderLineNumber() == null || criteria.getOrderLineNumber().equals(orderLineCustomEntity.get受注行番号())) &&
                    (criteria.getProductCode() == null || criteria.getProductCode().equals(orderLineCustomEntity.get商品コード())) &&
                    (criteria.getProductName() == null || orderLineCustomEntity.get商品名().contains(criteria.getProductName())) &&
                    (criteria.getDeliveryDate() == null || criteria.getDeliveryDate().equals(orderLineCustomEntity.get納期()))) {
                    shippings.add(shippingEntityMapper.mapToDomainModel(orderCustomEntity, orderLineCustomEntity));
                }
            }
        }
        if (criteria.getCompletionFlag() != null) {
            CompletionFlag checkFlag = CompletionFlag.of(criteria.getCompletionFlag() ? 1 : 0);
            shippings.removeIf(shipping -> !shipping.getCompletionFlag().equals(checkFlag));
        }

        return new PageInfo<>(shippings);
    }

    @Override
    public ShippingList search(ShippingCriteria criteria, SalesOrderCriteria salesOrderCriteria) {
        List<OrderCustomEntity> salesOrderCustomEntities = orderCustomMapper.selectByCriteria(salesOrderCriteria);

        List<Shipping> shippings = new ArrayList<>();

        for (OrderCustomEntity orderCustomEntity : salesOrderCustomEntities) {
            List<OrderLineCustomEntity> salesOrderLineCustomEntities = orderLineCustomMapper.selectBySalesOrderNumber(orderCustomEntity.get受注番号());
            for (OrderLineCustomEntity orderLineCustomEntity : salesOrderLineCustomEntities) {
                if ((criteria.getOrderLineNumber() == null || criteria.getOrderLineNumber().equals(orderLineCustomEntity.get受注行番号())) &&
                    (criteria.getProductCode() == null || criteria.getProductCode().equals(orderLineCustomEntity.get商品コード())) &&
                    (criteria.getProductName() == null || orderLineCustomEntity.get商品名().contains(criteria.getProductName())) &&
                    (criteria.getDeliveryDate() == null || criteria.getDeliveryDate().equals(orderLineCustomEntity.get納期()))) {
                    shippings.add(shippingEntityMapper.mapToDomainModel(orderCustomEntity, orderLineCustomEntity));
                }
            }
        }
        if (criteria.getCompletionFlag() != null) {
            CompletionFlag checkFlag = CompletionFlag.of(criteria.getCompletionFlag() ? 1 : 0);
            shippings.removeIf(shipping -> !shipping.getCompletionFlag().equals(checkFlag));
        }

        return shippingEntityMapper.mapToShippingList(shippings);
    }

    @Override
    public void updateSalesOrderLine(ShippingList shippingList) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        shippingList.asList().forEach(
            shipping -> {
                OrderCustomEntity salesOrderEntity = orderCustomMapper.selectByPrimaryKey(shipping.getOrderNumber().getValue());
                if (salesOrderEntity != null) {
                    salesOrderEntity.get受注データ明細().forEach(
                        salesOrderLineEntity -> {
                            if (salesOrderLineEntity.get受注行番号().equals(shipping.getOrderLineNumber())) {
                                orderLineCustomMapper.deleteBySalesOrderNumberAndLineNumber(shipping.getOrderNumber().getValue(), shipping.getOrderLineNumber());

                                受注データ明細Key key = new 受注データ明細Key();
                                key.set受注番号(shipping.getOrderNumber().getValue());
                                key.set受注行番号(shipping.getOrderLineNumber());
                                受注データ明細 salesOrderLineData = shippingEntityMapper.mapToEntity(key, shipping);

                                salesOrderLineData.set出荷指示数量(Objects.requireNonNull(shipping.getShipmentInstructionQuantity()).getAmount());
                                salesOrderLineData.set出荷済数量(Objects.requireNonNull(shipping.getShippedQuantity()).getAmount());
                                salesOrderLineData.set作成日時(salesOrderLineEntity.get作成日時());
                                salesOrderLineData.set作成者名(salesOrderLineEntity.get作成者名());
                                salesOrderLineData.set更新日時(LocalDateTime.now());
                                salesOrderLineData.set更新者名(username);
                                salesOrderLineMapper.insert(salesOrderLineData);
                            }
                        }
                    );
                }
            }
        );
    }
}
