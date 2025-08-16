package com.example.sms.infrastructure.datasource.sales.purchase.order;

import com.example.sms.domain.model.sales.order.CompletionFlag;
import com.example.sms.domain.model.sales.purchase.order.PurchaseOrder;
import com.example.sms.domain.model.sales.purchase.order.PurchaseOrderList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.ObjectOptimisticLockingFailureException;
import com.example.sms.infrastructure.datasource.autogen.mapper.発注データMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.発注データ明細Mapper;
import com.example.sms.infrastructure.datasource.autogen.model.発注データ;
import com.example.sms.infrastructure.datasource.autogen.model.発注データ明細;
import com.example.sms.infrastructure.datasource.autogen.model.発注データ明細Key;
import com.example.sms.infrastructure.datasource.sales.purchase.order.order_line.PurchaseOrderLineCustomMapper;
import com.example.sms.service.sales.purchase.order.PurchaseOrderCriteria;
import com.example.sms.service.sales.purchase.order.PurchaseOrderRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class PurchaseOrderDataSource implements PurchaseOrderRepository {
    final 発注データMapper purchaseOrderMapper;
    final PurchaseOrderCustomMapper purchaseOrderCustomMapper;
    final 発注データ明細Mapper purchaseOrderLineMapper;
    final PurchaseOrderLineCustomMapper purchaseOrderLineCustomMapper;
    final PurchaseOrderEntityMapper purchaseOrderEntityMapper;

    public PurchaseOrderDataSource(発注データMapper purchaseOrderMapper, PurchaseOrderCustomMapper purchaseOrderCustomMapper, 発注データ明細Mapper purchaseOrderLineMapper, PurchaseOrderLineCustomMapper purchaseOrderLineCustomMapper, PurchaseOrderEntityMapper purchaseOrderEntityMapper) {
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.purchaseOrderCustomMapper = purchaseOrderCustomMapper;
        this.purchaseOrderLineMapper = purchaseOrderLineMapper;
        this.purchaseOrderLineCustomMapper = purchaseOrderLineCustomMapper;
        this.purchaseOrderEntityMapper = purchaseOrderEntityMapper;
    }

    @Override
    public PurchaseOrder save(PurchaseOrder purchaseOrder) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        Optional<PurchaseOrderCustomEntity> existingEntity = Optional.ofNullable(purchaseOrderCustomMapper.selectByPrimaryKey(purchaseOrder.getPurchaseOrderNumber().getValue()));
        if (existingEntity.isEmpty()) {
            insertPurchaseOrder(purchaseOrder, username);
        } else {
            updatePurchaseOrder(purchaseOrder, username);
        }
        return purchaseOrder;
    }

    private void insertPurchaseOrder(PurchaseOrder purchaseOrder, String username) {
        LocalDateTime now = LocalDateTime.now();
        
        // メインデータの挿入
        発注データ purchaseOrderEntity = purchaseOrderEntityMapper.toEntity(purchaseOrder);
        purchaseOrderEntity.set作成日時(now);
        purchaseOrderEntity.set作成者名(username);
        purchaseOrderEntity.set更新日時(now);
        purchaseOrderEntity.set更新者名(username);
        purchaseOrderEntity.setVersion(1);
        
        purchaseOrderCustomMapper.insertForOptimisticLock(purchaseOrderEntity);
        
        // 明細データの挿入
        if (purchaseOrder.getPurchaseOrderLines() != null && !purchaseOrder.getPurchaseOrderLines().isEmpty()) {
            AtomicInteger lineNumber = new AtomicInteger(1);
            purchaseOrder.getPurchaseOrderLines().forEach(line -> {
                発注データ明細 lineEntity = purchaseOrderEntityMapper.toLineEntity(line);
                lineEntity.set発注番号(purchaseOrder.getPurchaseOrderNumber().getValue());
                lineEntity.set発注行番号(lineNumber.getAndIncrement());
                lineEntity.set作成日時(now);
                lineEntity.set作成者名(username);
                lineEntity.set更新日時(now);
                lineEntity.set更新者名(username);
                
                purchaseOrderLineCustomMapper.insert(lineEntity);
            });
        }
    }

    private void updatePurchaseOrder(PurchaseOrder purchaseOrder, String username) {
        LocalDateTime now = LocalDateTime.now();
        
        // 既存エンティティの取得
        PurchaseOrderCustomEntity existingEntity = purchaseOrderCustomMapper.selectByPrimaryKey(purchaseOrder.getPurchaseOrderNumber().getValue());
        if (existingEntity == null) {
            throw new IllegalStateException("更新対象の発注データが見つかりません: " + purchaseOrder.getPurchaseOrderNumber().getValue());
        }
        
        // メインデータの更新
        発注データ purchaseOrderEntity = purchaseOrderEntityMapper.toEntity(purchaseOrder);
        purchaseOrderEntity.set作成日時(existingEntity.get作成日時());
        purchaseOrderEntity.set作成者名(existingEntity.get作成者名());
        purchaseOrderEntity.set更新日時(now);
        purchaseOrderEntity.set更新者名(username);
        purchaseOrderEntity.setVersion(existingEntity.getVersion());
        
        int result = purchaseOrderCustomMapper.updateByPrimaryKeyForOptimisticLock(purchaseOrderEntity);
        if (result == 0) {
            throw new ObjectOptimisticLockingFailureException(発注データ.class, purchaseOrder.getPurchaseOrderNumber().getValue());
        }
        
        // 既存明細の削除
        purchaseOrderLineCustomMapper.deleteByPurchaseOrderNumber(purchaseOrder.getPurchaseOrderNumber().getValue());
        
        // 新しい明細の挿入
        if (purchaseOrder.getPurchaseOrderLines() != null && !purchaseOrder.getPurchaseOrderLines().isEmpty()) {
            AtomicInteger lineNumber = new AtomicInteger(1);
            purchaseOrder.getPurchaseOrderLines().forEach(line -> {
                発注データ明細 lineEntity = purchaseOrderEntityMapper.toLineEntity(line);
                lineEntity.set発注番号(purchaseOrder.getPurchaseOrderNumber().getValue());
                lineEntity.set発注行番号(lineNumber.getAndIncrement());
                lineEntity.set作成日時(now);
                lineEntity.set作成者名(username);
                lineEntity.set更新日時(now);
                lineEntity.set更新者名(username);
                
                purchaseOrderLineCustomMapper.insert(lineEntity);
            });
        }
    }

    @Override
    public Optional<PurchaseOrder> findByPurchaseOrderNumber(String purchaseOrderNumber) {
        PurchaseOrderCustomEntity entity = purchaseOrderCustomMapper.selectByPrimaryKey(purchaseOrderNumber);
        if (entity != null) {
            return Optional.of(purchaseOrderEntityMapper.toModel(entity));
        }
        return Optional.empty();
    }

    @Override
    public void delete(String purchaseOrderNumber) {
        // 明細データの削除
        purchaseOrderLineCustomMapper.deleteByPurchaseOrderNumber(purchaseOrderNumber);
        
        // メインデータの削除
        purchaseOrderMapper.deleteByPrimaryKey(purchaseOrderNumber);
    }

    @Override
    public PurchaseOrderList findByCompletionFlag(CompletionFlag completionFlag) {
        List<PurchaseOrderCustomEntity> entities = purchaseOrderCustomMapper.selectAllWithCompletionFlag(completionFlag.getValue());
        List<PurchaseOrder> purchaseOrders = purchaseOrderEntityMapper.toModelList(entities);
        return PurchaseOrderList.of(purchaseOrders);
    }
    
    @Override
    public void deleteAll() {
        // 全明細データの削除
        purchaseOrderLineCustomMapper.deleteAll();
        
        // 全メインデータの削除
        purchaseOrderCustomMapper.deleteAll();
    }

    @Override
    public PurchaseOrderList selectAll() {
        List<PurchaseOrderCustomEntity> entities = purchaseOrderCustomMapper.selectAll();
        List<PurchaseOrder> purchaseOrders = purchaseOrderEntityMapper.toModelList(entities);
        return PurchaseOrderList.of(purchaseOrders);
    }

    @Override
    public PageInfo<PurchaseOrder> selectAllWithPageInfo() {
        List<PurchaseOrderCustomEntity> purchaseOrderCustomEntities = purchaseOrderCustomMapper.selectAll();
        PageInfo<PurchaseOrderCustomEntity> pageInfo = new PageInfo<>(purchaseOrderCustomEntities);
        
        return PageInfoHelper.of(pageInfo, purchaseOrderEntityMapper::toModel);
    }

    @Override
    public PageInfo<PurchaseOrder> searchWithPageInfo(PurchaseOrderCriteria criteria) {
        List<PurchaseOrderCustomEntity> purchaseOrderCustomEntities = purchaseOrderCustomMapper.selectByCriteria(criteria.toMap());
        PageInfo<PurchaseOrderCustomEntity> pageInfo = new PageInfo<>(purchaseOrderCustomEntities);
        
        return PageInfoHelper.of(pageInfo, purchaseOrderEntityMapper::toModel);
    }
}