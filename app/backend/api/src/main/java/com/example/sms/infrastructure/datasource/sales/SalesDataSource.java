package com.example.sms.infrastructure.datasource.sales;


import com.example.sms.domain.model.sales.Sales;
import com.example.sms.domain.model.sales.SalesList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.ObjectOptimisticLockingFailureException;
import com.example.sms.infrastructure.datasource.autogen.mapper.売上データMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.売上データ明細Mapper;
import com.example.sms.infrastructure.datasource.autogen.model.売上データ;
import com.example.sms.infrastructure.datasource.autogen.model.売上データ明細;
import com.example.sms.infrastructure.datasource.autogen.model.売上データ明細Key;
import com.example.sms.infrastructure.datasource.sales.sales_line.SalesLineCustomMapper;
import com.example.sms.service.sales.SalesCriteria;
import com.example.sms.service.sales.SalesRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class SalesDataSource implements SalesRepository {
    private final 売上データMapper salesMapper;
    private final 売上データ明細Mapper salesLineMapper;
    private final SalesCustomMapper salesCustomMapper;
    private final SalesLineCustomMapper salesLineCustomMapper;
    private final SalesEntityMapper salesEntityMapper;

    public SalesDataSource(
            売上データMapper salesMapper, 売上データ明細Mapper salesLineMapper,
            SalesCustomMapper salesCustomMapper,
            SalesLineCustomMapper salesLineCustomMapper,
            SalesEntityMapper salesEntityMapper) {
        this.salesMapper = salesMapper;
        this.salesLineMapper = salesLineMapper;
        this.salesCustomMapper = salesCustomMapper;
        this.salesLineCustomMapper = salesLineCustomMapper;
        this.salesEntityMapper = salesEntityMapper;
    }

    @Override
    public void deleteAll() {
        salesLineCustomMapper.deleteAll();
        salesCustomMapper.deleteAll();
    }

    @Override
    public void save(Sales sales) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";
        Optional<SalesCustomEntity> salesEntity = Optional.ofNullable(salesCustomMapper.selectByPrimaryKey(sales.getSalesNumber()));

        if (salesEntity.isEmpty()) {
            createSales(sales, username);
        } else {
            updateSales(sales, salesEntity, username);
        }
    }

    private void updateSales(Sales sales, Optional<SalesCustomEntity> salesEntity, String username) {
        売上データ salesData = salesEntityMapper.mapToEntity(sales);
        if (salesEntity.isPresent()) {
            salesData.set作成日時(salesEntity.get().get作成日時());
            salesData.set作成者名(salesEntity.get().get作成者名());
            salesData.set更新日時(LocalDateTime.now());
            salesData.set更新者名(username);
            salesData.setVersion(salesEntity.get().getVersion());
        }

        int updateCount = salesCustomMapper.updateByPrimaryKeyForOptimisticLock(salesData);
        if (updateCount == 0) {
            throw new ObjectOptimisticLockingFailureException(SalesCustomEntity.class, sales.getSalesNumber());
        }

        if (sales.getSalesLines() != null) {
            salesLineCustomMapper.deleteBySalesNumber(sales.getSalesNumber());
            AtomicInteger index = new AtomicInteger(1);
            sales.getSalesLines().forEach(salesLine -> {
                売上データ明細Key key = new 売上データ明細Key();
                key.set売上番号(sales.getSalesNumber());
                key.set売上行番号(index.getAndIncrement());
                売上データ明細 salesLineData = salesEntityMapper.mapToEntity(key, salesLine);
                salesLineData.set作成日時(LocalDateTime.now());
                salesLineData.set作成者名(username);
                salesLineData.set更新日時(LocalDateTime.now());
                salesLineData.set更新者名(username);
                salesLineMapper.insert(salesLineData);
            });
        }
    }

    private void createSales(Sales sales, String username) {
        売上データ salesData = salesEntityMapper.mapToEntity(sales);
        salesData.set作成日時(LocalDateTime.now());
        salesData.set作成者名(username);
        salesData.set更新日時(LocalDateTime.now());
        salesData.set更新者名(username);
        salesCustomMapper.insertForOptimisticLock(salesData);

        if (sales.getSalesLines() != null) {
            salesLineCustomMapper.deleteBySalesNumber(sales.getSalesNumber());
            AtomicInteger index = new AtomicInteger(1);
            売上データ明細Key key = new 売上データ明細Key();
            sales.getSalesLines().forEach(salesLine -> {
                key.set売上番号(sales.getSalesNumber());
                key.set売上行番号(index.getAndIncrement());
                売上データ明細 salesLineData = salesEntityMapper.mapToEntity(key, salesLine);
                salesLineData.set作成日時(LocalDateTime.now());
                salesLineData.set作成者名(username);
                salesLineData.set更新日時(LocalDateTime.now());
                salesLineData.set更新者名(username);
                salesLineMapper.insert(salesLineData);
            });
        }
    }

    @Override
    public SalesList selectAll() {
        List<SalesCustomEntity> salesCustomEntities = salesCustomMapper.selectAll();

        return new SalesList(salesCustomEntities.stream()
                .map(salesEntityMapper::mapToDomainModel)
                .toList()
        );
    }

    @Override
    public Optional<Sales> findById(String salesCode) {
        SalesCustomEntity salesCustomEntity = salesCustomMapper.selectByPrimaryKey(salesCode);
        return salesCustomEntity != null ? Optional.of(salesEntityMapper.mapToDomainModel(salesCustomEntity)) : Optional.empty();
    }

    @Override
    public void delete(Sales sales) {
        if (!sales.getSalesLines().isEmpty()) {
            sales.getSalesLines().forEach(salesLine -> {
                売上データ明細Key key = new 売上データ明細Key();
                key.set売上番号(sales.getSalesNumber());
                key.set売上行番号(salesLine.getSalesLineNumber());
                salesLineMapper.deleteByPrimaryKey(key);
            });
        }
        salesCustomMapper.deleteAll(); // Assume single deletion not supported.
    }

    @Override
    public PageInfo<Sales> selectAllWithPageInfo() {
        List<SalesCustomEntity> salesCustomEntities = salesCustomMapper.selectAll();
        PageInfo<SalesCustomEntity> pageInfo = new PageInfo<>(salesCustomEntities);
        return PageInfoHelper.of(pageInfo, salesEntityMapper::mapToDomainModel);
    }

    @Override
    public PageInfo<Sales> searchWithPageInfo(SalesCriteria criteria) {
        List<SalesCustomEntity> salesCustomEntities = salesCustomMapper.selectByCriteria(criteria);
        PageInfo<SalesCustomEntity> pageInfo = new PageInfo<>(salesCustomEntities);
        return PageInfoHelper.of(pageInfo, salesEntityMapper::mapToDomainModel);
    }

    @Override
    public void save(SalesList salesList) {
        salesList.asList().forEach(this::save);
    }

    @Override
    public SalesList selectAllNotComplete() {
        List<SalesCustomEntity> salesCustomEntities = salesCustomMapper.selectAllNotComplete(0);  // Assuming 0 => 未完了
        return new SalesList(salesCustomEntities.stream()
                .map(salesEntityMapper::mapToDomainModel)
                .toList());
    }
}