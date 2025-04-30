package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.sales.shipping.ShippingList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.sales.order.OrderCustomEntity;
import com.example.sms.infrastructure.datasource.sales.order.OrderCustomMapper;
import com.example.sms.infrastructure.datasource.sales.shipping.ShippingEntityMapper;
import com.example.sms.service.system.download.ShippingCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShippingCSVDataSource implements ShippingCSVRepository {
    private final OrderCustomMapper orderCustomMapper;
    private final ShippingEntityMapper shippingEntityMapper;

    // コンストラクタ
    public ShippingCSVDataSource(OrderCustomMapper orderCustomMapper, ShippingEntityMapper shippingEntityMapper) {
        this.orderCustomMapper = orderCustomMapper;
        this.shippingEntityMapper = shippingEntityMapper;
    }

    @Override
    public List<ShippingDownloadCSV> convert(ShippingList shippingList) {
        if (shippingList != null) {
            return shippingList.asList().stream()
                    .map(shipping -> shippingEntityMapper.mapToCsvModel(shipping))
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<OrderCustomEntity> salesOrderEntities = orderCustomMapper.selectAll();
        return salesOrderEntities.size();
    }

    @Override
    public ShippingList selectBy(DownloadCriteria condition) {
        List<OrderCustomEntity> salesOrderEntities = orderCustomMapper.selectAll();
        return shippingEntityMapper.mapToShippingList(
                salesOrderEntities.stream()
                        .flatMap(orderCustomEntity -> 
                                orderCustomEntity.get受注データ明細().stream()
                                        .map(orderLineCustomEntity -> 
                                                shippingEntityMapper.mapToDomainModel(orderCustomEntity, orderLineCustomEntity)))
                        .toList());
    }
}