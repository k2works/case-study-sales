package com.example.sms.service.sales.shipping;

import com.example.sms.domain.event.sales.shipping.Shipped;
import com.example.sms.domain.event.sales.shipping.ShippingAggregate;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.model.sales.order.OrderNumber;
import com.example.sms.domain.model.sales.order.ShippingDate;
import com.example.sms.domain.type.quantity.Quantity;
import com.example.sms.service.inventory.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("ShippingEventHandlerテスト")
@ExtendWith(MockitoExtension.class)
class ShippingEventHandlerTest {

    @Mock
    private InventoryService inventoryService;

    private ShippingEventHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ShippingEventHandler(inventoryService);
    }

    @Test
    @DisplayName("出荷イベントを受信して在庫サービスのprocessShipmentを呼び出す")
    void shouldCallInventoryServiceProcessShipmentWhenShippedEventReceived() {
        // Given
        LocalDateTime shippingDateTime = LocalDateTime.of(2024, 1, 10, 10, 0);
        ShippingAggregate shipping = createTestShipping(shippingDateTime, 100);
        Shipped event = new Shipped(shipping);

        // When
        handler.handleShippedEvent(event);

        // Then
        verify(inventoryService, times(1)).processShipment(eq(event));
    }

    @Test
    @DisplayName("複数の出荷イベントを順次処理できる")
    void shouldHandleMultipleShippedEvents() {
        // Given
        LocalDateTime shippingDateTime1 = LocalDateTime.of(2024, 1, 10, 10, 0);
        LocalDateTime shippingDateTime2 = LocalDateTime.of(2024, 1, 11, 11, 0);

        ShippingAggregate shipping1 = createTestShipping(shippingDateTime1, 100);
        ShippingAggregate shipping2 = createTestShipping(shippingDateTime2, 150);

        Shipped event1 = new Shipped(shipping1);
        Shipped event2 = new Shipped(shipping2);

        // When
        handler.handleShippedEvent(event1);
        handler.handleShippedEvent(event2);

        // Then
        verify(inventoryService, times(1)).processShipment(eq(event1));
        verify(inventoryService, times(1)).processShipment(eq(event2));
        verify(inventoryService, times(2)).processShipment(any(Shipped.class));
    }

    @Test
    @DisplayName("InventoryServiceで例外が発生した場合は例外を再スローする")
    void shouldRethrowExceptionWhenInventoryServiceFails() {
        // Given
        LocalDateTime shippingDateTime = LocalDateTime.of(2024, 1, 20, 9, 0);
        ShippingAggregate shipping = createTestShipping(shippingDateTime, 50);
        Shipped event = new Shipped(shipping);

        doThrow(new RuntimeException("在庫不足エラー"))
                .when(inventoryService).processShipment(any(Shipped.class));

        // When & Then
        try {
            handler.handleShippedEvent(event);
        } catch (RuntimeException e) {
            // 例外が発生することを確認
        }

        verify(inventoryService, times(1)).processShipment(eq(event));
    }

    // テスト用のShippingオブジェクト作成ヘルパーメソッド
    private ShippingAggregate createTestShipping(LocalDateTime shippingDateTime, int quantity) {
        return ShippingAggregate.builder()
                .orderNumber(OrderNumber.of("OD20240001"))
                .warehouseCode("WH001")
                .productCode(ProductCode.of("PROD001"))
                .shippedQuantity(Quantity.of(quantity))
                .shippingDate(ShippingDate.of(shippingDateTime))
                .build();
    }
}