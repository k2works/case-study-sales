package com.example.sms.infrastructure.datasource.inventory;

import com.example.sms.domain.model.inventory.Inventory;
import com.example.sms.domain.model.inventory.InventoryKey;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.infrastructure.datasource.autogen.model.在庫データ;
import com.example.sms.infrastructure.datasource.autogen.model.在庫データKey;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 在庫エンティティマッパー
 */
@Component
public class InventoryEntityMapper {

    /**
     * ドメインモデルのキーから在庫データキーに変換
     */
    public 在庫データKey toDataKey(InventoryKey key) {
        if (key == null) return null;
        
        在庫データKey dataKey = new 在庫データKey();
        dataKey.set倉庫コード(key.getWarehouseCode());
        dataKey.set商品コード(key.getProductCode());
        dataKey.setロット番号(key.getLotNumber());
        dataKey.set在庫区分(key.getStockCategory());
        dataKey.set良品区分(key.getQualityCategory());
        return dataKey;
    }

    /**
     * CustomEntityからドメインモデルに変換
     */
    public Inventory toModel(InventoryCustomEntity entity) {
        if (entity == null) return null;
        
        return Inventory.builder()
                .warehouseCode(entity.get倉庫コード())
                .productCode(ProductCode.of(entity.get商品コード()))
                .lotNumber(entity.getロット番号())
                .stockCategory(entity.get在庫区分())
                .qualityCategory(entity.get良品区分())
                .actualStockQuantity(entity.get実在庫数())
                .availableStockQuantity(entity.get有効在庫数())
                .lastShipmentDate(entity.get最終出荷日())
                .productName(entity.get商品名())
                .warehouseName(entity.get倉庫名())
                .build();
    }

    /**
     * CustomEntityのListからドメインモデルのListに変換
     */
    public List<Inventory> toModelList(List<InventoryCustomEntity> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    /**
     * ドメインモデルから自動生成エンティティに変換
     */
    public 在庫データ toEntity(Inventory model) {
        if (model == null) return null;
        
        在庫データ entity = new 在庫データ();
        entity.set倉庫コード(model.getWarehouseCode());
        entity.set商品コード(model.getProductCode().getValue());
        entity.setロット番号(model.getLotNumber());
        entity.set在庫区分(model.getStockCategory());
        entity.set良品区分(model.getQualityCategory());
        entity.set実在庫数(model.getActualStockQuantity());
        entity.set有効在庫数(model.getAvailableStockQuantity());
        entity.set最終出荷日(model.getLastShipmentDate());
        return entity;
    }

    /**
     * CustomEntityから在庫データキーに変換
     */
    public 在庫データKey toKey(InventoryCustomEntity entity) {
        if (entity == null) return null;
        
        在庫データKey key = new 在庫データKey();
        key.set倉庫コード(entity.get倉庫コード());
        key.set商品コード(entity.get商品コード());
        key.setロット番号(entity.getロット番号());
        key.set在庫区分(entity.get在庫区分());
        key.set良品区分(entity.get良品区分());
        return key;
    }

    /**
     * CustomEntityから自動生成エンティティに変換
     */
    public 在庫データ toEntity(InventoryCustomEntity customEntity) {
        if (customEntity == null) return null;
        
        在庫データ entity = new 在庫データ();
        entity.set倉庫コード(customEntity.get倉庫コード());
        entity.set商品コード(customEntity.get商品コード());
        entity.setロット番号(customEntity.getロット番号());
        entity.set在庫区分(customEntity.get在庫区分());
        entity.set良品区分(customEntity.get良品区分());
        entity.set実在庫数(customEntity.get実在庫数());
        entity.set有効在庫数(customEntity.get有効在庫数());
        entity.set最終出荷日(customEntity.get最終出荷日());
        entity.set作成日時(customEntity.get作成日時());
        entity.set作成者名(customEntity.get作成者名());
        entity.set更新日時(customEntity.get更新日時());
        entity.set更新者名(customEntity.get更新者名());
        entity.setVersion(customEntity.getVersion());
        return entity;
    }

    /**
     * CustomEntityのListから自動生成エンティティのListに変換
     */
    public List<在庫データ> toEntityList(List<InventoryCustomEntity> customEntities) {
        if (customEntities == null) return null;
        return customEntities.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}