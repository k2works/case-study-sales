package com.example.sms.infrastructure.datasource.procurement.purchase;

import com.example.sms.domain.model.procurement.purchase.Purchase;
import com.example.sms.domain.model.procurement.purchase.PurchaseList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.ObjectOptimisticLockingFailureException;
import com.example.sms.infrastructure.datasource.autogen.mapper.仕入データMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.仕入データ明細Mapper;
import com.example.sms.infrastructure.datasource.autogen.model.仕入データ;
import com.example.sms.infrastructure.datasource.autogen.model.仕入データ明細;
import com.example.sms.service.procurement.purchase.PurchaseCriteria;
import com.example.sms.service.procurement.purchase.PurchaseRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class PurchaseDataSource implements PurchaseRepository {
    final 仕入データMapper purchaseMapper;
    final PurchaseCustomMapper purchaseCustomMapper;
    final 仕入データ明細Mapper purchaseLineMapper;
    final PurchaseLineCustomMapper purchaseLineCustomMapper;
    final PurchaseEntityMapper purchaseEntityMapper;

    public PurchaseDataSource(仕入データMapper purchaseMapper, PurchaseCustomMapper purchaseCustomMapper, 仕入データ明細Mapper purchaseLineMapper, PurchaseLineCustomMapper purchaseLineCustomMapper, PurchaseEntityMapper purchaseEntityMapper) {
        this.purchaseMapper = purchaseMapper;
        this.purchaseCustomMapper = purchaseCustomMapper;
        this.purchaseLineMapper = purchaseLineMapper;
        this.purchaseLineCustomMapper = purchaseLineCustomMapper;
        this.purchaseEntityMapper = purchaseEntityMapper;
    }

    @Override
    public Purchase save(Purchase purchase) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        Optional<PurchaseCustomEntity> existingEntity = Optional.ofNullable(purchaseCustomMapper.selectByPrimaryKey(purchase.getPurchaseNumber().getValue()));
        if (existingEntity.isEmpty()) {
            insertPurchase(purchase, username);
        } else {
            updatePurchase(purchase, username);
        }
        return purchase;
    }

    private void insertPurchase(Purchase purchase, String username) {
        LocalDateTime now = LocalDateTime.now();

        // メインデータの挿入
        仕入データ purchaseEntity = purchaseEntityMapper.toEntity(purchase);
        purchaseEntity.set作成日時(now);
        purchaseEntity.set作成者名(username);
        purchaseEntity.set更新日時(now);
        purchaseEntity.setUpdater(username);

        purchaseCustomMapper.insertForOptimisticLock(purchaseEntity);

        // 明細データの挿入
        if (purchase.getPurchaseLines() != null && !purchase.getPurchaseLines().isEmpty()) {
            AtomicInteger lineNumber = new AtomicInteger(1);
            purchase.getPurchaseLines().forEach(line -> {
                仕入データ明細 lineEntity = purchaseEntityMapper.toLineEntity(line);
                lineEntity.set仕入番号(purchase.getPurchaseNumber().getValue());
                lineEntity.set仕入行番号(lineNumber.getAndIncrement());
                lineEntity.set作成日時(now);
                lineEntity.set作成者名(username);
                lineEntity.set更新日時(now);
                lineEntity.set更新者名(username);

                purchaseLineCustomMapper.insert(lineEntity);
            });
        }
    }

    private void updatePurchase(Purchase purchase, String username) {
        LocalDateTime now = LocalDateTime.now();

        // 既存エンティティの取得
        PurchaseCustomEntity existingEntity = purchaseCustomMapper.selectByPrimaryKey(purchase.getPurchaseNumber().getValue());
        if (existingEntity == null) {
            throw new IllegalStateException("更新対象の仕入データが見つかりません: " + purchase.getPurchaseNumber().getValue());
        }

        // メインデータの更新
        仕入データ purchaseEntity = purchaseEntityMapper.toEntity(purchase);
        purchaseEntity.set作成日時(existingEntity.get作成日時());
        purchaseEntity.set作成者名(existingEntity.get作成者名());
        purchaseEntity.set更新日時(now);
        purchaseEntity.setUpdater(username);

        int result = purchaseCustomMapper.updateByPrimaryKeyForOptimisticLock(purchaseEntity);
        if (result == 0) {
            throw new ObjectOptimisticLockingFailureException(仕入データ.class, purchase.getPurchaseNumber().getValue());
        }

        // 既存明細の削除
        purchaseLineCustomMapper.deleteByPurchaseNumber(purchase.getPurchaseNumber().getValue());

        // 新しい明細の挿入
        if (purchase.getPurchaseLines() != null && !purchase.getPurchaseLines().isEmpty()) {
            AtomicInteger lineNumber = new AtomicInteger(1);
            purchase.getPurchaseLines().forEach(line -> {
                仕入データ明細 lineEntity = purchaseEntityMapper.toLineEntity(line);
                lineEntity.set仕入番号(purchase.getPurchaseNumber().getValue());
                lineEntity.set仕入行番号(lineNumber.getAndIncrement());
                lineEntity.set作成日時(now);
                lineEntity.set作成者名(username);
                lineEntity.set更新日時(now);
                lineEntity.set更新者名(username);

                purchaseLineCustomMapper.insert(lineEntity);
            });
        }
    }

    @Override
    public Optional<Purchase> findByPurchaseNumber(String purchaseNumber) {
        PurchaseCustomEntity entity = purchaseCustomMapper.selectByPrimaryKey(purchaseNumber);
        if (entity != null) {
            return Optional.of(purchaseEntityMapper.toModel(entity));
        }
        return Optional.empty();
    }

    @Override
    public void delete(String purchaseNumber) {
        // 明細データの削除
        purchaseLineCustomMapper.deleteByPurchaseNumber(purchaseNumber);

        // メインデータの削除
        purchaseMapper.deleteByPrimaryKey(purchaseNumber);
    }

    @Override
    public void deleteAll() {
        // 全明細データの削除
        purchaseLineCustomMapper.deleteAll();

        // 全メインデータの削除
        purchaseCustomMapper.deleteAll();
    }

    @Override
    public PurchaseList selectAll() {
        List<PurchaseCustomEntity> entities = purchaseCustomMapper.selectAll();
        List<Purchase> purchases = purchaseEntityMapper.toModelList(entities);
        return PurchaseList.of(purchases);
    }

    @Override
    public PageInfo<Purchase> selectAllWithPageInfo() {
        List<PurchaseCustomEntity> purchaseCustomEntities = purchaseCustomMapper.selectAll();
        PageInfo<PurchaseCustomEntity> pageInfo = new PageInfo<>(purchaseCustomEntities);

        return PageInfoHelper.of(pageInfo, purchaseEntityMapper::toModel);
    }

    @Override
    public PageInfo<Purchase> searchWithPageInfo(PurchaseCriteria criteria) {
        List<PurchaseCustomEntity> purchaseCustomEntities = purchaseCustomMapper.selectByCriteria(criteria.toMap());
        PageInfo<PurchaseCustomEntity> pageInfo = new PageInfo<>(purchaseCustomEntities);

        return PageInfoHelper.of(pageInfo, purchaseEntityMapper::toModel);
    }
}
