package com.example.sms.infrastructure.datasource.shipping;

import com.example.sms.domain.model.sales_order.CompletionFlag;
import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.model.shipping.ShippingList;
import com.example.sms.infrastructure.datasource.ObjectOptimisticLockingFailureException;
import com.example.sms.infrastructure.datasource.autogen.mapper.受注データMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.受注データ明細Mapper;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ明細;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ明細Key;
import com.example.sms.infrastructure.datasource.sales_order.SalesOrderCustomEntity;
import com.example.sms.infrastructure.datasource.sales_order.SalesOrderCustomMapper;
import com.example.sms.infrastructure.datasource.sales_order.sales_order_line.SalesOrderLineCustomEntity;
import com.example.sms.infrastructure.datasource.sales_order.sales_order_line.SalesOrderLineCustomMapper;
import com.example.sms.service.sales_order.SalesOrderCriteria;
import com.example.sms.service.shipping.ShippingCriteria;
import com.example.sms.service.shipping.ShippingRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
    public void save(Shipping shipping) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        Optional<SalesOrderCustomEntity> salesOrderEntity = Optional.ofNullable(salesOrderCustomMapper.selectByPrimaryKey(shipping.getOrderNumber().getValue()));
        if (salesOrderEntity.isEmpty()) {
            createShipping(shipping, username);
        } else {
            updateShipping(shipping, salesOrderEntity, username);
        }
    }

    private void updateShipping(Shipping shipping, Optional<SalesOrderCustomEntity> salesOrderEntity, String username) {
        受注データ salesOrderData = shippingEntityMapper.mapToEntity(shipping);
        if (salesOrderEntity.isPresent()) {
            salesOrderData.set作成日時(salesOrderEntity.get().get作成日時());
            salesOrderData.set作成者名(salesOrderEntity.get().get作成者名());
            salesOrderData.set更新日時(LocalDateTime.now());
            salesOrderData.set更新者名(username);
            salesOrderData.setVersion(salesOrderEntity.get().getVersion());
        }
        int updateCount = salesOrderCustomMapper.updateByPrimaryKeyForOptimisticLock(salesOrderData);
        if (updateCount == 0) {
            throw new ObjectOptimisticLockingFailureException(受注データ.class, shipping.getOrderNumber());
        }

        受注データ明細Key key = new 受注データ明細Key();
        key.set受注番号(shipping.getOrderNumber().getValue());
        key.set受注行番号(shipping.getOrderLineNumber());

        salesOrderLineCustomMapper.deleteBySalesOrderNumberAndLineNumber(shipping.getOrderNumber().getValue(), String.valueOf(shipping.getOrderLineNumber()));

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
        salesOrderCustomMapper.insertForOptimisticLock(salesOrderData);

        受注データ明細Key key = new 受注データ明細Key();
        key.set受注番号(shipping.getOrderNumber().getValue());
        key.set受注行番号(shipping.getOrderLineNumber());

        salesOrderLineCustomMapper.deleteBySalesOrderNumberAndLineNumber(shipping.getOrderNumber().getValue(), String.valueOf(shipping.getOrderLineNumber()));

        受注データ明細 salesOrderLineData = shippingEntityMapper.mapToEntity(key, shipping);
        salesOrderLineData.set作成日時(LocalDateTime.now());
        salesOrderLineData.set作成者名(username);
        salesOrderLineData.set更新日時(LocalDateTime.now());
        salesOrderLineData.set更新者名(username);
        salesOrderLineMapper.insert(salesOrderLineData);
    }

    @Override
    public ShippingList selectAll() {
        List<SalesOrderCustomEntity> salesOrderCustomEntities = salesOrderCustomMapper.selectAllNotComplete(CompletionFlag.未完了.getValue());
        List<Shipping> shippings = new ArrayList<>();

        for (SalesOrderCustomEntity salesOrderCustomEntity : salesOrderCustomEntities) {
            List<SalesOrderLineCustomEntity> salesOrderLineCustomEntities = salesOrderLineCustomMapper.selectBySalesOrderNumber(salesOrderCustomEntity.get受注番号());
            for (SalesOrderLineCustomEntity salesOrderLineCustomEntity : salesOrderLineCustomEntities) {
                if (salesOrderLineCustomEntity.get完了フラグ() == CompletionFlag.未完了.getValue()) {
                    shippings.add(shippingEntityMapper.mapToDomainModel(salesOrderCustomEntity, salesOrderLineCustomEntity));
                }
            }
        }

        return shippingEntityMapper.mapToShippingList(shippings);
    }

    @Override
    public ShippingList selectAllNotComplete() {
        List<SalesOrderCustomEntity> salesOrderCustomEntities = salesOrderCustomMapper.selectAllNotComplete(CompletionFlag.未完了.getValue());
        List<Shipping> shippings = new ArrayList<>();

        for (SalesOrderCustomEntity salesOrderCustomEntity : salesOrderCustomEntities) {
            List<SalesOrderLineCustomEntity> salesOrderLineCustomEntities = salesOrderLineCustomMapper.selectBySalesOrderNumber(salesOrderCustomEntity.get受注番号());
            for (SalesOrderLineCustomEntity salesOrderLineCustomEntity : salesOrderLineCustomEntities) {
                if (salesOrderLineCustomEntity.get完了フラグ() == CompletionFlag.未完了.getValue()) {
                    shippings.add(shippingEntityMapper.mapToDomainModel(salesOrderCustomEntity, salesOrderLineCustomEntity));
                }
            }
        }

        return shippingEntityMapper.mapToShippingList(shippings);
    }

    @Override
    public Optional<Shipping> findById(String orderNumber, String orderLineNumber) {
        SalesOrderCustomEntity salesOrderCustomEntity = salesOrderCustomMapper.selectByPrimaryKey(orderNumber);
        if (salesOrderCustomEntity != null) {
            SalesOrderLineCustomEntity salesOrderLineCustomEntities = salesOrderLineCustomMapper.selectBySalesOrderNumberAndLineNumber(orderNumber, orderLineNumber);
            if (salesOrderLineCustomEntities != null) {
                return Optional.of(shippingEntityMapper.mapToDomainModel(salesOrderCustomEntity, salesOrderLineCustomEntities));
            }
        }
        return Optional.empty();
    }

    @Override
    public PageInfo<Shipping> selectAllWithPageInfo() {
        List<SalesOrderCustomEntity> salesOrderCustomEntities = salesOrderCustomMapper.selectAll();
        List<Shipping> shippings = new ArrayList<>();

        for (SalesOrderCustomEntity salesOrderCustomEntity : salesOrderCustomEntities) {
            List<SalesOrderLineCustomEntity> salesOrderLineCustomEntities = salesOrderLineCustomMapper.selectBySalesOrderNumber(salesOrderCustomEntity.get受注番号());
            for (SalesOrderLineCustomEntity salesOrderLineCustomEntity : salesOrderLineCustomEntities) {
                shippings.add(shippingEntityMapper.mapToDomainModel(salesOrderCustomEntity, salesOrderLineCustomEntity));
            }
        }

        return new PageInfo<>(shippings);
    }

    @Override
    public PageInfo<Shipping> searchWithPageInfo(ShippingCriteria criteria, SalesOrderCriteria salesOrderCriteria) {
        List<SalesOrderCustomEntity> salesOrderCustomEntities = salesOrderCustomMapper.selectByCriteria(salesOrderCriteria);

        List<Shipping> shippings = new ArrayList<>();

        for (SalesOrderCustomEntity salesOrderCustomEntity : salesOrderCustomEntities) {
            List<SalesOrderLineCustomEntity> salesOrderLineCustomEntities = salesOrderLineCustomMapper.selectBySalesOrderNumber(salesOrderCustomEntity.get受注番号());
            for (SalesOrderLineCustomEntity salesOrderLineCustomEntity : salesOrderLineCustomEntities) {
                if ((criteria.getOrderLineNumber() == null || criteria.getOrderLineNumber().equals(salesOrderLineCustomEntity.get受注行番号())) &&
                    (criteria.getProductCode() == null || criteria.getProductCode().equals(salesOrderLineCustomEntity.get商品コード())) &&
                    (criteria.getProductName() == null || salesOrderLineCustomEntity.get商品名().contains(criteria.getProductName())) &&
                    (criteria.getDeliveryDate() == null || criteria.getDeliveryDate().equals(salesOrderLineCustomEntity.get納期()))) {
                    shippings.add(shippingEntityMapper.mapToDomainModel(salesOrderCustomEntity, salesOrderLineCustomEntity));
                }
            }
        }

        return new PageInfo<>(shippings);
    }

    @Override
    public ShippingList search(ShippingCriteria criteria, SalesOrderCriteria salesOrderCriteria) {
        List<SalesOrderCustomEntity> salesOrderCustomEntities = salesOrderCustomMapper.selectByCriteria(salesOrderCriteria);

        List<Shipping> shippings = new ArrayList<>();

        for (SalesOrderCustomEntity salesOrderCustomEntity : salesOrderCustomEntities) {
            List<SalesOrderLineCustomEntity> salesOrderLineCustomEntities = salesOrderLineCustomMapper.selectBySalesOrderNumber(salesOrderCustomEntity.get受注番号());
            for (SalesOrderLineCustomEntity salesOrderLineCustomEntity : salesOrderLineCustomEntities) {
                if ((criteria.getOrderLineNumber() == null || criteria.getOrderLineNumber().equals(salesOrderLineCustomEntity.get受注行番号())) &&
                    (criteria.getProductCode() == null || criteria.getProductCode().equals(salesOrderLineCustomEntity.get商品コード())) &&
                    (criteria.getProductName() == null || salesOrderLineCustomEntity.get商品名().contains(criteria.getProductName())) &&
                    (criteria.getDeliveryDate() == null || criteria.getDeliveryDate().equals(salesOrderLineCustomEntity.get納期()))) {
                    shippings.add(shippingEntityMapper.mapToDomainModel(salesOrderCustomEntity, salesOrderLineCustomEntity));
                }
            }
        }

        return shippingEntityMapper.mapToShippingList(shippings);
    }

    @Override
    public void orderShipping(ShippingList shippingList) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        shippingList.asList().forEach(
            shipping -> {
                SalesOrderCustomEntity salesOrderEntity = salesOrderCustomMapper.selectByPrimaryKey(shipping.getOrderNumber().getValue());
                if (salesOrderEntity != null) {
                    salesOrderEntity.get受注データ明細().forEach(
                        salesOrderLineEntity -> {
                            if (salesOrderLineEntity.get受注行番号().equals(shipping.getOrderLineNumber())) {
                                salesOrderLineCustomMapper.deleteBySalesOrderNumberAndLineNumber(shipping.getOrderNumber().getValue(), String.valueOf(shipping.getOrderLineNumber()));

                                受注データ明細Key key = new 受注データ明細Key();
                                key.set受注番号(shipping.getOrderNumber().getValue());
                                key.set受注行番号(shipping.getOrderLineNumber());
                                受注データ明細 salesOrderLineData = shippingEntityMapper.mapToEntity(key, shipping);
                                salesOrderLineData.set完了フラグ(CompletionFlag.完了.getValue());
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
