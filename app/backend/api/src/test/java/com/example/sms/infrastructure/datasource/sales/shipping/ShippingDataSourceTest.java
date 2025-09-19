package com.example.sms.infrastructure.datasource.sales.shipping;

import com.example.sms.domain.event.sales.shipping.Shipped;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.model.sales.order.OrderNumber;
import com.example.sms.domain.model.sales.order.ShippingDate;
import com.example.sms.domain.model.sales.shipping.Shipping;
import com.example.sms.domain.model.sales.shipping.ShippingList;
import com.example.sms.domain.type.quantity.Quantity;
import com.example.sms.infrastructure.datasource.autogen.mapper.受注データMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.受注データ明細Mapper;
import com.example.sms.infrastructure.datasource.sales.order.OrderCustomEntity;
import com.example.sms.infrastructure.datasource.sales.order.OrderCustomMapper;
import com.example.sms.infrastructure.datasource.sales.order.order_line.OrderLineCustomEntity;
import com.example.sms.infrastructure.datasource.sales.order.order_line.OrderLineCustomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("ShippingDataSourceテスト")
@ExtendWith(MockitoExtension.class)
class ShippingDataSourceTest {

    @Mock
    private 受注データMapper salesOrderMapper;
    @Mock
    private OrderCustomMapper orderCustomMapper;
    @Mock
    private 受注データ明細Mapper salesOrderLineMapper;
    @Mock
    private OrderLineCustomMapper orderLineCustomMapper;
    @Mock
    private ShippingEntityMapper shippingEntityMapper;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    private ShippingDataSource shippingDataSource;

    @BeforeEach
    void setUp() {
        shippingDataSource = new ShippingDataSource(
                salesOrderMapper,
                orderCustomMapper,
                salesOrderLineMapper,
                orderLineCustomMapper,
                shippingEntityMapper,
                eventPublisher
        );
    }

    @Test
    @DisplayName("updateSalesOrderLine - 出荷済数量が増加した場合にShippedイベントを発行する")
    void shouldPublishShippedEventWhenShippedQuantityIncreases() {
        // Given
        LocalDateTime shippingDateTime = LocalDateTime.of(2024, 1, 10, 10, 0);
        Shipping shipping = createTestShipping(shippingDateTime, 100);
        ShippingList shippingList = new ShippingList(List.of(shipping));

        OrderCustomEntity orderEntity = mock(OrderCustomEntity.class);
        OrderLineCustomEntity orderLineEntity = mock(OrderLineCustomEntity.class);

        when(orderCustomMapper.selectByPrimaryKey("OD20240001")).thenReturn(orderEntity);
        when(orderEntity.get受注データ明細()).thenReturn(List.of(orderLineEntity));
        when(orderLineEntity.get受注行番号()).thenReturn(1);
        when(orderLineEntity.get出荷済数量()).thenReturn(50); // 前回の出荷済数量
        when(orderLineEntity.get作成日時()).thenReturn(LocalDateTime.now().minusDays(1));
        when(orderLineEntity.get作成者名()).thenReturn("test-user");

        when(shippingEntityMapper.mapToEntity(any(), any())).thenReturn(new com.example.sms.infrastructure.datasource.autogen.model.受注データ明細());

        ArgumentCaptor<Shipped> eventCaptor = ArgumentCaptor.forClass(Shipped.class);

        // When
        shippingDataSource.updateSalesOrderLine(shippingList);

        // Then
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
        Shipped capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent).isNotNull();
        assertThat(capturedEvent.getShipping()).isNotNull();
        assertThat(capturedEvent.getShipping().getOrderNumber()).isEqualTo(OrderNumber.of("OD20240001"));
        assertThat(capturedEvent.getShipping().getShippedQuantity()).isEqualTo(Quantity.of(100));
    }

    @Test
    @DisplayName("updateSalesOrderLine - 出荷済数量が変わらない場合はイベントを発行しない")
    void shouldNotPublishEventWhenShippedQuantityDoesNotIncrease() {
        // Given
        LocalDateTime shippingDateTime = LocalDateTime.of(2024, 1, 10, 10, 0);
        Shipping shipping = createTestShipping(shippingDateTime, 50); // 同じ数量
        ShippingList shippingList = new ShippingList(List.of(shipping));

        OrderCustomEntity orderEntity = mock(OrderCustomEntity.class);
        OrderLineCustomEntity orderLineEntity = mock(OrderLineCustomEntity.class);

        when(orderCustomMapper.selectByPrimaryKey("OD20240001")).thenReturn(orderEntity);
        when(orderEntity.get受注データ明細()).thenReturn(List.of(orderLineEntity));
        when(orderLineEntity.get受注行番号()).thenReturn(1);
        when(orderLineEntity.get出荷済数量()).thenReturn(50); // 同じ出荷済数量
        when(orderLineEntity.get作成日時()).thenReturn(LocalDateTime.now().minusDays(1));
        when(orderLineEntity.get作成者名()).thenReturn("test-user");

        when(shippingEntityMapper.mapToEntity(any(), any())).thenReturn(new com.example.sms.infrastructure.datasource.autogen.model.受注データ明細());

        // When
        shippingDataSource.updateSalesOrderLine(shippingList);

        // Then
        verify(eventPublisher, never()).publishEvent(any(Shipped.class));
    }

    // テスト用のShippingオブジェクト作成ヘルパーメソッド
    private Shipping createTestShipping(LocalDateTime shippingDateTime, int quantity) {
        return Shipping.builder()
                .orderNumber(OrderNumber.of("OD20240001"))
                .orderLineNumber(1)
                .warehouseCode("WH001")
                .productCode(ProductCode.of("PROD001"))
                .shippedQuantity(Quantity.of(quantity))
                .shipmentInstructionQuantity(Quantity.of(quantity))
                .shippingDate(ShippingDate.of(shippingDateTime))
                .build();
    }
}