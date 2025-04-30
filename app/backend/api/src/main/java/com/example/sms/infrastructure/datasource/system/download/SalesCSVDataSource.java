package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.sales.sales.SalesList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.sales.sales.SalesCustomEntity;
import com.example.sms.infrastructure.datasource.sales.sales.SalesCustomMapper;
import com.example.sms.infrastructure.datasource.sales.sales.SalesEntityMapper;
import com.example.sms.service.system.download.SalesCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SalesCSVDataSource implements SalesCSVRepository {
    private final SalesCustomMapper salesCustomMapper;
    private final SalesEntityMapper salesEntityMapper;

    // コンストラクタ
    public SalesCSVDataSource(SalesCustomMapper salesCustomMapper, SalesEntityMapper salesEntityMapper) {
        this.salesCustomMapper = salesCustomMapper;
        this.salesEntityMapper = salesEntityMapper;
    }

    @Override
    public List<SalesDownloadCSV> convert(SalesList salesList) {
        if (salesList != null) {
            return salesList.asList().stream()
                    .flatMap(sales ->
                            sales.getSalesLines().stream()
                                    .map(salesLine -> salesEntityMapper.mapToCsvModel(sales, salesLine))
                    ).toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<SalesCustomEntity> salesEntities = salesCustomMapper.selectAll();
        return salesEntities.size();
    }

    @Override
    public SalesList selectBy(DownloadCriteria condition) {
        List<SalesCustomEntity> salesEntities = salesCustomMapper.selectAll();
        return new SalesList(salesEntities.stream()
                .map(salesEntityMapper::mapToDomainModel)
                .toList());
    }
}