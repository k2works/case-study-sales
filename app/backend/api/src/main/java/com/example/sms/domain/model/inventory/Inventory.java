package com.example.sms.domain.model.inventory;

import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.type.quantity.Quantity;
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
    WarehouseCode warehouseCode; // 倉庫コード
    ProductCode productCode; // 商品コード
    String lotNumber; // ロット番号
    StockCategory stockCategory; // 在庫区分
    QualityCategory qualityCategory; // 良品区分
    Quantity actualStockQuantity; // 実在庫数
    Quantity availableStockQuantity; // 有効在庫数
    LocalDateTime lastShipmentDate; // 最終出荷日
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
                .warehouseCode(WarehouseCode.of(warehouseCode))
                .productCode(ProductCode.of(productCode))
                .lotNumber(lotNumber)
                .stockCategory(StockCategory.of(stockCategory))
                .qualityCategory(QualityCategory.of(qualityCategory))
                .actualStockQuantity(Quantity.of(actualStockQuantity))
                .availableStockQuantity(Quantity.of(availableStockQuantity))
                .lastShipmentDate(lastShipmentDate)
                .build();
    }

    /**
     * 在庫キーを取得
     */
    public InventoryKey getKey() {
        return InventoryKey.of(
                warehouseCode.getValue(),
                productCode.getValue(),
                lotNumber,
                stockCategory.getCode(),
                qualityCategory.getCode()
        );
    }

    /**
     * 在庫を調整
     */
    public Inventory adjustStock(Integer adjustmentQuantity) {
        Quantity adjustment = Quantity.of(Math.abs(adjustmentQuantity));
        Quantity newActualStock = adjustmentQuantity >= 0
            ? actualStockQuantity.plus(adjustment)
            : actualStockQuantity.minus(adjustment);
        Quantity newAvailableStock = adjustmentQuantity >= 0
            ? availableStockQuantity.plus(adjustment)
            : availableStockQuantity.minus(adjustment);

        isTrue(newActualStock.getAmount() >= 0, "調整後の実在庫数が負になります");
        isTrue(newAvailableStock.getAmount() >= 0, "調整後の有効在庫数が負になります");

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
        isTrue(availableStockQuantity.getAmount() >= reserveQuantity, "有効在庫数が不足しています");

        Quantity reserve = Quantity.of(reserveQuantity);
        return this.toBuilder()
                .availableStockQuantity(availableStockQuantity.minus(reserve))
                .build();
    }

    /**
     * 在庫を出荷
     */
    public Inventory ship(Integer shipmentQuantity, LocalDateTime shipmentDate) {
        isTrue(shipmentQuantity > 0, "出荷数量は正の数である必要があります");
        isTrue(actualStockQuantity.getAmount() >= shipmentQuantity, "実在庫数が不足しています");

        Quantity shipment = Quantity.of(shipmentQuantity);
        Quantity newActualStock = actualStockQuantity.minus(shipment);
        Quantity newAvailableStock = Quantity.of(Math.min(availableStockQuantity.getAmount(), newActualStock.getAmount()));

        return this.toBuilder()
                .actualStockQuantity(newActualStock)
                .availableStockQuantity(newAvailableStock)
                .lastShipmentDate(shipmentDate)
                .build();
    }

    /**
     * 在庫を入荷
     */
    public Inventory receive(Integer receiptQuantity) {
        isTrue(receiptQuantity > 0, "入荷数量は正の数である必要があります");

        Quantity receipt = Quantity.of(receiptQuantity);
        return this.toBuilder()
                .actualStockQuantity(actualStockQuantity.plus(receipt))
                .availableStockQuantity(availableStockQuantity.plus(receipt))
                .build();
    }

    /**
     * 在庫が利用可能か判定
     */
    public boolean isAvailable() {
        return availableStockQuantity.getAmount() > 0;
    }

    /**
     * 在庫が存在するか判定
     */
    public boolean hasStock() {
        return actualStockQuantity.getAmount() > 0;
    }
}