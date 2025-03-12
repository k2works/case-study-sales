package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.sales_order.SalesOrderCustomEntity;
import com.example.sms.infrastructure.datasource.sales_order.SalesOrderCustomMapper;
import com.example.sms.infrastructure.datasource.sales_order.SalesOrderEntityMapper;
import com.example.sms.service.system.download.SalesOrderCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SalesOrderCSVDataSource implements SalesOrderCSVRepository {
    private final SalesOrderCustomMapper salesOrderCustomMapper;
    private final SalesOrderEntityMapper salesOrderEntityMapper;

    // コンストラクタ
    public SalesOrderCSVDataSource(SalesOrderCustomMapper salesOrderCustomMapper, SalesOrderEntityMapper salesOrderEntityMapper) {
        this.salesOrderCustomMapper = salesOrderCustomMapper;
        this.salesOrderEntityMapper = salesOrderEntityMapper;
    }

    @Override
    public List<SalesOrderDownloadCSV> convert(SalesOrderList salesOrderList) {
        if (salesOrderList != null) {
            return salesOrderList.asList().stream()
                    .flatMap(salesOrder ->
                            salesOrder.getSalesOrderLines().stream()
                                    .map(salesOrderLine -> salesOrderEntityMapper.mapToCsvModel(salesOrder, salesOrderLine))
                    ).toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<SalesOrderCustomEntity> salesOrderEntities = salesOrderCustomMapper.selectAll();
        return salesOrderEntities.size();
    }

    @Override
    public SalesOrderList selectBy(DownloadCriteria condition) {
        List<SalesOrderCustomEntity> salesOrderEntities = salesOrderCustomMapper.selectAll();
        return new SalesOrderList(salesOrderEntities.stream()
                .map(salesOrderEntityMapper::mapToDomainModel)
                .toList());
    }
}