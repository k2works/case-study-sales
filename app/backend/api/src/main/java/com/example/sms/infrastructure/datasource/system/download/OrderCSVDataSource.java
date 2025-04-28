package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.order.OrderList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.order.OrderCustomEntity;
import com.example.sms.infrastructure.datasource.order.OrderCustomMapper;
import com.example.sms.infrastructure.datasource.order.OrderEntityMapper;
import com.example.sms.service.system.download.OrderCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderCSVDataSource implements OrderCSVRepository {
    private final OrderCustomMapper orderCustomMapper;
    private final OrderEntityMapper orderEntityMapper;

    // コンストラクタ
    public OrderCSVDataSource(OrderCustomMapper orderCustomMapper, OrderEntityMapper orderEntityMapper) {
        this.orderCustomMapper = orderCustomMapper;
        this.orderEntityMapper = orderEntityMapper;
    }

    @Override
    public List<OrderDownloadCSV> convert(OrderList orderList) {
        if (orderList != null) {
            return orderList.asList().stream()
                    .flatMap(salesOrder ->
                            salesOrder.getOrderLines().stream()
                                    .map(salesOrderLine -> orderEntityMapper.mapToCsvModel(salesOrder, salesOrderLine))
                    ).toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<OrderCustomEntity> salesOrderEntities = orderCustomMapper.selectAll();
        return salesOrderEntities.size();
    }

    @Override
    public OrderList selectBy(DownloadCriteria condition) {
        List<OrderCustomEntity> salesOrderEntities = orderCustomMapper.selectAll();
        return new OrderList(salesOrderEntities.stream()
                .map(orderEntityMapper::mapToDomainModel)
                .toList());
    }
}