package com.example.sms.domain.model.inventory;

import com.example.sms.domain.model.master.product.ProductCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.commons.lang3.Validate.isTrue;

/**
 * 在庫
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class Inventory {
    String warehouseCode; // 倉庫コード
    ProductCode productCode; // 商品コード
    String lotNumber; // ロット番号
    String stockCategory; // 在庫区分
    String qualityCategory; // 良品区分
    Integer actualStockQuantity; // 実在庫数
    Integer availableStockQuantity; // 有効在庫数
    LocalDateTime lastShipmentDate; // 最終出荷日
    LocalDateTime createdDateTime; // 作成日時
    String createdBy; // 作成者名
    LocalDateTime updatedDateTime; // 更新日時
    String updatedBy; // 更新者名
    Integer version; // バージョン（楽観的排他制御用）
    String productName; // 商品名
    String warehouseName; // 倉庫名

    public static Inventory of(
            String warehouseCode,
            String productCode,
            String lotNumber,
            String stockCategory,
            String qualityCategory,
            Integer actualStockQuantity,
            Integer availableStockQuantity,
            LocalDateTime lastShipmentDate) {
        
        notNull(warehouseCode, "倉庫コードは必須です");
        notNull(productCode, "商品コードは必須です");
        notNull(lotNumber, "ロット番号は必須です");
        notNull(stockCategory, "在庫区分は必須です");
        notNull(qualityCategory, "良品区分は必須です");
        notNull(actualStockQuantity, "実在庫数は必須です");
        notNull(availableStockQuantity, "有効在庫数は必須です");
        
        isTrue(actualStockQuantity >= 0, "実在庫数は0以上である必要があります");
        isTrue(availableStockQuantity >= 0, "有効在庫数は0以上である必要があります");
        isTrue(availableStockQuantity <= actualStockQuantity, "有効在庫数は実在庫数以下である必要があります");
        
        return Inventory.builder()
                .warehouseCode(warehouseCode)
                .productCode(ProductCode.of(productCode))
                .lotNumber(lotNumber)
                .stockCategory(stockCategory)
                .qualityCategory(qualityCategory)
                .actualStockQuantity(actualStockQuantity)
                .availableStockQuantity(availableStockQuantity)
                .lastShipmentDate(lastShipmentDate)
                .build();
    }

    /**
     * 在庫キーを取得
     */
    public InventoryKey getKey() {
        return InventoryKey.of(
                warehouseCode,
                productCode.getValue(),
                lotNumber,
                stockCategory,
                qualityCategory
        );
    }

    /**
     * 在庫を調整
     */
    public Inventory adjustStock(Integer adjustmentQuantity) {
        Integer newActualStock = actualStockQuantity + adjustmentQuantity;
        Integer newAvailableStock = availableStockQuantity + adjustmentQuantity;
        
        isTrue(newActualStock >= 0, "調整後の実在庫数が負になります");
        isTrue(newAvailableStock >= 0, "調整後の有効在庫数が負になります");
        
        return this.toBuilder()
                .actualStockQuantity(newActualStock)
                .availableStockQuantity(newAvailableStock)
                .build();
    }

    /**
     * 在庫を予約
     */
    public Inventory reserve(Integer reserveQuantity) {
        isTrue(reserveQuantity > 0, "予約数量は正の数である必要があります");
        isTrue(availableStockQuantity >= reserveQuantity, "有効在庫数が不足しています");
        
        return this.toBuilder()
                .availableStockQuantity(availableStockQuantity - reserveQuantity)
                .build();
    }

    /**
     * 在庫を出荷
     */
    public Inventory ship(Integer shipmentQuantity, LocalDateTime shipmentDate) {
        isTrue(shipmentQuantity > 0, "出荷数量は正の数である必要があります");
        isTrue(actualStockQuantity >= shipmentQuantity, "実在庫数が不足しています");
        
        return this.toBuilder()
                .actualStockQuantity(actualStockQuantity - shipmentQuantity)
                .availableStockQuantity(Math.min(availableStockQuantity, actualStockQuantity - shipmentQuantity))
                .lastShipmentDate(shipmentDate)
                .build();
    }

    /**
     * 在庫を入荷
     */
    public Inventory receive(Integer receiptQuantity) {
        isTrue(receiptQuantity > 0, "入荷数量は正の数である必要があります");
        
        return this.toBuilder()
                .actualStockQuantity(actualStockQuantity + receiptQuantity)
                .availableStockQuantity(availableStockQuantity + receiptQuantity)
                .build();
    }

    /**
     * 在庫が利用可能か判定
     */
    public boolean isAvailable() {
        return availableStockQuantity > 0;
    }

    /**
     * 在庫が存在するか判定
     */
    public boolean hasStock() {
        return actualStockQuantity > 0;
    }
}