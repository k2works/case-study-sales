package com.example.sms.service.sales.shipping;

import com.example.sms.domain.event.sales.shipping.Shipped;
import com.example.sms.service.inventory.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class ShippingEventHandler {

    /** 在庫を管理するアプリケーションサービス */
    private final InventoryService inventoryService;

    public ShippingEventHandler(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * 出荷完了時のイベントハンドラー
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleShippedEvent(Shipped shipped) {
        // 出荷が完了したら在庫を減らす
        this.inventoryService.processShipment(shipped);
    }
}